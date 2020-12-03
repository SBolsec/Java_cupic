package hr.fer.oprpp1.hw05.shell;

/**
 * Exception for when there is an input/output
 * exception in the shell
 * @author sbolsec
 *
 */
public class ShellIOException extends RuntimeException {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -223772554029292629L;

	/**
	 * Default constructor
	 */
	ShellIOException() {
		super();
	}
	
	/**
	 * Constructor which takes message
	 * @param message
	 */
	ShellIOException(String message) {
		super(message);
	}
}
