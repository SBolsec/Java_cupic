package hr.fer.oprpp1.hw04.db;

/**
 * Exception which is thrown when there is a problem in the query lexer
 * @author sbolsec
 *
 */
public class QueryLexerException extends RuntimeException {
	private static final long serialVersionUID = 50L;
	
	/**
	 * Default constructor.
	 */
	public QueryLexerException() {
		super();
	}
	
	/**
	 * Constructor which takes a error message.
	 * @param message
	 */
	public QueryLexerException(String message) {
		super(message);
	}
}
