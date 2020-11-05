package hr.fer.oprpp1.hw04.db;

/**
 * Types of tokens in the lexer for thr query.
 * @author sbolsec
 *
 */
public enum QueryTokenType {
	/** Operator **/
	OPERATOR,
	/** String in quotes **/
	STRING_LITERAL,
	/** Name of field of student record **/
	FIELD_NAME,
	/** Connecting two comparisons **/
	AND,
	/** End of string **/
	EOF
}
