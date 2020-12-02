package hr.fer.oprpp1.java.hw05.shell.lexer;

/**
 * Enumeration of lexer token types
 * @author sbolsec
 *
 */
public enum TokenType {
	/** String of text **/
	STRING,
	/** Text in qoutes **/
	QUOTE,
	/** Found opening quote **/
	QUOTE_START,
	/** End **/
	EOF
}
