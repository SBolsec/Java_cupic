package hr.fer.oprpp1.hw08.jnotepadpp.document;

/**
 * Listener that listens to changes in registered <code>MultipleDocumentModel</code>.
 * @author sbolsec
 *
 */
public interface MultipleDocumentListener {
	/**
	 * This method is called when the current document changed.
	 * @param previousModel previous document
	 * @param currentModel current document
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	/**
	 * This method is called when a document is added to the <code>MultipleDocumentModel</code>
	 * that this listener is registered to.
	 * @param model document that was added
	 */
	void documentAdded(SingleDocumentModel model);
	
	/**
	 * This method is called when a document is added to the <code>MultipleDocumentModel</code>
	 * that this listener is registered to.
	 * @param model document that was removed
	 */
	void documentRemoved(SingleDocumentModel model);
}
