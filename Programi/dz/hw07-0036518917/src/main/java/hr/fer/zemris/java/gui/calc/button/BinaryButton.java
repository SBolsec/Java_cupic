package hr.fer.zemris.java.gui.calc.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;

public class BinaryButton extends JButton implements ActionListener {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -4026636158989045223L;

	private DoubleBinaryOperator first;
	private DoubleBinaryOperator second;
	private String firstString;
	private String secondString;
	private boolean inverse;
	
	public BinaryButton(DoubleBinaryOperator first, DoubleBinaryOperator second, String firstString, String secondString) {
		super();
		this.first = first;
		this.second = second;
		this.firstString = firstString;
		this.secondString = secondString;
		this.inverse = false;
	}

	public String getName() {
		return inverse ? secondString : firstString;
	}

	public DoubleBinaryOperator getOperator() {
		return inverse ? second : first;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.inverse = !this.inverse;
		this.setText(getName());
		repaint();
	}
}
