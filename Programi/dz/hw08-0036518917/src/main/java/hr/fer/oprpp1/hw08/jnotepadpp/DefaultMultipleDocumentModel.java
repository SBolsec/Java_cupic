package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;

/**
 * Default implementation of the <code>MultipleDocumentModel</code> interface.
 * @author sbolsec
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	/** Generated serial version UID **/
	private static final long serialVersionUID = -798335009799139777L;
	
	/** Current <code>SingleDocumentModel</code> **/
	private SingleDocumentModel currentDocument;
	/** Current tab index **/
	private int currentTabIndex = 0;
	/** Previous tab index **/
	private int previousTabIndex = 0;
	/** Tab icon when document is not modified **/
	private ImageIcon icon = getIcon("icons/greenDisk.png");
	/** Tab icon when document is modified**/
	private ImageIcon iconModified = getIcon("icons/redDisk.png");
	
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
			int index = documents.indexOf(model);
			if (index == -1)
				return;
			setTitleAt(index, model.getFilePath().getFileName().toString());
			setToolTipTextAt(index, model.getFilePath().toString());
		}
	};
	
	/**
	 * Constructor.
	 */
	public DefaultMultipleDocumentModel() {
		super();
		
		// Change listener which keeps track of currently opened tab
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				previousTabIndex = currentTabIndex;
				currentTabIndex = getSelectedIndex();
				
				SingleDocumentModel previuos = null;
				if (previousTabIndex < documents.size() && previousTabIndex != -1) {
					previuos = documents.get(previousTabIndex);
				}
				SingleDocumentModel current = currentDocument;
				if (currentTabIndex < documents.size() && currentTabIndex != -1) {
					current = documents.get(currentTabIndex);
				}
				final SingleDocumentModel a = previuos;
				final SingleDocumentModel b = current;
				currentDocument = b;
				
				fire(l -> l.currentDocumentChanged(a, b));
			}
		});
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		SingleDocumentModel previous = currentDocument;
		
		// Create new empty document
		SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
		currentDocument = document;
		documents.add(document);
		document.addSingleDocumentListener(listener);
		
		// Add it to new tab
		JScrollPane scrollPane = new JScrollPane(document.getTextComponent());
		String title = "unnamed";
		String tip = "Unnamed document";
		addTab(title, iconModified, scrollPane, tip);
		setSelectedIndex(documents.size()-1);
		
		// Notify registered listeners that a document has been added
		fire(l -> l.documentAdded(document));
		
		// Notify registered listeners that current document has changed
		fire(l -> l.currentDocumentChanged(previous, currentDocument));
		
		return document;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		// Check if the path is valid
		if (path == null)
			throw new NullPointerException("Provided path can not be null!");
		if (!Files.exists(path))
			throw new IllegalArgumentException("Provided path does not exist!");
		if (!Files.isReadable(path))
			throw new IllegalArgumentException("Provided path does not have read permission!");
		if (!Files.isRegularFile(path))
			throw new IllegalArgumentException("Provided path is not a regular file!");
		
		SingleDocumentModel previous = currentDocument;
		
		// If document for provided path already exists just switch to it
		for (int i = 0; i < documents.size(); i++) {
			SingleDocumentModel d = documents.get(i);
			if (d.getFilePath() != null && d.getFilePath().equals(path)) {
				this.currentDocument = d;
				setSelectedIndex(i);
				return d;
			}
		}
		
		// Load content of file
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error while reading the file!");
		}
		
		// Create new document for the file
		String text = new String(bytes, StandardCharsets.UTF_8);
		SingleDocumentModel model = new DefaultSingleDocumentModel(path, text);
		documents.add(model);
		currentDocument = model;
		model.addSingleDocumentListener(listener);
		
		// Add document to new tab
		JScrollPane scrollPane = new JScrollPane(model.getTextComponent());
		addTab(path.getFileName().toString(), icon, scrollPane, path.toString());
		setSelectedIndex(getTabCount()-1);
		setTitleAt(getSelectedIndex(), model.getFilePath().getFileName().toString());
		setToolTipTextAt(getSelectedIndex(), model.getFilePath().toString());
		
		// If there was only a empty unnamed document before loading this document,
		// delete the empty document
		if (documents.size() == 2) {
			if (!documents.get(0).isModified() && documents.get(0).getFilePath() == null) {
				SingleDocumentModel d = documents.get(0);
				documents.remove(0);
				this.removeTabAt(0);
				fire(l -> l.documentRemoved(d));
			}
		}
		
		// Notify registered listeners that a document has been added
		fire(l -> l.documentAdded(model));
		
		// Notify registered listeners that current document has changed
		fire(l -> l.currentDocumentChanged(previous, currentDocument));
		
		return model;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		// Determine which path to use
		Path path = newPath != null ? newPath : model.getFilePath();
		if (path == null) {
			throw new IllegalArgumentException("Path is null!");
		}
		
		// If the path already exists throw an exception
		for (SingleDocumentModel d : documents) {
			if (d.equals(model)) 
				continue;
			if (path.equals(d.getFilePath()))
				throw new IllegalArgumentException("Specified file is already opened!");
		}
		
		// Save document to disk
		String text = model.getTextComponent().getText();
		byte[] podatci = text.getBytes(StandardCharsets.UTF_8);
		try {
			Files.write(path, podatci);
		} catch (IOException e1) {
			throw new IllegalArgumentException("Save failed!");
		}
		
		// Create new document so that the modification detection works properly
		// and remove the old document
		SingleDocumentModel doc = new DefaultSingleDocumentModel(path, text);
		int index = documents.indexOf(model);
		//model.removeSingleDocumentListener(listener);
		documents.remove(index);
		documents.add(index, doc);
		currentDocument = doc;
		doc.addSingleDocumentListener(listener);
		
		// Add new document to the tab where the previous document was
		JScrollPane scrollPane = new JScrollPane(doc.getTextComponent());
		removeTabAt(index);
		insertTab(path.getFileName().toString(), icon, scrollPane, path.toString(), index);
		setSelectedIndex(index);
		setTitleAt(index, doc.getFilePath().getFileName().toString());
		setToolTipTextAt(index, doc.getFilePath().toString());
		
		fire(l -> l.currentDocumentChanged(model, currentDocument));
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		SingleDocumentModel previous = currentDocument;
		
		// Remove document and its tab
		int index = documents.indexOf(model);
		documents.remove(model);
		removeTabAt(index);
		
		// Set new tab and currentDocument
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
		
		// Notify registered listeners that the document has been removed
		fire(l -> l.documentRemoved(model));
		
		// Notify registered listeners that current document has changed
		fire(l -> l.currentDocumentChanged(previous, currentDocument));
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
	
	/**
	 * Returns icon from provided path or null
	 * @param name path of image
	 * @return <code>ImageIcon</code> created from provided path
	 */
	private ImageIcon getIcon(String name) {
		InputStream is = this.getClass().getResourceAsStream(name);
		if (is == null)
			return null;
		
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
			return new ImageIcon(bytes);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Notifies all the registered listeners using given consumer.
	 * @param consumer consumer which is used to notify all listeners
	 */
	private void fire(Consumer<MultipleDocumentListener> consumer) {
		for (MultipleDocumentListener l : listeners) {
			consumer.accept(l);
		}
	}
}
