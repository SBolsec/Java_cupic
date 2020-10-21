package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Enumeration of all states of the lexer for "smart script"
 * @author sbolsec
 *
 */
public enum SmartScriptLexerState {
	/** Outside of tag **/
	BASIC,
	/** Inside of tag **/
	TAG
}
