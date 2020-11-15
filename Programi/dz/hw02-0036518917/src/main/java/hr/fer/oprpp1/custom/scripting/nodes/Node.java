package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all graph nodes.
 * @author sbolsec
 *
 */
public abstract class Node {
	
	/** Stores the children of this node **/
	protected ArrayIndexedCollection children;
	
	/**
	 * Adds given child to an internally managed collection of children
	 * @param child node to be added as a child to this node
	 */
	public void addChildNode(Node child) {
		if (children == null)
			children = new ArrayIndexedCollection();
		children.add(child);
	}
	
	/**
	 * Returns the number of (direct) children
	 * @return number of children of this node
	 */
	public int numberOfChildren() {
		if (this.children == null)
			return 0;
		return this.children.size();
	}
	
	/**
	 * Returns selected child or throws IndexOutOfBoundsException if the index is invalid
	 * @param index which child to return
	 * @return child at specified index
	 * @throws IndexOutOfBoundsException if index is less than 0 or greater than the number of children
	 */
	public Node getChild(int index) {
		return (Node) children.get(index);
	}
	
	/**
	 * Generates the escaping characters so that the text follows the escaping rules inside the tag
	 * @param input text to be examined
	 * @return generated text
	 */
	protected String generateStringInsideTag(String input) {
		StringBuilder sb = new StringBuilder();
		char[] elements = input.toCharArray();
		sb.append('"');
		for (int i = 0, n = elements.length; i < n; i++) {
			char c = elements[i];
			if (c == '\\' || c == '"') {
				sb.append('\\');
			}
			sb.append(c);
		}
		sb.append('"');
		return sb.toString();
	}
}
