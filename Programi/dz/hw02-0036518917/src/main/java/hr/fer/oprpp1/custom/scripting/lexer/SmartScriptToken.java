package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * This class models a lexer for "smart script"
 * @author sbolsec
 *
 */
public class SmartScriptToken {
	/** Token type of this token **/
	private SmartScriptTokenType type;
	/** Value of this token **/
	private Element value;
	
	/**
	 * Constructor which sets the token type and value of this token.
	 * @param type type of the token
	 * @param value value of the token
	 */
	public SmartScriptToken(SmartScriptTokenType type, Element value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of this token.
	 * @return value of this token
	 */
	public Element getValue() {
		return value;
	}

	/**
	 * Returns the token type of this token.
	 * @return token type of this token
	 */
	public SmartScriptTokenType getType() {
		return type;
	}
}
