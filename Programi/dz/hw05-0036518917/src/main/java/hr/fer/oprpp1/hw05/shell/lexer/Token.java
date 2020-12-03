package hr.fer.oprpp1.hw05.shell.lexer;

/**
 * Token that the lexer produces
 * @author sbolsec
 *
 */
public class Token {
	/** Type of the token **/
	private TokenType type;
	/** Value of the token **/
	private String value;
	
	/**
	 * Constructor which initializes the values
	 * @param type
	 * @param value
	 */
	public Token(TokenType type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns token type of this token
	 * @return token type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns value of this token
	 * @return token value
	 */
	public String getValue() {
		return value;
	}
}
