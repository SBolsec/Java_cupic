package hr.fer.oprpp1.hw02.prob1;

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
	
	/**
	 * This constructor takes the input text which will be tokenized
	 * @param text text to be tokenized
	 */
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
	 * Sets the state of the lexer to the given state.
	 * @param state state to which the lexer will be switched
	 */
	public void setState(LexerState state) {
		if (state == null)
			throw new NullPointerException("State can not be null!");
		this.state = state;
	}
	
	/**
	 * Generates the next token.
	 * @throws LexerException if there was na error while generating next token
	 */
	private void extractNextToken() {
		// If the end was already determined, the another call returns an exception
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No tokens available.");
        }
        
        // Else skip whitespace:
        skipWhitespace();
        
        // If there is no more characters, generate token that signifies the end (EOF)
        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return;
        }
        
        // If the lexer is in the extended state
        if (this.state == LexerState.EXTENDED) {
        	if (data[currentIndex] == Character.valueOf('#')) {
        		currentIndex++;
        		token = new Token(TokenType.SYMBOL, Character.valueOf('#'));
        		return;
        	}
        	int startIndex = currentIndex;
        	while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex]) && data[currentIndex] != '#') {
        		currentIndex++;
        	}
        	String word = String.valueOf(data, startIndex, currentIndex - startIndex);
        	token = new Token(TokenType.WORD, word);
        	return;
        }
        
        // Check if there is a word:
        char c = data[currentIndex];
        if (Character.isLetter(c) || c == '\\' ) {
        	int startIndex = 0;
        	if (c == '\\') {
        		startIndex = currentIndex + 1;
        		escapedCharacter();
        	} else {
        		startIndex = currentIndex++;
        	}
        	
        	while (currentIndex < data.length && (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\')) {
        		if (data[currentIndex] == '\\') {
        			escapedCharacter();
        			continue;
        		}
        		currentIndex++;
        	}
        	
        	String word = createWord(startIndex, currentIndex);
        	token = new Token(TokenType.WORD, word);
        	return;
        }
        
        // Check if there is a number
        if (Character.isDigit(c)) {
        	int startIndex = currentIndex++;
        	while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
        		currentIndex++;
        	}
        	try {
        		Long result = Long.parseLong(String.valueOf(data, startIndex, currentIndex - startIndex));
        		token = new Token(TokenType.NUMBER, result);
        		return;
        	} catch (NumberFormatException ex) {
        		throw new LexerException("Input is wrong!");
        	}
        }
        
        // If it's not a word or a number, then it's a symbol
        token = new Token(TokenType.SYMBOL, Character.valueOf(c));
        currentIndex++;
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
	 * Checks for escaped characters in the input text.
	 * @throws LexerException if the input was wrong
	 */
	private void escapedCharacter() {
		if (currentIndex + 1 >= data.length)
			throw new LexerException("Invalid escape!");
		
		char nextChar = data[++currentIndex];
		if (Character.isDigit(nextChar) || nextChar == '\\') {
			currentIndex++;
		} else {
			throw new LexerException("Invalid escape!");
		}
	}
	
	/**
	 * Creates a word from the input text from the starting index to the end index.
	 * @param startIndex beginning of the word
	 * @param endIndex end of the word
	 * @return word from start index to end index
	 */
	private String createWord(int startIndex, int endIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			char c = data[i];
			if (Character.isLetter(c) || Character.isDigit(c) || (i+1 < data.length && c == '\\' && data[i+1] == '\\')) {
				if (c == '\\') {
					i = i+2;
				}
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
