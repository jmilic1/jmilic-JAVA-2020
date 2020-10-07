package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.DoubleBinaryOperator;

/**
 * Simple object wrapper which offers 4 simple operations which can be used if
 * the wrapper null or if it is an instance of String, Integer or Double class.
 * 
 * @author Jura Milić
 *
 */
public class ValueWrapper {
	/**
	 * Binary operator for addition.
	 */
	private static final DoubleBinaryOperator ADD = Double::sum;
	/**
	 * Binary operator for subtraction.
	 */
	private static final DoubleBinaryOperator SUB = (x, y) -> x - y;
	/**
	 * Binary operator for multiplication.
	 */
	private static final DoubleBinaryOperator MUL = (x, y) -> x * y;
	/**
	 * Binary operator for division.
	 */
	private static final DoubleBinaryOperator DIV = (x, y) -> x / y;
	/**
	 * The wrapped object's value.
	 */
	private Object value;

	/**
	 * Constructs a ValueWrapper with given value.
	 * 
	 * @param value given value
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Sets the wrapped object's value.
	 * 
	 * @param value given value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns the wrapped object's value.
	 * 
	 * @return object value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Adds the given value to this wrapped object's value. Operation can only be
	 * executed if operands are null, Integer, Double or a String representation of
	 * a number. If either operands are an instance of Double, the result will also
	 * be an instance of Double, otherwise the result is an instance of Integer.
	 * 
	 * @param incValue given value
	 * @throws RuntimeException if either operands are not String, Integer, Double
	 *                          nor null
	 */
	public void add(Object incValue) {
		doArithmetic(determineValue(value), determineValue(incValue), ADD);
	}

	/**
	 * Subtracts the given value from this wrapped object's value. Operation can
	 * only be executed if operands are null, Integer, Double or a String
	 * representation of a number. If either operands are an instance of Double, the
	 * result will also be an instance of Double, otherwise the result is an
	 * instance of Integer.
	 * 
	 * @param decValue given value
	 * @throws RuntimeException if either operands are not String, Integer, Double
	 *                          nor null
	 */
	public void subtract(Object decValue) {
		doArithmetic(determineValue(value), determineValue(decValue), SUB);
	}

	/**
	 * Multiplies the given value by this wrapped object's value and stores the
	 * result in this ValueWrapper. Operation can only be executed if operands are
	 * null, Integer, Double or a String representation of a number. If either
	 * operands are an instance of Double, the result will also be an instance of
	 * Double, otherwise the result is an instance of Integer.
	 * 
	 * @param mulValue given value
	 * @throws RuntimeException if either operands are not String, Integer, Double
	 *                          nor null
	 */
	public void multiply(Object mulValue) {
		doArithmetic(determineValue(value), determineValue(mulValue), MUL);
	}

	/**
	 * Divides this wrapped object's value by the given value and stores the result
	 * in this ValueWrapper. Operation can only be executed if operands are null,
	 * Integer, Double or a String representation of a number. If either operands
	 * are an instance of Double, the result will also be an instance of Double,
	 * otherwise the result is an instance of Integer.
	 * 
	 * @param divValue given value
	 * @throws RuntimeException if either operands are not String, Integer, Double
	 *                          nor null
	 */
	public void divide(Object divValue) {
		doArithmetic(determineValue(value), determineValue(divValue), DIV);
	}

	/**
	 * Compares this wrapped object's value to given value. Operation can only be
	 * executed if operands are null, Integer, Double or a String representation of
	 * a number.
	 * 
	 * @param withValue given value
	 * @return -1 if wrapped object is less than given, 1 if it is larger than
	 *         given, or 0 if equal
	 * @throws RuntimeException if either operands are not String, Integer, Double
	 *                          nor null
	 */
	public int numCompare(Object withValue) {
		return Double.compare(determineValue(value).doubleValue(), determineValue(withValue).doubleValue());
	}

	/**
	 * Returns a Number representation of given object. Valid instances are of
	 * Integer, Double, or String classes. If given object is null, the method will
	 * return an Integer with value 0.
	 * 
	 * @param arg given object
	 * @return Integer or Double representation of the given object
	 */
	private Number determineValue(Object arg) {
		if (arg == null) {
			return Integer.valueOf(0);
		}

		if (!(arg instanceof String) && !(arg instanceof Double) && !(arg instanceof Integer)) {
			throw new RuntimeException("Cannot perform arithmetic on non-number objects!");
		}

		if (arg instanceof String) {
			return stringToNumber((String) arg);
		}

		return (Number) arg;
	}

	/**
	 * Tries to parse a string into a number. If string can not be parsed into an
	 * Integer, it will be parsed into Double.
	 * 
	 * @param arg given string
	 * @return Number representation of string
	 * @throws RuntimeException if string does not represent a number
	 */
	private Number stringToNumber(String arg) {
		try {
			return Integer.valueOf(arg);
		} catch (NumberFormatException ex) {
			try {
				return Double.valueOf(arg);
			} catch (NumberFormatException ex2) {
				throw new RuntimeException("Unable to do arithmetic on non number Strings!");
			}
		}
	}

	/**
	 * Executes the given DoubleBinaryOperator on given Number operands and stores
	 * the result into this ValueWrapper. If either operands are instances of
	 * Double, the result will also be a Double instance, otherwise result is an
	 * instance of Integer.
	 * 
	 * @param arg1 given first operand
	 * @param arg2 given second operand
	 * @param op   given DoubleBinaryOperator
	 */
	private void doArithmetic(Number arg1, Number arg2, DoubleBinaryOperator op) {
		if (arg1 instanceof Double || arg2 instanceof Double) {
			value = Double.valueOf(op.applyAsDouble(arg1.doubleValue(), arg2.doubleValue()));
		} else {
			value = Integer
					.valueOf(Double.valueOf(op.applyAsDouble(arg1.doubleValue(), arg2.doubleValue())).intValue());
		}
	}
}