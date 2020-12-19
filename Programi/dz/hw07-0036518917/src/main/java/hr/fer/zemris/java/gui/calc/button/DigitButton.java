package hr.fer.zemris.java.gui.calc.button;

import javax.swing.JButton;

/**
 * Button that adds a digit to the current value
 * @author sbolsec
 *
 */
public class DigitButton extends JButton {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -6268997809045536018L;
	
	/** Digit that this button represents **/
	private int digit;
	
	/**
	 * Constructor which sets the digit.
	 * @param digit digit to be set
	 */
	public DigitButton(int digit) {
		this.digit = digit;
	}
	
	/**
	 * Returns the digit of this button.
	 * @return digit
	 */
	public int getDigit() {
		return digit;
	}
}
