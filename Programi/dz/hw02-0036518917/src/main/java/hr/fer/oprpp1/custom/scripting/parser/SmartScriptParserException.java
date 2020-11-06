package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Exception which is thrown when there is a problem in the parser.
 * 
 * @author sbolsec
 *
 */
public class SmartScriptParserException extends RuntimeException {
	private static final long serialVersionUID = 10L;
	
	/**
     * Default constructor
     */
	public SmartScriptParserException() {
		super();
	}
	
	/**
     * Constructor which takes error message
     * @param message error message
     */
	public SmartScriptParserException(String message) {
		super(message);
	}
}
