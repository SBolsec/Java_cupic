package hr.fer.zemris.java.gui.layouts;

/**
 * Exception in the calc layout.
 * @author sbolsec
 *
 */
public class CalcLayoutException extends RuntimeException {

	/** Generated serial version UID **/
	private static final long serialVersionUID = 8465025981115533808L;

	/**
	 * Default constructor.
	 */
	public CalcLayoutException() {
	}

	/**
	 * Constructor which takes message.
	 * @param message message that describes exception
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
	
}
