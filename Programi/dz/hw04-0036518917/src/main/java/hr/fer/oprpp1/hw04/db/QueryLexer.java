package hr.fer.oprpp1.hw04.db;

/**
 * Tokenizes the queries.
 * @author sbolsec
 *
 */
public class QueryLexer {

	/** Input text **/
	private char[] data;
	/** Current token **/
	private QueryToken token;
	/** Index of the first unprocessed character **/
	private int currentIndex;
	
	/**
	 * This constructor takes the input text which will be tokenized
	 * @param text text to be tokenized
	 */
	public QueryLexer(String text) {
		data = text.toCharArray();
		token = null;
		currentIndex = 0;
	}
	
	/**
	 * Returns the last generated token. 
	 * Can be called multiple times; it does not start generating the next token.
	 * @return current token
	 */
	public QueryToken getToken() {
		return this.token;
	}
	
	/**
	 * Generates and returns the next token.
	 * @return next token
	 * @throws QueryLexerException if there was an error while generating next token
	 */
	public QueryToken nextToken() {
		extractNextToken();
		return getToken();
	}
	
	/**
	 * Generates the next token.
	 * @throws QueryLexerException if there was an error while generating next token
	 */
	private void extractNextToken() {
		// If the end was already determined, the another call returns an exception
        if (token != null && token.getType() == QueryTokenType.EOF) {
            throw new QueryLexerException("No tokens available.");
        }
        
        // Else skip whitespace
        skipWhitespace();
        
        // If there is no more characters, generate token that signifies the end (EOF)
        if (currentIndex >= data.length) {
            token = new QueryToken(QueryTokenType.EOF, null);
            return;
        }
        
        char c = data[currentIndex];
        
        // If there is less/greater than, less/greater or equal operator  
        if (c == '<' || c == '>') {
        	if (currentIndex + 1 < data.length) {
        		if (data[currentIndex + 1] == '=') {
        			currentIndex += 2;
        			token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(data, currentIndex - 2, 2));
        			return;
        		}
        	}
        	token = new QueryToken(QueryTokenType.OPERATOR, String.valueOf(data, currentIndex, 1));
			currentIndex++;
        	return;
        }
        
        // If there is equals operator
        if (c == '=') {
        	token = new QueryToken(QueryTokenType.OPERATOR, "=");
        	currentIndex++;
        	return;
        }
        
        // If there is not equals operator
        if (c == '!') {
        	if (currentIndex + 1 >= data.length)
        		throw new QueryLexerException("Not equals operator was missing equals sign!");
        	if (data[++currentIndex] != '=')
        		throw new QueryLexerException("Not equals operator was missing equals sign!");
        	token = new QueryToken(QueryTokenType.OPERATOR, "!=");
        	currentIndex++;
        	return;
        }
        
        // If there is a string literal
        if (c == '"') {
        	int start = currentIndex + 1;
        	while (data[++currentIndex] != '"');
        	if (currentIndex == start) {
        		currentIndex++;
        		token = new QueryToken(QueryTokenType.STRING_LITERAL, "");
        		return;
        	}
        	token = new QueryToken(QueryTokenType.STRING_LITERAL, String.valueOf(data, start, currentIndex - start));
        	currentIndex++;
        	return;
        }
        
        // Else there is a field name or 'and' or 'LIKE'
        int start = currentIndex;
        currentIndex++;
        while (currentIndex < data.length && data[currentIndex] != '<' && data[currentIndex] != '>' && data[currentIndex] != '=' &&
        		data[currentIndex] != '"' && data[currentIndex] != '!' && !Character.isWhitespace(data[currentIndex])) {
        	currentIndex++;
        }
        String name = String.valueOf(data, start, currentIndex - start);
        QueryTokenType type = getType(name);
        token = new QueryToken(type, name);
	}
	
	/**
	 * Skips all whitespace characters.
	 */
	private void skipWhitespace() {
		while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}
	
	/**
	 * Determines the token type of the input.
	 * @param text input from which to determine token type
	 * @return token type of input
	 * @throws QueryLexerException if input was invalid
	 */
	private QueryTokenType getType(String text) {
		switch(text.trim()) {
		case "jmbag":
		case "firstName":
		case "lastName": return QueryTokenType.FIELD_NAME;
		case "LIKE": return QueryTokenType.OPERATOR;
		}
		if (text.trim().toLowerCase().equals("and"))
			return QueryTokenType.AND;
		
		throw new QueryLexerException("Input not supported! It was: " + text + ".");
	}
}
