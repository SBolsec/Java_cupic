package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Exception which is thrown when there is a problem in the lexer for "smart script"
 * @author sbolsec
 *
 */
public class SmartScriptLexerException extends RuntimeException {
	
	/** Generated serial version UID **/
	private static final long serialVersionUID = 6295420773039356932L;

	/**
     * Default constructor
     */
	public SmartScriptLexerException() {
		super();
	}
	
	/**
     * Constructor which takes error message
     * @param message error message
     */
	public SmartScriptLexerException(String message) {
		super(message);
	}
}
