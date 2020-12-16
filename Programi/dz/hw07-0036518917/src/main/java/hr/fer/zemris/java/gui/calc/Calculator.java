package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

public class Calculator extends JFrame {

	/** Generated serial version UID **/
	private static final long serialVersionUID = -5148492242213114522L;
	
	private CalcModel model;
	private Container cp;
	private InverseCheckBox cb;
	/** Used by push and pop **/
	private Stack<Double> memory = new Stack<>();
	private Color btnColor = new Color(221, 221, 255, 255);

	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Java Calculator v1.0");
		setSize(600,400);
		initGUI();
	}
	
	private void initGUI() {
		model = new CalcModelImpl();
		cp = getContentPane();
		cp.setLayout(new CalcLayout(10));
		
		cb = new InverseCheckBox();
		cb.setText("Inv");
		cb.addItemListener(l -> {
			cb.informListeners();
		});
		cp.add(cb, new RCPosition(5, 7));

		initDisplay();
		initDigitButtons();
		initUnaryButtons();
		initBinaryButtons();
		initOtherButtons();
	}
	
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
				} catch (Exception e) {
					
				}
			});
			
			cp.add(btn, new RCPosition(r, c));
		}
	}
	
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
				try {
					model.setActiveOperand(model.getValue());
					double result = btn.getOperator().applyAsDouble(model.getActiveOperand());
					model.setValue(result);
					model.setActiveOperand(result);
					model.clear();
					//model.clearActiveOperand();
				} catch (Exception e) {
					
				}
			});
			cb.addActionListener(btn);
			cp.add(btn, new RCPosition(r, c));
		}
	}
	
	private void initBinaryButtons() {
		String btnNames[] = new String[] {
				"/", "*", "-", "+" 
		};
		
		DoubleBinaryOperator[] operators = new DoubleBinaryOperator[] {
				(a,b) -> a/b,
				(a,b) -> a*b,
				(a,b) -> a-b,
				(a,b) -> a+b
		};
		
		for (int i = 0; i < 4; i++) {
			JButton btn = new JButton();
			btn.setText(btnNames[i]);
			btn.setBackground(btnColor);
			final int op = i;
			btn.addActionListener(l -> {
				try {
					double res = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
//					String s = Double.toString(res);
//					if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
//					model.freezeValue(s);
					model.setValue(res);
				} catch (Exception e) {
					
				}
				try {
					model.setActiveOperand(model.getValue());
					String s = Double.toString(model.getValue());
					model.clear();
					if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
					if (s.length() > 0 && s.charAt(0) == '-')
						model.swapSign();
					model.freezeValue(s);
				} catch (Exception e) {
				}
				model.setPendingBinaryOperation(operators[op]);
				model.clear();
			});
			cp.add(btn, new RCPosition(i+2, 6));
		}
		
		BinaryButton btn = new BinaryButton(Math::pow, (x,n)->Math.pow(x, 1/n), "x^n", "x^(1/n)");
		btn.setBackground(btnColor);
		btn.setText("x^n");
		btn.addActionListener(l -> {
			try {
				double res = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
				model.setValue(res);
			} catch (Exception e) {
				
			}
			try {
				model.setActiveOperand(model.getValue());
				String s = Double.toString(model.getValue());
				model.clear();
				if (s.endsWith(".0")) s = s.substring(0, s.length()-2);
				if (s.length() > 0 && s.charAt(0) == '-')
					model.swapSign();
				model.freezeValue(s);
			} catch (Exception e) {
			}
			model.setPendingBinaryOperation(btn.getOperator());
			model.clear();
		});
		cb.addActionListener(btn);
		cp.add(btn, new RCPosition(5, 1));
	}
	
	private void initOtherButtons() {
		// Clear button
		JButton clr = new JButton();
		clr.setText("clr");
		clr.setBackground(btnColor);
		clr.addActionListener(l -> {
			model.clear();
		});
		cp.add(clr, new RCPosition(1, 7));
		
		// Reset button
		JButton reset = new JButton();
		reset.setText("reset");
		reset.setBackground(btnColor);
		reset.addActionListener(l -> {
			model.clearAll();
		});
		cp.add(reset, new RCPosition(2, 7));
		
		// Push button
		JButton push = new JButton();
		push.setText("push");
		push.setBackground(btnColor);
		push.addActionListener(l -> {
			try {
				this.memory.push(model.getValue());
			} catch (Exception e) {}
		});
		cp.add(push, new RCPosition(3, 7));
		
		// Pop button
		JButton pop = new JButton();
		pop.setText("pop");
		pop.setBackground(btnColor);
		pop.addActionListener(l -> {
			try {
				model.setValue(this.memory.pop());
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
			try {
				double res = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
				model.setValue(res);
				model.clearActiveOperand();
				model.setPendingBinaryOperation(null);
				model.clear();
			} catch (Exception e) {}
		});

		cp.add(equals, new RCPosition(1, 6));
		
		// Invert sign
		JButton sign = new JButton();
		sign.setText("+/-");
		sign.setBackground(btnColor);
		sign.addActionListener(l -> {
			model.swapSign();
		});
		cp.add(sign, new RCPosition(5, 4));
		
		// Add decimal dot
		JButton dot = new JButton();
		dot.setText(".");
		dot.setBackground(btnColor);
		dot.addActionListener(l -> {
			model.insertDecimalPoint();
		});
		cp.add(dot, new RCPosition(5, 5));
	}
	
	private class InverseCheckBox extends JCheckBox {

		/** Generated serial version UID **/
		private static final long serialVersionUID = 7952893386476862646L;
		
		private List<ActionListener> listeners = new ArrayList<>();
		
		public void addActionListener(ActionListener l) {
			listeners.add(l);
		}

		public void removeActionListener(ActionListener l) {
			listeners.remove(l);
		}
		
		/**
		 * Informs all the listeners that there was a change
		 */
		public void informListeners() {
			if (listeners != null) {
				for (ActionListener l : listeners) {
					l.actionPerformed(null);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
}
