package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Implementation of the CalcModel
 * @author sbolsec
 *
 */
public class CalcModelImpl2 implements CalcModel {
	/** Stores whether the model is editable **/
	private boolean editable;
	/** Stores whether the input is positive **/
	private boolean positive;
	/** String representation of input digits **/
	private String input;
	/** Numeric value of input digits **/
	private Double value;
	/** Frozen value of the display **/
	private String frozen;
	/** Active operand **/
	private Double activeOperand;
	/** Operation pending to be executed **/
	private DoubleBinaryOperator pendingOperation;
	/** Listeners **/
	private List<CalcValueListener> listeners = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public CalcModelImpl2() {
		editable = true;
		positive = true;
		input = "";
		value = 0.;
		frozen = null;
	}
	
	@Override
	public void addCalcValueListener(CalcValueListener l) {
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		listeners.remove(l);
	}

	@Override
	public double getValue() {
		return this.value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
		this.positive = value >= 0;
		this.editable = false;
		
		String s = Double.toString(value);
		if (Double.isNaN(value))
			this.positive = true;
		if (positive)
			this.input = s;
		else 
			this.input = s.substring(1);
		
		informListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		this.input = "";
		this.value = null;
		this.positive = true;
		this.editable = true;
		
		informListeners();
	}

	@Override
	public void clearAll() {
		clear();
		clearActiveOperand();
		this.pendingOperation = null;
		this.frozen = null;
		
		informListeners();
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable)
			throw new CalculatorInputException("Calculator model is not editable!");
		
		this.frozen = null;
		this.positive = !this.positive;
		
		if (this.value != null)
			this.value = -this.value;
		
		informListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable)
			throw new CalculatorInputException("Calculator model is not editable!");
		if (input.contains("."))
			throw new CalculatorInputException("Input already contains decimal point!");
		if (input.length() == 0)
			//this.input = "0";
			throw new CalculatorInputException("There were no digits before decimal point!");
		
		this.input += ".";
		this.frozen = null;
		
		informListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (!editable)
			throw new CalculatorInputException("Calculator model is not editable!");
		if ((input.substring(0, input.indexOf('.') == -1 ? input.length() : input.indexOf('.'))).length() == 308)
			throw new CalculatorInputException("Number is too big!");

		String newInput = input + digit;
		if (newInput.charAt(0) == '0') 
			newInput = newInput.replaceFirst("0+", "0");
		if (newInput.length() >= 2) {
			if (newInput.charAt(0) == '0' && Character.isDigit(newInput.charAt(1))) {
				newInput = newInput.substring(1);
			}
		}
		try {
			double newValue = Double.parseDouble(newInput);
			this.input = newInput;
			this.value = positive ? newValue : -newValue;
			this.frozen = null;
		} catch (NumberFormatException e) {
			throw new CalculatorInputException("New input could not be parsed as double, it was: " + newInput);
		}
		
		informListeners();
	}

	@Override
	public boolean isActiveOperandSet() {
		return this.activeOperand != null;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (this.activeOperand == null)
			throw new IllegalStateException("Active operand is not set!");
		return this.activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
	}

	@Override
	public void clearActiveOperand() {
		this.activeOperand = null;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return this.pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingOperation = op;
	}
	
	@Override
	public void freezeValue(String value) {
		this.frozen = value;
		
		informListeners();
	}

	@Override
	public boolean hasFrozenValue() {
		return this.frozen != null;
	}

	@Override
	public String toString() {
		if (hasFrozenValue()) {
			return this.frozen;
		}
		return (positive ? "" : "-") + (input.equals("") ? "0" : input);
	}
	
	/**
	 * Informs all the listeners that there was a change
	 */
	private void informListeners() {
		if (listeners != null) {
			for (CalcValueListener l : listeners) {
				l.valueChanged(this);
			}
		}
	}
}
