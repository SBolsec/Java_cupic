package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.button.BinaryButton;
import hr.fer.zemris.java.gui.calc.button.DigitButton;
import hr.fer.zemris.java.gui.calc.button.UnaryButton;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Calculator based on the Windows XP calculator.
 * @author sbolsec
 *
 */
public class Calculator extends JFrame {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -5148492242213114522L;
	
	/** Calculator model which keeps track of all the data **/
	private CalcModel model;
	/** Container in which the visual components are added **/
	private Container cp;
	/** Checkbox which inverses all the unary buttons **/
	private InverseCheckBox cb;
	/** Stack used by push and pop **/
	private final Stack<Double> memory = new Stack<>();
	/** Color of the buttons **/
	private final Color btnColor = new Color(221, 221, 255, 255);

	/**
	 * Constructor.
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Java Calculator v1.0");
		setSize(700,400);
		initGUI();
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		model = new CalcModelImpl();
		cp = getContentPane();
		cp.setLayout(new CalcLayout(10));
		
		cb = new InverseCheckBox();
		cb.setText("Inv");
		cb.addItemListener(l -> cb.informListeners());
		cp.add(cb, new RCPosition(5, 7));

		initDisplay();
		initDigitButtons();
		initUnaryButtons();
		initBinaryButtons();
		initOtherButtons();
	}
	
	/**
	 * Initializes the display.
	 */
	private void initDisplay() {
		CalcDisplay display = new CalcDisplay();
		display.setText("0");
		display.setBackground(Color.YELLOW);
		display.setOpaque(true);
		display.setFont(display.getFont().deriveFont(30f));
		display.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		model.addCalcValueListener(display);
		cp.add(display, new RCPosition(1, 1));
	}
	
	/**
	 * Initializes the digit buttons.
	 */
	private void initDigitButtons() {
		for (int i = 0; i < 10; i++) {
			DigitButton btn = new DigitButton(i);
			btn.setBackground(btnColor);
			btn.setText(Integer.toString(i));
			btn.setFont(btn.getFont().deriveFont(30f));
			
			int r,c;
			if (i == 0) {
				r = 5;
				c = 3;
			} else if (i < 4) {
				r = 4;
				c = i + 2;
			} else if (i < 7) {
				r = 3;
				c = i - 1;
			} else {
				r = 2;
				c = i - 4;
			}
			
			btn.addActionListener(l -> {
				try {
					model.insertDigit(btn.getDigit());
				} catch (Exception ignored) {
					
				}
			});
			
			cp.add(btn, new RCPosition(r, c));
		}
	}
	
	/**
	 * Initializes the unary buttons.
	 */
	private void initUnaryButtons() {
		String[][] btnNames = new String[][] {
			{"sin", "arcsin"},
			{"cos", "arccos"},
			{"tan", "arctan"},
			{"ctg", "arcctg"},
			{"1/x", "1/x"},
			{"log", "10^x"},
			{"ln", "e^x"}
		};
		
		DoubleUnaryOperator[][] operators = new DoubleUnaryOperator[][] {
			{Math::sin, Math::asin},
			{Math::cos, Math::acos},
			{Math::tan, Math::atan},
			{x -> Math.tan(1/x), x -> Math.atan(1/x)},
			{x -> 1/x, x -> 1/x},
			{Math::log10, x -> Math.pow(10, x)},
			{Math::log, x -> Math.pow(Math.E, x)},
		};
		
		for (int i = 0; i < 7; i++) {
			int r, c;
			if (i < 4) {
				c = 2;
				r = i + 2;
			} else {
				c = 1;
				r = i - 2;
			}
			
			UnaryButton btn = new UnaryButton(operators[i][0], operators[i][1], btnNames[i][0], btnNames[i][1]);
			btn.setBackground(btnColor);
			btn.setText(btnNames[i][0]);
			btn.addActionListener(l -> {
				if (model.isActiveOperandSet() && model.getPendingBinaryOperation() == null) {
					double res = btn.getOperator().applyAsDouble(model.getActiveOperand());
					model.setActiveOperand(res);
					model.clear();

					String s = Double.toString(res);
					if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
					model.freezeValue(s);
					return;
				}

				double res = btn.getOperator().applyAsDouble(model.getValue());
				model.freezeValue(null);
				model.setValue(res);
			});
			cb.addActionListener(btn);
			cp.add(btn, new RCPosition(r, c));
		}
	}
	
	/**
	 * Initializes the binary buttons.
	 */
	private void initBinaryButtons() {
		String[] btnNames = new String[] {
				"/", "*", "-", "+" 
		};
		
		DoubleBinaryOperator[] operators = new DoubleBinaryOperator[] {
				(a,b) -> a/b,
				(a,b) -> a*b,
				(a,b) -> a-b,
				Double::sum
		};
		
		for (int i = 0; i < 4; i++) {
			JButton btn = new JButton();
			btn.setText(btnNames[i]);
			btn.setBackground(btnColor);
			final int op = i;
			btn.addActionListener(l -> binaryButtonAction(operators[op]));
			cp.add(btn, new RCPosition(i+2, 6));
		}
		
		BinaryButton btn = new BinaryButton(Math::pow, (x,n)->Math.pow(x, 1/n), "x^n", "x^(1/n)");
		btn.setBackground(btnColor);
		btn.setText("x^n");
		btn.addActionListener(l -> binaryButtonAction(btn.getOperator()));
		cb.addActionListener(btn);
		cp.add(btn, new RCPosition(5, 1));
	}
	
	/**
	 * Initializes the binary buttons.
	 */
	private void binaryButtonAction(DoubleBinaryOperator op) {
		if (model.isActiveOperandSet()) {
			if (model.getPendingBinaryOperation() == null) {
				model.setPendingBinaryOperation(op);
				return;
			}
			
			double res = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
			
			String s = Double.toString(res);
			if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
			
			model.setActiveOperand(res);
			model.setPendingBinaryOperation(op);
			model.clear();
			model.freezeValue(s);
			return;
		} 
		
		model.setActiveOperand(model.getValue());
		model.setPendingBinaryOperation(op);
			
		String s = Double.toString(model.getValue());
		if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
			
		model.clear();
		model.freezeValue(s);
	}
	
	/**
	 * Initializes all the other buttons.
	 */
	private void initOtherButtons() {
		// Clear button
		JButton clr = new JButton();
		clr.setText("clr");
		clr.setBackground(btnColor);
		clr.addActionListener(l -> model.clear());
		cp.add(clr, new RCPosition(1, 7));
		
		// Reset button
		JButton reset = new JButton();
		reset.setText("reset");
		reset.setBackground(btnColor);
		reset.addActionListener(l -> model.clearAll());
		cp.add(reset, new RCPosition(2, 7));
		
		// Push button
		JButton push = new JButton();
		push.setText("push");
		push.setBackground(btnColor);
		push.addActionListener(l -> {
			try {
				if (model.isActiveOperandSet() && model.getPendingBinaryOperation() == null) {
					this.memory.push(model.getActiveOperand());
				} else {
					this.memory.push(model.getValue());
				}
			} catch (Exception e) {JOptionPane.showMessageDialog(null, "Nothing to push!");}
		});
		cp.add(push, new RCPosition(3, 7));
		
		// Pop button
		JButton pop = new JButton();
		pop.setText("pop");
		pop.setBackground(btnColor);
		pop.addActionListener(l -> {
			try {
				model.setValue(this.memory.pop());
				model.freezeValue(null);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "The stack is empty!");
			}
		});
		cp.add(pop, new RCPosition(4, 7));
		
		// Equals button
		JButton equals = new JButton();
		equals.setText("=");
		equals.setBackground(btnColor);
		equals.addActionListener(l -> {
			if (!model.isActiveOperandSet() || model.getPendingBinaryOperation() == null) {
				return;
			}

			double res = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
				
			String s = Double.toString(res);
			if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
				
			model.freezeValue(s);
			model.setActiveOperand(res);
			model.setPendingBinaryOperation(null);
			model.clear();
		});

		cp.add(equals, new RCPosition(1, 6));
		
		// Invert sign
		JButton sign = new JButton();
		sign.setText("+/-");
		sign.setBackground(btnColor);
		sign.addActionListener(l -> {
			if (model.isActiveOperandSet()) {
				model.setActiveOperand(-model.getActiveOperand());
				
				String s = Double.toString(model.getActiveOperand());
				if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
				
				model.freezeValue(s);
			} else {
				model.setValue(-model.getValue());
				String s = Double.toString(model.getValue());
				if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
				model.freezeValue(s);
				//model.swapSign();
			}
		});
		cp.add(sign, new RCPosition(5, 4));
		
		// Add decimal dot
		JButton dot = new JButton();
		dot.setText(".");
		dot.setBackground(btnColor);
		dot.addActionListener(l -> {
			try {
				model.insertDecimalPoint();
			} catch (Exception e) {
				//model.clearAll();
			}
		});
		cp.add(dot, new RCPosition(5, 5));
	}
	
	/**
	 * Starts the calculator.
	 * @param args command line arguments // not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
	}
}
