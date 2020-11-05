package hr.fer.oprpp1.hw04.db;

/**
 * Exception which is thrown when there is a problem in the query parser
 * @author sbolsec
 *
 */
public class QueryParserException extends RuntimeException {
	private static final long serialVersionUID = 110L;
	
	/**
	 * Default constructor.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Constructor which takes a error message.
	 * @param message
	 */
	public QueryParserException(String message) {
		super(message);
	}
}
