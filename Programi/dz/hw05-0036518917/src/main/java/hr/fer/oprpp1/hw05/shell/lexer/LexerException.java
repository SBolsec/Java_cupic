package hr.fer.oprpp1.hw05.shell.lexer;

/**
 * Exception which is thrown when there is a problem in the lexer for "smart script"
 * @author sbolsec
 *
 */
public class LexerException extends RuntimeException {

	/** Generated serial version UID **/
	private static final long serialVersionUID = 8348952961592693146L;

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
