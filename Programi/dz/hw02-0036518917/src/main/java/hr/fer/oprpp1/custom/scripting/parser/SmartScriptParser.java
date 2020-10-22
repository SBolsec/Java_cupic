package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerException;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptToken;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;

/**
 * This is a parser for the specified format.
 * 
 * @author sbolsec
 *
 */
public class SmartScriptParser {
	
	/** This will store nodes for tree construction **/
	private ObjectStack tree;
	/** Lexer for "smart script" **/
	private SmartScriptLexer lexer;
	/** Holds all tokens that the lexer generates **/
	private List tokens;
	
	/**
	 *  Initializes the parser and starts the parsing process 
	 */
	public SmartScriptParser(String docBody) {
		try {
			tree = new ObjectStack();
			DocumentNode docNode = new DocumentNode();
			tree.push(docNode);
			lexer = new SmartScriptLexer(docBody);
			tokens = new ArrayIndexedCollection();
			startParsing();
		} catch (SmartScriptLexerException e) {
			throw new SmartScriptParserException(e.getMessage());
		}
	}
	
	/**
	 * Returns the document node which is the start of the generated tree
	 * @return
	 */
	public DocumentNode getDocumentNode() {
		if (tree.size() != 1)
			throw new SmartScriptParserException("There were more nodes on the stack than expected, was expecting 1, there were: " + tree.size() + ".");
		return (DocumentNode) tree.pop();
	}
	
	/**
	 * Method which does the actual parsing
	 */
	private void startParsing() {
		// Generate all the tokens
		generateTokens();
		
		for (int i = 0, n = tokens.size(); i < n; i++) {
			SmartScriptToken token = (SmartScriptToken) tokens.get(i);
			SmartScriptTokenType type = token.getType();
			Node lastNodeOnStack = (Node) tree.peek();
			
			if (type == SmartScriptTokenType.STRING) {
				Node node = new TextNode(token.getValue().asText());
				lastNodeOnStack.addChildNode(node);
				continue;
			}
			
			if (type == SmartScriptTokenType.TAG_OPENED && i + 1 < n) {
				SmartScriptToken nextToken = (SmartScriptToken) tokens.get(++i);
				if (nextToken.getType() != SmartScriptTokenType.STRING) {
					throw new SmartScriptLexerException("Tag was not FOR, END or =");
				}
				String tagName = nextToken.getValue().asText();
				if (tagName.equals("FOR")) {
					// First element of for-tag must be a variable
					if (i + 1 >= n) throw new SmartScriptParserException();
					SmartScriptToken variable = (SmartScriptToken) tokens.get(++i);
					if (variable.getType() != SmartScriptTokenType.VARIABLE) {
						throw new SmartScriptParserException(variable.getValue().asText() + " is not a variable name!");
					}
					
					// Second element of for-tag must be a variable, string or number
					if (i + 1 >= n) throw new SmartScriptParserException();
					SmartScriptToken start = (SmartScriptToken) tokens.get(++i);
					if (start.getType() != SmartScriptTokenType.VARIABLE
							&& start.getType() != SmartScriptTokenType.STRING
							&& start.getType() != SmartScriptTokenType.INTEGER
							&& start.getType() != SmartScriptTokenType.DOUBLE) {
						throw new SmartScriptParserException("Wrong element in for-tag as start! Allowed elements are variables, numbers and string!");
					}
					
					// Third element of for-tag must be a variable, string or number
					if (i + 1 >= n) throw new SmartScriptParserException();
					SmartScriptToken end = (SmartScriptToken) tokens.get(++i);
					if (end.getType() != SmartScriptTokenType.VARIABLE
							&& end.getType() != SmartScriptTokenType.STRING
							&& end.getType() != SmartScriptTokenType.INTEGER
							&& end.getType() != SmartScriptTokenType.DOUBLE) {
						throw new SmartScriptParserException("Wrong element in for-tag as end! Allowed elements are variables, numbers and string!");
					}
					
					// For-tag can have a optional fourth element which must be a variable, string or number; or it ends at three elements
					if (i + 1 >= n) throw new SmartScriptParserException();
					SmartScriptToken stepOrTagEnd = (SmartScriptToken) tokens.get(++i);
					if (stepOrTagEnd.getType() == SmartScriptTokenType.TAG_CLOSED) {
						Node node = new ForLoopNode((ElementVariable) variable.getValue(), start.getValue(), end.getValue(), null);
						lastNodeOnStack.addChildNode(node);
						tree.push(node);
						continue;
					}
					if (stepOrTagEnd.getType() != SmartScriptTokenType.VARIABLE
							&& stepOrTagEnd.getType() != SmartScriptTokenType.STRING
							&& stepOrTagEnd.getType() != SmartScriptTokenType.INTEGER
							&& stepOrTagEnd.getType() != SmartScriptTokenType.DOUBLE) {
						throw new SmartScriptParserException("Wrong element in for-tag as step! Allowed elements are variables, numbers and string!");
					}
					
					// If there was four elements, the next token must be a closing tag
					if (i + 1 >= n) throw new SmartScriptParserException();
					SmartScriptToken closingTag = (SmartScriptToken) tokens.get(++i);
					if (closingTag.getType() != SmartScriptTokenType.TAG_CLOSED) {
						throw new SmartScriptParserException("For-tag was not closed!");
					}
					Node node = new ForLoopNode((ElementVariable) variable.getValue(), start.getValue(), end.getValue(), stepOrTagEnd.getValue());
					lastNodeOnStack.addChildNode(node);
					tree.push(node);
					continue;
				} else if (tagName.equals("EQUALS")) {
					SmartScriptToken newToken = null;
					List echoElements = new ArrayIndexedCollection();
					while (i + 1 < n && (newToken = (SmartScriptToken) tokens.get(++i)).getType() != SmartScriptTokenType.TAG_CLOSED && newToken.getType() != SmartScriptTokenType.EOF) {
						echoElements.add(newToken.getValue());
					}
					if (newToken == null) {
						Node node = new EchoNode(null);
						lastNodeOnStack.addChildNode(node);
						continue;
					} else if (newToken.getType() == SmartScriptTokenType.EOF) {
						throw new SmartScriptParserException("Echo tag was never closed!");
					} else if (newToken.getType() == SmartScriptTokenType.TAG_CLOSED) {
						Object[] objectArray = echoElements.toArray();
						Element[] elements = new Element[objectArray.length];
						for (int count = 0; count < objectArray.length; count++) {
							elements[count] = (Element) objectArray[count];
						}
						Node node = new EchoNode(elements);
						lastNodeOnStack.addChildNode(node);
						continue;
					} else {
						throw new SmartScriptParserException("Echo tag was invalid!");
					}
				} else if (tagName.equals("END")) {
					SmartScriptToken closingTag = (SmartScriptToken) tokens.get(++i);
					if (closingTag.getType() != SmartScriptTokenType.TAG_CLOSED) {
						throw new SmartScriptParserException("End tag was not valid");
					}
					tree.pop();
					continue;
				} else {
					throw new SmartScriptParserException("Tag name not valid!");
				}
			} else {
				throw new SmartScriptParserException("Tag was opened but there was nothing after it.");
			}
		}
	}
	
	/**
	 * Uses the SmartScriptLexer to generate tokens
	 */
	private void generateTokens() {
		try {
			SmartScriptToken token;
			while ((token = lexer.nextToken()) != null && token.getType() != SmartScriptTokenType.EOF) {
				tokens.add(token);
				if (token.getType() == SmartScriptTokenType.TAG_OPENED) {
					lexer.setState(SmartScriptLexerState.TAG);
				}
				if (token.getType() == SmartScriptTokenType.TAG_CLOSED) {
					lexer.setState(SmartScriptLexerState.BASIC);
				}
			}
		} catch (SmartScriptLexerException e) {
			throw new SmartScriptParserException("There was an error while generating the tokens with the lexer.");
		}
	}
}
