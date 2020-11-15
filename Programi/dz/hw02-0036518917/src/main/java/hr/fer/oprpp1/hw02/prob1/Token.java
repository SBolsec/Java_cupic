package hr.fer.oprpp1.hw02.prob1;

/**
 * This class models a token from the input text.
 * @author sbolsec
 *
 */
public class Token {
	
	/** Token type of this token **/
	private TokenType type;
	/** Value of this token **/
	private Object value;
	
	/**
	 * Constructor which sets the token type and value of this token.
	 * @param type type of the token
	 * @param value value of the token
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of this token.
	 * @return value of this token
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns the token type of this token.
	 * @return token type of this token
	 */
	public TokenType getType() {
		return type;
	}
}
