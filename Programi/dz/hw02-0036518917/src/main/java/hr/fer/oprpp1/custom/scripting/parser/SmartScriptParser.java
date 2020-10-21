package hr.fer.oprpp1.custom.scripting.parser;


import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;

/**
 * This is a parser for the specified format.
 * 
 * @author sbolsec
 *
 */
public class SmartScriptParser {
	
	/** Document body text **/
	private String docBody;
	/** This will store nodes for tree construction **/
	private ObjectStack tree;
	/** Lexer for "smart script" **/
	private SmartScriptLexer lexer;
	
	/**
	 *  Initializes the parser and starts the parsing process 
	 */
	public SmartScriptParser(String docBody) {
		this.docBody = docBody;
		tree = new ObjectStack();
		DocumentNode docNode = new DocumentNode();
		tree.push(docNode);
		lexer = new SmartScriptLexer(docBody);
		startParsing();
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
		
	}
}
