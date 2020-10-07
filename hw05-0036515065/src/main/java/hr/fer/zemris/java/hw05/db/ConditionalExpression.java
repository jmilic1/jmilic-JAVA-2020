package hr.fer.zemris.java.hw05.db;

/**
 * Represents an expression consisting of a ComparisonOperator and two operands.
 * One operand is a a FieldValueGetter which gets a value of a specific field.
 * The other operand is a string literal. Value and literal are compared using
 * ComparisonOperator.
 * 
 * @author Jura Milić
 *
 */
public class ConditionalExpression {
	/**
	 * Value getter
	 */
	private IFieldValueGetter valueGetter;
	/**
	 * String literal
	 */
	private String literal;
	/**
	 * Operator
	 */
	private IComparisonOperator operator;

	/**
	 * Constructs ConditionalExpression with values set by given parameters.
	 * 
	 * @param getter   given value getter
	 * @param literal  given string literal
	 * @param operator given operator
	 */
	public ConditionalExpression(IFieldValueGetter getter, String literal, IComparisonOperator operator) {
		this.valueGetter = getter;
		this.literal = literal;
		this.operator = operator;
	}

	/**
	 * @return operator
	 */
	public IComparisonOperator getComparisonOperator() {
		return operator;
	}

	/**
	 * @return valueGetter
	 */
	public IFieldValueGetter getFieldGetter() {
		return valueGetter;
	}

	/**
	 * @return literal
	 */
	public String getStringLiteral() {
		return literal;
	}
}
