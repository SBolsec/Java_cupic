package hr.fer.oprpp1.hw08.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;

/**
 * Default implementation of the <code>SingleDocumentModel</code> interface.
 * @author sbolsec
 *
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/** Path of the document **/
	private Path filePath;
	/** Used as editor for document content **/
	private JTextArea textArea;
	/** Initial text of document **/
	private final String ogText;
	/** Flag which signals if the document has been modified **/
	private boolean modified;
	/** List of listeners registered to this document **/
	private final List<SingleDocumentListener> listeners = new ArrayList<>();
	
	/**
	 * Constructor which initializes the file path and content of the text component.
	 * @param filePath path to document
	 * @param text text content of document
	 */
	public DefaultSingleDocumentModel(Path filePath, String text) {
		this.filePath = filePath;
		this.ogText = text;
		textArea = new JTextArea(text);
		modified = false;
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkModified();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkModified();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				checkModified();
			}
		});
	}
	
	/**
	 * Checks whether the text has been modified and informs
	 * all listeners.
	 */
	private void checkModified() {
		setModified(!textArea.getText().equals(ogText));
	}

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		this.filePath = path;
		
		for (SingleDocumentListener l : listeners) {
			l.documentFilePathUpdated(this);
		}
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		
		for (SingleDocumentListener l : listeners) {
			l.documentModifyStatusUpdated(this);
		}
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}

}
