package hr.fer.zemris.java.gui.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * An implementation of CalcModel used for modeling a basic Calculator not
 * unlike the default calculator in Windows XP operating systems.
 * 
 * @author Jura Milić
 *
 */
public class CalcModelImpl implements CalcModel {
	/**
	 * Indicates whether the calculator is editable. If calculator tries to edit
	 * it's value while it is not in an editable state it will throw an exception.
	 */
	private boolean editable;
	/**
	 * Checks if the value of the calculator is positive.
	 */
	private boolean positive;
	/**
	 * String representation of the current value in calculator.
	 */
	private String value;
	/**
	 * double representation of the current value in calculator
	 */
	private double number;
	/**
	 * Current frozen value in calculator.
	 */
	private String frozenValue;
	/**
	 * The first operand used in next binary operation.
	 */
	private double activeOperand;
	/**
	 * Checks whether the first Operand is set.
	 */
	private boolean isActiveOperandSet;
	/**
	 * Pending Binary Operation to be executed.
	 */
	private DoubleBinaryOperator pendingOperation;
	/**
	 * All the value listeners which need to check whether the value of calculator
	 * has changed.
	 */
	private List<CalcValueListener> listeners;

	/**
	 * Constructs CalcModelImpl with default parameters.
	 */
	public CalcModelImpl() {
		editable = true;
		positive = true;
		value = "";
		number = 0;
		frozenValue = null;
		isActiveOperandSet = false;
		pendingOperation = null;
		listeners = new ArrayList<CalcValueListener>();
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
		if (positive) {
			return number;
		} else {
			return -number;
		}
	}

	@Override
	public void setValue(double value) {
		if (positive && value < 0) {
			positive = false;
			value = -value;
		} else {
			if (!positive && value > 0) {
				positive = true;
				value = -value;
			}
		}

		number = value;

		if (Double.isNaN(value)) {
			this.value = "NaN";
		} else {
			if (Double.isInfinite(number)) {
				this.value = "";

				this.value += "Infinity";
			} else {
				this.value = Double.toString(number);
			}
		}
		editable = false;

		updateListeners();
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		value = "";
		number = 0;
		editable = true;

		updateListeners();
	}

	@Override
	public void clearAll() {
		frozenValue = null;
		clear();
		isActiveOperandSet = false;
		pendingOperation = null;
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if (editable) {
			if (positive) {
				positive = false;
			} else {
				positive = true;
			}

			frozenValue = null;

			updateListeners();
		} else {
			throw new CalculatorInputException("Sign was tried to be swapped while value was not editable!");
		}
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if (!editable) {
			throw new CalculatorInputException("Model is not editable!");
		}

		try {
			Integer.valueOf(value);
		} catch (NumberFormatException ex) {
			throw new CalculatorInputException("Value already contains a decimal point!");
		}

		value += ".";

		frozenValue = null;

		updateListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if (editable) {
			if (!value.equals("0") || digit != 0) {
				if (Math.abs(number - 1E+308) < 1E-5) {
					throw new CalculatorInputException("Number too large!");
				}
				String tmp = value + digit;

				try {
					Double.valueOf(tmp);
				} catch (NumberFormatException ex) {
					throw new CalculatorInputException("Could not parse String into double!");
				}

				if (value.equals("0")) {
					value = "";
				}

				value += digit;
				number = Double.valueOf(value);
				frozenValue = null;

				updateListeners();
			}
		} else {
			throw new CalculatorInputException("Model tried to insert a digit while model was not editable!");
		}
	}

	@Override
	public boolean isActiveOperandSet() {
		return isActiveOperandSet;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if (isActiveOperandSet) {
			return activeOperand;
		} else {
			throw new IllegalStateException("Active operand was not set!");
		}
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		isActiveOperandSet = true;
	}

	@Override
	public void clearActiveOperand() {
		isActiveOperandSet = false;
		this.activeOperand = 0;
	}

	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		if (op == null) {
			pendingOperation = op;
		} else {
			double res = number;
			if (isActiveOperandSet && pendingOperation != null) {
				res = this.pendingOperation.applyAsDouble(number, activeOperand);
			}
			freezeValue(Double.toString(res));
			setActiveOperand(res);
			pendingOperation = op;
			clear();
		}
	}

	@Override
	public String toString() {
		if (frozenValue == null) {
			String result = "";

			if (!positive) {
				result = "-";
			}

			if (value.length() == 0) {
				result += "0";
			} else {
				result += value;
			}
			return result;
		} else {
			return frozenValue;
		}
	}

	/**
	 * Stores given String in frozenValue variable.
	 * 
	 * @param value given String
	 */
	public void freezeValue(String value) {
		frozenValue = value;
	}

	/**
	 * Checks whether a frozen value exists
	 * 
	 * @return true if frozen exists, false otherwise
	 */
	public boolean hasFrozenValue() {
		if (frozenValue == null) {
			return false;
		}
		return true;
	}

	/**
	 * Updates all of the value listeners the model has stored.
	 */
	private void updateListeners() {
		for (CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}

}
