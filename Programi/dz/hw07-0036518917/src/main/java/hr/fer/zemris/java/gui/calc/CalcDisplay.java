package hr.fer.zemris.java.gui.calc;

import javax.swing.JLabel;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;

/**
 * JLabel which acts as a display for the calculator.
 * @author sbolsec
 *
 */
public class CalcDisplay extends JLabel implements CalcValueListener {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -4559429533339611890L;

	/**
	 * Changes the text displayed on the display.
	 */
	@Override
	public void valueChanged(CalcModel model) {
		this.setText(model.toString());
		repaint();
	}
}
