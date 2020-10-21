package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits class <code>Element</code> and has a single 
 * read-only String property: symbol
 * 
 * @author sbolsec
 *
 */
public class ElementOperator extends Element {
	/** Read only String property **/
	private String symbol;
	
	/**
	 * Constructor which sets the symbol property
	 * @param name will be assigned to the property symbol
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Returns the symbol.
	 * @return name returns symbol
	 */
	public String asText() {
		return symbol;
	}
}
