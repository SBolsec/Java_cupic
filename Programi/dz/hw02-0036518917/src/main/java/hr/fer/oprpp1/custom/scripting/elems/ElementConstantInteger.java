package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits class <code>Element</code> and has a single 
 * read-only String property: int
 * 
 * @author sbolsec
 *
 */
public class ElementConstantInteger extends Element {
	/** Read only int property **/
	private int value;
	
	/**
	 * Constructor which sets the value property
	 * @param name will be assigned to the property value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	
	/**
	 * Returns the value.
	 * @return name returns value
	 */
	public String asText() {
		return Integer.toString(value);
	}
}
