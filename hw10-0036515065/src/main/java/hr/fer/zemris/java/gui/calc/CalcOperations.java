package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;

/**
 * Class containing binary operators used by Calculator.
 * 
 * @author Jura Milić
 *
 */
public class CalcOperations {
	/**
	 * Sum of two numbers.
	 */
	public static final DoubleBinaryOperator SUM = Double::sum;
	/**
	 * Difference of two numbers.
	 */
	public static final DoubleBinaryOperator DIFFERENCE = (x, y) -> x - y;
	/**
	 * Multiplication of two numbers.
	 */
	public static final DoubleBinaryOperator MULTIPLY = (x, y) -> x * y;
	/**
	 * Division of two numbers.
	 */
	public static final DoubleBinaryOperator DIVISION = (x, y) -> x / y;
	/**
	 * Power (x^n)
	 */
	public static final DoubleBinaryOperator POWER = (x, n) -> Math.pow(x, n);
	/**
	 * Root [x^(1/n)]
	 */
	public static final DoubleBinaryOperator ROOT = (x, n) -> Math.pow(x, 1 / n);
}
