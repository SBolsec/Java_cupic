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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TextNode)) return false;
		TextNode other = (TextNode) obj;
		return this.text.equals(other.text);
	}
	
	@Override
	public String toString() {
		return getText();
	}
}
