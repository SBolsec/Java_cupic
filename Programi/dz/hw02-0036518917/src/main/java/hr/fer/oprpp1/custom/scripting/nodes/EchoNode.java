package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

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
	
//	@Override
//	public boolean equals(Object obj) {
//		
//	}
//	
//	@Override
//	public String toString() {
//		
//	}
}
