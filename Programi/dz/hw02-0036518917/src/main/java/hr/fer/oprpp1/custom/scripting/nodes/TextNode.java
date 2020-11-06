package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A node representing a piece of textual data.
 * 
 * @author sbolsec
 *
 */
public class TextNode extends Node {
	/** Text of the text node **/
	private String text;
	
	/**
	 * Constructor which initializes the text.
	 * @param text text of the text node
	 */
	public TextNode(String text) {
		this.text = text;
	}
	
	/**
	 * Returns the text.
	 * @return text of text node
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Two text nodes are equal if they store the same text.
	 * @param obj object to be tested
	 * @return true if text nodes are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TextNode)) return false;
		TextNode other = (TextNode) obj;
		return this.text.equals(other.text);
	}
	
	/**
	 * Return text this nodes stores.
	 * @return stored text
	 */
	@Override
	public String toString() {
		return generateStringWithEscapedCharacters(getText());
	}
	
	/**
	 * Generates the escaping characters so that the text follows the escaping rules outside the tag
	 * @param input text to be examined
	 * @return generated text
	 */
	private String generateStringWithEscapedCharacters(String input) {
		StringBuilder sb = new StringBuilder();
		char[] elements = input.toCharArray();
		for (int i = 0, n = elements.length; i < n; i++) {
			char c = elements[i];
			if ((c == '{' && i+1 < n && elements[i+1] == '$') || c == '\\') {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb.toString();
	}
}
