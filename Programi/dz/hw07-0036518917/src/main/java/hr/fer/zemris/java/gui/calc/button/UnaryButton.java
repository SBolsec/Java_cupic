package hr.fer.zemris.java.gui.calc.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;

/**
 * Button which models a unary operation which can perform two different
 * unary operations based on its state.
 * @author sbolsec
 *
 */
public class UnaryButton extends JButton implements ActionListener {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -3620935050355208730L;

	/** Operation to be performed in normal state **/
	private DoubleUnaryOperator first;
	/** Operation to be performed in inverted state **/
	private DoubleUnaryOperator second;
	/** Text to be displayed on button in normal state **/
	private String firstString;
	/** Text to be displayed on button in normal state **/
	private String secondString;
	/** State of the button, true == normal state, false == inverted state **/
	private boolean inverse;
	
	/**
	 * Constructor which initializes all the parameters.
	 * @param first operation to be performed in normal state
	 * @param second operation to be performed in inverted state
	 * @param firstString text do be displayed in normal state
	 * @param secondString text to be displayed in inverted state
	 */
	public UnaryButton(DoubleUnaryOperator first, DoubleUnaryOperator second, String firstString, String secondString) {
		super();
		this.first = first;
		this.second = second;
		this.firstString = firstString;
		this.secondString = secondString;
		this.inverse = false;
	}

	/**
	 * Returns text to be displayed on button based on the state of the button.
	 * @return text to be displayed on button based on state.
	 */
	public String getName() {
		return inverse ? secondString : firstString;
	}

	/**
	 * Returns the operation to be performed based on the state of the button.
	 * @return operation to be performed based on state
	 */
	public DoubleUnaryOperator getOperator() {
		return inverse ? second : first;
	}

	/**
	 * Changes the state of the button and displayed text.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.inverse = !this.inverse;
		this.setText(getName());
		repaint();
	}
}
