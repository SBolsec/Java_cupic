package hr.fer.oprpp1.hw08.jnotepadpp.document;

import java.nio.file.Path;

/**
 * Represents a model capable of holding zero, one or more documents,
 * where each document and having a concept of current document - the one
 * which is shown to the user and on whihc user works.
 * @author sbolsec
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Creates new <code>SingleDocumentModel</code>.
	 * @return newly created instance of <code>SingleDocumentModel</code>
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Returns current <code>SingleDocumentModel</code>.
	 * @return current <code>SingleDocumentModel</code>
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Loads document from given path and creates new 
	 * <code>SingleDocumentModel</code> using loaded document.
	 * @param path path from which to load document
	 * @return new <code>SingleDocumentModel</code> created from given document
	 * @throws NullPointerException if path was null
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Saves document to the given path. If given newPath is null document will be
	 * saved using path associated to it, otherwise newPath will be used and after
	 * saving is completed, document's path will be updated to newPath.
	 * @param model model which to save
	 * @param newPath path where to save document
	 * @throws IllegalArgumentException if method is called with newPath of some existing <code>SingleDocumentModel</code>
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Removes specified document (does not check modification status or ask
	 * any questions).
	 * @param model document which to close
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Adds <code>MultipleDocumentListener</code> to this <code>MultipleDocumentModel</code>.
	 * @param l <code>MultipleDocumentListener</code> to be added
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Removes <code>MultipleDocumentListener</code> from this <code>MultipleDocumentModel</code>.
	 * @param l <code>MultipleDocumentListener</code> to be removed
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Returns number of documents.
	 * @return number of documents
	 */
	int getNumberOfDocuments();
	
	/**
	 * Returns document at given index
	 * @param index index at which to return document
	 * @return document at given index
	 * @throws IndexOutOfBoundsException if index is out of bounds
	 */
	SingleDocumentModel getDocument(int index);
}
