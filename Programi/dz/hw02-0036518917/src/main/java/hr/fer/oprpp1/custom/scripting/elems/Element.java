package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Base elements class
 * 
 * @author sbolsec
 *
 */
public abstract class Element {
	/**
	 * In this class it only returns an empty string
	 * @return empty string
	 */
	public String asText() {
		return "";
	}
	
	/**
	 * Tests whether the given object equals this one.
	 * Returns true if this.asText() equals other.asText()
	 * @param object to be tested
	 * @return true if this.asText() equals other.asText()
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Element)) return false;
		Element other = (Element) obj;
		return this.asText().equals(other.asText());
	}
	
	/**
	 * Returns value of this element
	 * @return value of this element
	 */
	@Override 
	public String toString() {
		return this.asText();
	}
}
