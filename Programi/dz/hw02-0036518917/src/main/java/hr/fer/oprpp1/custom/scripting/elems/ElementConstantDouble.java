package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits class <code>Element</code> and has a single 
 * read-only String property: double
 * 
 * @author sbolsec
 *
 */
public class ElementConstantDouble extends Element {
	/** Read only double property **/
	private double value;
	
	/**
	 * Constructor which sets the value property
	 * @param name will be assigned to the property value
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}
	
	/**
	 * Returns the value.
	 * @return name returns value
	 */
	public String asText() {
		return Double.toString(value);
	}
}
