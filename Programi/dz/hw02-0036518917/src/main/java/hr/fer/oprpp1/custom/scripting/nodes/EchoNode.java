package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;

/**
 * A node representing a command which generates some textual output dynamically.
 *
 * @author sbolsec
 *
 */
public class EchoNode extends Node {
	
	/** Elements of the echo node **/
	private Element[] elements;
	
	/**
	 * Constructor which initializes the elements of the echo node.
	 * @param elements to be added to the echo node
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}
	
	/**
	 * Returns the elements of the echo node.
	 * @return elements of the echo node
	 */
	public Element[] getElements() {
		return elements;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EchoNode)) return false;
		EchoNode other = (EchoNode) obj;
		if (this.elements.length != other.elements.length)
			return false;
		for (int i = 0; i < elements.length; i++) {
			if (!this.elements[i].equals(other.elements[i])) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$= ");
		for (int i = 0; i < elements.length; i++) {
			Element element = (Element) elements[i];
			if (element instanceof ElementString) {
				sb.append(this.generateStringInsideTag(element.asText()));
			} else {
				sb.append(elements[i].asText());
			}
			if (i != elements.length - 1) {
				sb.append(' ');
			}
		}
		sb.append(" $}");
		return sb.toString();
	}
}
