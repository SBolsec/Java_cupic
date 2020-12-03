package hr.fer.oprpp1.hw05.shell.lexer;

/**
 * This is a lexical analyzer.
 * @author sbolsec
 *
 */
public class Lexer {
	/** input text **/
	private char[] data;
	/** current token **/
	private Token token;
	/** index of the first unprocessed character **/
	private int currentIndex;
	/** State in which the lexer operates **/
	private LexerState state;
	
	public Lexer(String text) {
		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
	}
	
	/**
	 * Generates and returns the next token.
	 * @return next token
	 * @throws LexerException if there was an error while generating next token
	 */
	public Token nextToken() {
		extractNextToken();
		return getToken();
	}
	
	/**
	 * Returns the last generated token. 
	 * Can be called multiple times; it does not start generating the next token.
	 * @return current token
	 */
	public Token getToken() {
		return this.token;
	}
	
	/**
	 * Sets the lexer state
	 * @param state state to be set
	 */
	public void setState(LexerState state) {
		this.state = state;
	}
	
	/**
	 * Generates the next token.
	 * @throws SmartScriptLexerException if there was an error while generating next token
	 */
	private void extractNextToken() {
		// If the end was already determined, the another call returns an exception
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No tokens available.");
        }
        
        skipWhitespace();
        
        // If there is no more characters, generate token that signifies the end (EOF)
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return;
        }
        
        // If lexer is in basic state (outside of quotes)
        if (state == LexerState.BASIC) {
        	extractNextTokenBasic();
        	return;
        } 
        // Else lexer is in quote state
        extractNextTokenQuote();
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
	 * Generates the next token in lexer basic state
	 */
	private void extractNextTokenBasic() {
		int start = currentIndex;
		while (currentIndex < data.length) {
			if (Character.isWhitespace(data[currentIndex])) {
				String res = String.copyValueOf(data, start, currentIndex - start);
				token = new Token(TokenType.STRING, res);
				return;
			} else if (data[currentIndex] == '\"') {
				if (start == currentIndex) {
					token = new Token(TokenType.QUOTE_START, null);
					return;
				}
				String res = String.copyValueOf(data, start, currentIndex - start);
				token = new Token(TokenType.STRING, res);
				return;
			}
			currentIndex++;
		}
		if (currentIndex >= data.length) {
			String res = String.copyValueOf(data, start, currentIndex - start);
			token = new Token(TokenType.STRING, res);
		}
	}
	
	/**
	 * Generates the next token in lexer quote state
	 * @throws LexerException if there was an error while generating next token
	 */
	private void extractNextTokenQuote() {
		// Check for string inside tag
        if (data[currentIndex] == Character.valueOf('"')) {
        	int startIndex = ++currentIndex;
        	while (currentIndex < data.length) {
        		if (data[currentIndex] == Character.valueOf('\\')) {
        			escapedCharacterInsideQuotes();
        			continue;
        		} else if (data[currentIndex] == Character.valueOf('"')) {
        			break;
        		}
        		currentIndex++;
        	}
        	if (currentIndex >= data.length || data[currentIndex] != Character.valueOf('"')) {
        		throw new LexerException("String was never closed!");
        	}
        	if (currentIndex == data.length - 1 && data[currentIndex] != '\"')
        	if (currentIndex < data.length && data[currentIndex+1] != ' ') {
        		throw new LexerException("After the qutes text there must be either nothing or at least one space!");
        	}
        	
        	String text = createTextInsideTag(startIndex, currentIndex - 1);
        	token = new Token(TokenType.QUOTE, text);
        	currentIndex++;
        	return;
        }
	}
	
	/**
	 * Checks for escaped characters in the input text.
	 * @throws SmartScriptLexerException if the input was wrong
	 */
	private void escapedCharacterInsideQuotes() {
		if (currentIndex + 1 < data.length) {
			char c = data[currentIndex + 1];
			if (c == Character.valueOf('\\') || c == Character.valueOf('"')) {
				currentIndex += 2;
				return;
			}
		}
		throw new LexerException("Illegal escape!");
	}
	
	/**
	 * Returns the escaped characters following the rules inside the quotes
	 * @param startIndex starting index of the text
	 * @param endIndex end index of the text
	 * @return text with escaped characters
	 */
	private String createTextInsideTag(int startIndex, int endIndex) {
		StringBuilder sb = new StringBuilder();
		loop:
		for (int i = startIndex; i <= endIndex; i++) {
			char c = data[i];
			if (c == '\\') {
				c = data[++i];
				switch (c) {
					case '\\': sb.append('\\'); continue loop;
					case '\"': sb.append('\"'); continue loop;
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
