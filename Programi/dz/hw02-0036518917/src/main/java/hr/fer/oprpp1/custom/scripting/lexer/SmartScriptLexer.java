package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.hw02.prob1.LexerException;

/**
 * This is a lexical analyzer.
 * @author sbolsec
 *
 */
public class SmartScriptLexer {
	/** input text **/
	private char[] data;
	/** current token **/
	private SmartScriptToken token;
	/** index of the first unprocessed character **/
	private int currentIndex;
	/** State in which the lexer operates **/
	private SmartScriptLexerState state;
	
	/**
	 * This constructor takes the input text which will be tokenized
	 * @param text text to be tokenized
	 */
	public SmartScriptLexer(String text) {
		data = text.toCharArray();
		currentIndex = 0;
		state = SmartScriptLexerState.BASIC;
	}
	
	/**
	 * Generates and returns the next token.
	 * @return next token
	 * @throws LexerException if there was an error while generating next token
	 */
	public SmartScriptToken nextToken() {
		extractNextToken();
		return getToken();
	}
	
	/**
	 * Returns the last generated token. 
	 * Can be called multiple times; it does not start generating the next token.
	 * @return current token
	 */
	public SmartScriptToken getToken() {
		return this.token;
	}
	
	/**
	 * Sets the state of the lexer to the given state.
	 * @param state state to which the lexer will be switched
	 */
	public void setState(SmartScriptLexerState state) {
		if (state == null)
			throw new NullPointerException("State can not be null!");
		this.state = state;
	}
	
	/**
	 * Generates the next token.
	 * @throws SmartScriptLexerException if there was an error while generating next token
	 */
	private void extractNextToken() {
		// If the end was already determined, the another call returns an exception
        if (token != null && token.getType() == SmartScriptTokenType.EOF) {
            throw new SmartScriptLexerException("No tokens available.");
        }
        
        // Else skip whitespace:
        if (state != SmartScriptLexerState.BASIC)
        	skipWhitespace();
        
        // If there is no more characters, generate token that signifies the end (EOF)
        if (currentIndex >= data.length) {
            token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
            return;
        }
        
        // If lexer is in basic state (outside of tags)
        if (state == SmartScriptLexerState.BASIC) {
        	extractNextTokenBasic();
        	return;
        } 
        // Else lexer is in tag state
        extractNextTokenTag();
	}
	
	/**
	 * Generates the next token in lexer basic state
	 */
	private void extractNextTokenBasic() {
		int startIndex = currentIndex;
    	
    	loop:
    	while (currentIndex < data.length) {
    		switch (data[currentIndex]) {
    			case '\\': escapedCharacterOutsideTag(); break;
    			case '{': 
    				if (currentIndex + 1 < data.length && data[currentIndex + 1] == Character.valueOf('$')) {
    					break loop;
    				}
    		}
    		currentIndex++;
    	}
    	String text = createTextOutsideTag(startIndex, currentIndex - 1);
    	
    	// Found the tag opening
    	if (text.length() == 0) {
    		token = new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null);
    		return;
    	}
    	token = new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString(text));
    	return;
	}
	
	/**
	 * Generates the next token in lexer tag state
	 * @throws SmartScriptLexerException if there was an error while generating next token
	 */
	private void extractNextTokenTag() {
        char c = data[currentIndex];
        
        // Check for open tag and tag name
        if (c == Character.valueOf('{') && currentIndex + 1 < data.length && data[currentIndex+1] == Character.valueOf('$')) {
        	try {
        		currentIndex += 2;
        		skipWhitespace();
        		
        		if (data[currentIndex] == Character.valueOf('=')) { 
        			token = new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS"));
        			currentIndex++;
        			return;
        		}
        		
        		if (Character.isLetter(data[currentIndex])) {
        			String name = checkVariableOrFunctionNameInTag().toUpperCase();
        			token = new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString(name));
        			
        			return;
        		}
        		throw new SmartScriptLexerException();
        	} catch (Exception e) {
        		throw new SmartScriptLexerException("Tag was not named properly!");
        	}
        }
        
        // Check for variable name
        if (Character.isLetter(c)) {
        	String name = checkVariableOrFunctionNameInTag();
        	token = new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable(name));
        	
        	return;
        }
        
        // Check for function in tag
        if (c == Character.valueOf('@')) {
        	if (currentIndex + 1 < data.length && Character.isLetter(data[++currentIndex])) {
        		String name = checkVariableOrFunctionNameInTag();
        		token = new SmartScriptToken(SmartScriptTokenType.FUNCTION, new ElementFunction(name));
        		currentIndex++;
        		return;
        	}
        	throw new SmartScriptLexerException("Function name is invalid!");
        }
        
        // Check for number inside tag
        if (Character.isDigit(c) || (c == Character.valueOf('-') && currentIndex + 1 < data.length && Character.isDigit(data[currentIndex + 1]))) {
        	int startIndex = currentIndex++;
        	boolean decimal = false;
        	if (currentIndex + 1 < data.length) {
            	while (currentIndex < data.length && (Character.isDigit(data[currentIndex]) || (!decimal && data[currentIndex] == Character.valueOf('.')))) {
            		if (data[currentIndex] == Character.valueOf('.')) {
            			decimal = true;
            		}
            		currentIndex++;
            	}
        	}
        	
        	try {
        		String text = String.valueOf(data, startIndex, currentIndex - startIndex);
        		if (decimal) {
        			double num = Double.parseDouble(text);
        			token = new SmartScriptToken(SmartScriptTokenType.DOUBLE, new ElementConstantDouble(num));
        		} else {
        			int num = Integer.parseInt(text);
        			token = new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(num));
        		}
        		return;
        	} catch (NumberFormatException e) {
				throw new SmartScriptLexerException("Number was invalid!");
			}
        }
        
        // Check for valid operators
        if (isOperator(c)) {
        	token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, new ElementOperator(Character.toString(c)));
        	currentIndex++;
        	return;
        }
        
        // Check for string inside tag
        if (c == Character.valueOf('"')) {
        	int startIndex = ++currentIndex;
        	while (currentIndex < data.length) {
        		if (data[currentIndex] == Character.valueOf('\\')) {
        			escapedCharacterInsideTag();
        			continue;
        		} else if (data[currentIndex] == Character.valueOf('"')) {
        			break;
        		}
        		currentIndex++;
        	}
        	if (currentIndex >= data.length || data[currentIndex] != Character.valueOf('"')) {
        		throw new SmartScriptLexerException("String was never closed!");
        	}
        	String text = createTextInsideTag(startIndex, currentIndex - 1);
        	token = new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString(text));
        	currentIndex++;
        	return;
        }
        
        // Check for closing tag
        if (c == Character.valueOf('$') && currentIndex + 1 < data.length && data[currentIndex+1] == Character.valueOf('}')) {
        	token = new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null);
        	currentIndex += 2;
        	return;
        }
        
        // If it came to here, it was a mistake
        throw new SmartScriptLexerException("Invalid input!");
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
	private void escapedCharacterOutsideTag() {
		if (currentIndex + 1 < data.length) {
			char c = data[currentIndex + 1];
			if (c == Character.valueOf('\\') || c == Character.valueOf('{')) {
				currentIndex++;
				return;
			}
		}
		throw new SmartScriptLexerException("Illegal escape!");
	}
	
	/**
	 * Creates a string from the input text from the starting index to the end index.
	 * @param startIndex beginning of the string
	 * @param endIndex end of the string
	 * @return word from start index to end index
	 */
	private String createTextOutsideTag(int startIndex, int endIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i <= endIndex; i++) {
			char c = data[i];
			if (c == '\\') {
				c = data[++i];
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * Returns the name of a variable or function inside tag
	 * @return name of variable or function inside tag
	 */
	private String checkVariableOrFunctionNameInTag() {
		int startIndex = currentIndex++;
		while (currentIndex < data.length && 
						(Character.isLetterOrDigit(data[currentIndex]) || 
						Character.valueOf('_') == data[currentIndex])) {
			currentIndex++;
		}
		return String.valueOf(data, startIndex, currentIndex - startIndex);
	}
	
	/**
	 * Checks whether the character is a valid operator.
	 * @param c character to be checked
	 * @return true if passed character is a valid operator, false otherwise
	 */
	public boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
	}
	
	/**
	 * Checks for escaped characters in the input text.
	 * @throws SmartScriptLexerException if the input was wrong
	 */
	private void escapedCharacterInsideTag() {
		if (currentIndex + 1 < data.length) {
			char c = data[currentIndex + 1];
			if (c == Character.valueOf('\\') || c == Character.valueOf('"') || c == Character.valueOf('n') || c == Character.valueOf('r') || c == Character.valueOf('t')) {
				currentIndex += 2;
				return;
			}
		}
		throw new SmartScriptLexerException("Illegal escape!");
	}
	
	/**
	 * Returns the escaped characters following the rules inside the tag
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
					case 'n': sb.append('\n'); continue loop;
					case 'r': sb.append('\r'); continue loop;
					case 't': sb.append('\t'); continue loop;
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
