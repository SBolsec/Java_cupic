package hr.fer.oprpp1.hw08.jnotepadpp.document;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Represents a single document, having information about file path
 * from which document was loaded (can be null for new document), document
 * modification status and reference to Swing component which is used for 
 * editing (each document has its own editor component).
 * @author sbolsec
 *
 */
public interface SingleDocumentModel {
	/**
	 * Returns <code>JTextArea</code> component of this document.
	 * @return <code>JTextArea</code> component of this document
	 */
	JTextArea getTextComponent();
	
	/**
	 * Returns file path of this document.
	 * @return file path of this document
	 */
	Path getFilePath();
	
	/**
	 * Sets file path of this document.
	 * @param path path to be set
	 * @throws NullPointerException if given path is null
	 */
	void setFilePath(Path path);
	
	/**
	 * Checks whether the document has been modified.
	 * @return true if document has been modfied, false otherwise
	 */
	boolean isModified();
	
	/**
	 * Sets whether the document has beeen modified.
	 * @param modified status to set
	 */
	void setModified(boolean modified);
	
	/**
	 * Adds <code>SingleDocumentListener</code> to this <code>SingleDocumentModel</code>.
	 * @param l <code>SingleDocumentListener</code> to be added
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Removes <code>SingleDocumentListener</code> from this <code>SingleDocumentModel</code>.
	 * @param l <code>SingleDocumentListener</code> to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
