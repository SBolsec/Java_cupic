package hr.fer.oprpp1.hw05.shell.lexer;

/**
 * Enumeration of all the states of lexer
 * @author sbolsec
 *
 */
public enum LexerState {
	/** Basic, normal state **/
	BASIC,
	/** While reading quoted text **/
	QUOTES
}
