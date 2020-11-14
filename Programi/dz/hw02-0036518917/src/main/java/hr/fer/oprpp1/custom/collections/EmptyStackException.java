package hr.fer.oprpp1.custom.collections;

/**
 * Exception which is thrown when the stack is empty.
 * @author sbolsec
 *
 */
public class EmptyStackException extends RuntimeException {
	/** Generated serial version UID **/
	private static final long serialVersionUID = -233758984868815594L;

    /**
     * Default constructor
     */
    public EmptyStackException() {
        super();
    }

    /**
     * Constructor which takes error message
     * @param message error message
     */
    public EmptyStackException(String message) {
        super(message);
    }
}
