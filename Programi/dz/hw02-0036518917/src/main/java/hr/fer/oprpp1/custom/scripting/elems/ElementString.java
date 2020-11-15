package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits class <code>Element</code> and has a single 
 * read-only String property: String
 * @author sbolsec
 *
 */
public class ElementString extends Element {
	/** Read only String property **/
	private String value;
	
	/**
	 * Constructor which sets the value property
	 * @param value will be assigned to the property value
	 */
	public ElementString(String value) {
		this.value = value;
	}
	
	/**
	 * Returns the value.
	 * @return value returns value
	 */
	public String asText() {
		return value;
	}
}
