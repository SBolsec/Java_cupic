package hr.fer.oprpp1.hw08.jnotepadpp.document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.oprpp1.hw08.jnotepadpp.Util;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	/** Generated serial version UID **/
	private static final long serialVersionUID = -798335009799139777L;
	
	/** Current <code>SingleDocumentModel</code> **/
	private SingleDocumentModel currentDocument;
	/** Tab icon when document is not modified **/
	private ImageIcon icon = new Util().getIcon("icons/greenDisk.png");
	/** Tab icon when document is modified**/
	private ImageIcon iconModified = new Util().getIcon("icons/redDisk.png");
	
	/** List of documents **/
	private final List<SingleDocumentModel> documents = new ArrayList<>();
	/** List of listeners registered to this <code>MultipleDocumentModel</code> **/
	private final List<MultipleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Listener that listens to changes of each document
	 */
	private SingleDocumentListener listener = new SingleDocumentListener() {
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			int index = documents.indexOf(model);
			if (index == -1) {
				return;
			} else if (model.isModified()) {
				setIconAt(documents.indexOf(model), iconModified);
			} else {
				setIconAt(documents.indexOf(model), icon);
			}
		}
		
		@Override
		public void documentFilePathUpdated(SingleDocumentModel model) {
			
		}
	};
	
	/**
	 * Constructor.
	 */
	public DefaultMultipleDocumentModel() {
		super();
		
		this.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (getSelectedIndex() == -1) {
					currentDocument = null;
					return;
				}
				currentDocument = getDocument(getSelectedIndex());
			}
		});
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
		currentDocument = document;
		documents.add(document);
		document.addSingleDocumentListener(listener);
		
		return document;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		if (path == null)
			throw new NullPointerException("Provided path can not be null!");
		if (!Files.exists(path))
			throw new IllegalArgumentException("Provided path does not exist!");
		if (!Files.isReadable(path))
			throw new IllegalArgumentException("Provided path does not have read permission!");
		if (!Files.isRegularFile(path))
			throw new IllegalArgumentException("Provided path is not a regular file!");
		
		// If document for provided path already exists just switch to it
		for (int i = 0; i < documents.size(); i++) {
			SingleDocumentModel d = documents.get(i);
			if (d.getFilePath() != null && d.getFilePath().equals(path)) {
				this.currentDocument = d;
				setSelectedIndex(i);
				return d;
			}
		}
		
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error while reading the file!");
		}
		
		String text = new String(bytes, StandardCharsets.UTF_8);
		SingleDocumentModel model = new DefaultSingleDocumentModel(path, text);
		documents.add(model);
		currentDocument = model;
		model.addSingleDocumentListener(listener);
		
		JScrollPane scrollPane = new JScrollPane(model.getTextComponent());
		
		addTab(path.getFileName().toString(), icon, scrollPane, path.toString());
		setSelectedIndex(this.getTabCount()-1);
		setTitleAt(getSelectedIndex(), model.getFilePath().getFileName().toString());
		setToolTipTextAt(getSelectedIndex(), model.getFilePath().toString());
		
		if (documents.size() == 2) { // If there is one empty document
			if (!documents.get(0).isModified() && documents.get(0).getFilePath() == null) {
				documents.remove(0);
				this.removeTabAt(0);
			}
		}
		return model;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path path = newPath != null ? newPath : model.getFilePath();
		if (path == null) {
			throw new IllegalArgumentException("Path is null!");
		}
		
		String text = model.getTextComponent().getText();
		byte[] podatci = text.getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(path, podatci);
			
			SingleDocumentModel doc = new DefaultSingleDocumentModel(path, text);
			int index = documents.indexOf(model);
			//model.removeSingleDocumentListener(listener);
			documents.remove(index);
			documents.add(index, doc);
			currentDocument = doc;
			doc.addSingleDocumentListener(listener);
			
			JScrollPane scrollPane = new JScrollPane(doc.getTextComponent());
			removeTabAt(index);
			insertTab(path.getFileName().toString(), icon, scrollPane, path.toString(), index);
			setSelectedIndex(index);
			setTitleAt(index, doc.getFilePath().getFileName().toString());
			setToolTipTextAt(index, doc.getFilePath().toString());
		} catch (IOException e1) {
			throw new IllegalArgumentException("Save failed!");
		}
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		documents.remove(model);
		removeTabAt(index);
		if (index == 0) {
			if (documents.size() != 0) {
				currentDocument = documents.get(0);
				setSelectedIndex(0);
			} else {
				currentDocument = null;
				setSelectedIndex(-1);
			}
		}
		else {
			currentDocument = documents.get(index-1);
			setSelectedIndex(index-1);
		}
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}
}
