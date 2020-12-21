package hr.fer.oprpp1.hw08.jnotepadpp.document;

/**
 * Listener that listens to changes in registered <code>SingleDocumentModel</code>.
 * @author sbolsec
 *
 */
public interface SingleDocumentListener {
	/**
	 * This method is called when the modify status of the <code>SingleDocumentModel</code> changed
	 * to which this listener is registered.
	 * @param model document whose modify status updated
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * This method is called when the file path of the <code>SingleDocumentModel</code> changed
	 * to which this listener is registered.
	 * @param model document whose file path updated
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
