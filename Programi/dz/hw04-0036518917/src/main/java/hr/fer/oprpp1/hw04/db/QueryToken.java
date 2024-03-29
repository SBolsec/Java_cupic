package hr.fer.oprpp1.hw04.db;

/**
 * Token generated by lexer from the query.
 * @author sbolsec
 *
 */
public class QueryToken {
	
	/** Value of the token **/
	private String value;
	/** Type of the token **/
	private QueryTokenType type;
	
	/**
	 * Constructor which sets the value and type of the token
	 * @param value value to be set
	 * @param type type of the token to be set
	 */
	public QueryToken(QueryTokenType type, String value) {
		super();
		this.value = value;
		this.type = type;
	}

	/**
	 * Returns the value of the token.
	 * @return value of the token
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the type of the token.
	 * @return type of the token
	 */
	public QueryTokenType getType() {
		return type;
	}
}
