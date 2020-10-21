package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Enumeration of all types of tokens present in "smart script"
 * 
 * @author sbolsec
 *
 */
public enum SmartScriptTokenType {
	/** Opened tag **/
	TAG_OPENED,
	/** Closed tag **/
	TAG_CLOSED,
	/** Variable name **/
	VARIABLE,
	/** Integer **/
	INTEGER,
	/** Double **/
	DOUBLE,
	/** String **/
	STRING,
	/** Function **/
	FUNCTION,
	/** Operator **/
	OPERATOR,
	/** End of text **/
	EOF
}
