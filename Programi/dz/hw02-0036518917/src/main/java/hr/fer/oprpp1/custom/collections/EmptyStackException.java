package hr.fer.oprpp1.custom.collections;

/**
 * Exception which is thrown when the stack is empty.
 * @author sbolsec
 *
 */
public class EmptyStackException extends RuntimeException {
    private static final long serialVersionUID = 1L;

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
