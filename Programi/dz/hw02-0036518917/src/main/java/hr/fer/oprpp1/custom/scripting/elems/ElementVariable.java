package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits class <code>Element</code> and has a single 
 * read-only String property: name
 * @author sbolsec
 *
 */
public class ElementVariable extends Element {
	/** Read only String property **/
	private String name;
	
	/**
	 * Constructor which sets the name property
	 * @param name will be asigned to the property name
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name.
	 * @return name returns name
	 */
	public String asText() {
		return name;
	}
}
