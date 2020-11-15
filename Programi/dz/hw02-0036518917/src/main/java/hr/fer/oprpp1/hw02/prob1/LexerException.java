package hr.fer.oprpp1.hw02.prob1;

/**
 * Exception which is thrown when there is a problem in the lexer.
 * @author sbolsec
 *
 */
public class LexerException extends RuntimeException {
	
	/** Generated serial version UID **/
	private static final long serialVersionUID = 1609971924115613480L;

	/**
     * Default constructor
     */
	public LexerException() {
		super();
	}
	
	/**
     * Constructor which takes error message
     * @param message error message
     */
	public LexerException(String message) {
		super(message);
	}
}
