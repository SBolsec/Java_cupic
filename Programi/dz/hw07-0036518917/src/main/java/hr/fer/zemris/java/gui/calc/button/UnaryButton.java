package hr.fer.zemris.java.gui.calc.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;

public class UnaryButton extends JButton implements ActionListener {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -3620935050355208730L;

	private DoubleUnaryOperator first;
	private DoubleUnaryOperator second;
	private String firstString;
	private String secondString;
	private boolean inverse;
	
	public UnaryButton(DoubleUnaryOperator first, DoubleUnaryOperator second, String firstString, String secondString) {
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

	public DoubleUnaryOperator getOperator() {
		return inverse ? second : first;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.inverse = !this.inverse;
		this.setText(getName());
		repaint();
	}
}
