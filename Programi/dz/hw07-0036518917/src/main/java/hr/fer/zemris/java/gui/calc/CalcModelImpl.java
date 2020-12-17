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
public class CalcModelImpl implements CalcModel {
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
	public CalcModelImpl() {
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
		if (value == null)
			return activeOperand;
		return positive ? value : -value;
	}

	@Override
	public void setValue(double value) {
		this.positive = value >= 0;
		this.value = Math.abs(value);
		if (Double.isNaN(value)) {
			this.input = "NaN";
		} else if (Double.isInfinite(value)) {
			this.input = "Infinity";
		} else {
			this.input = Double.toString(this.value);
		}
		this.frozen = null;
		this.editable = false;
		
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
		this.editable = true;
		informListeners();
	}

	@Override
	public void clearAll() {
		this.frozen = null;
		this.positive = true;
		clear();
		clearActiveOperand();
		pendingOperation = null;
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (!editable) 
			throw new CalculatorInputException("Calculator model is not editable!");
		this.positive = !this.positive;
		this.frozen = null;
		informListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable) 
			throw new CalculatorInputException("Calculator model is not editable!");
		if (input.contains("."))
			throw new CalculatorInputException("Input already contains decimal point!");
		if (input.length() == 0)
			throw new CalculatorInputException("There were no digits before decimal point!");
		
		this.input += ".";
		this.value = Double.parseDouble(input);
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
			this.frozen = null;
			this.value = newValue;
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
			throw new IllegalStateException();
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
	}

	@Override
	public boolean hasFrozenValue() {
		return this.frozen != null;
	}

	@Override
	public String toString() {
		if (hasFrozenValue())
			return (frozen.equals("") ? "0" : frozen);
		else 
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
