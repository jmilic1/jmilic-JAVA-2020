package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * An implementation of IFilter which accepts given record based on defined
 * conditional expressions.
 * 
 * @author Jura Milić
 *
 */
public class QueryFilter implements IFilter {
	/**
	 * Defined expressions
	 */
	private List<ConditionalExpression> expressions;

	/**
	 * Constructs a QueryFilter with given expressions.
	 * 
	 * @param expressions given expressions
	 */
	public QueryFilter(List<ConditionalExpression> expressions) {
		this.expressions = expressions;
	}

	/**
	 * Checks if given record satisfies every defined expression
	 */
	@Override
	public boolean accepts(StudentRecord record) {

		for (ConditionalExpression expression : expressions) {
			if (!expression.getComparisonOperator().satisfied(expression.getFieldGetter().get(record),
					expression.getStringLiteral()))
				return false;
		}
		return true;

	}

}
