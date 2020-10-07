package hr.fer.zemris.java.hw05.db;

/**
 * Basic interface for a Comparison Operator. Checks if two given strings
 * satisfy an implemented operation.
 * 
 * @author Jura Milić
 *
 */
public interface IComparisonOperator {
	/**
	 * Checks if operation between two operands is satisfied.
	 * 
	 * @param value1 first operand
	 * @param value2 second operand
	 * @return true if comparison is satisfied
	 */
	public boolean satisfied(String value1, String value2);
}
