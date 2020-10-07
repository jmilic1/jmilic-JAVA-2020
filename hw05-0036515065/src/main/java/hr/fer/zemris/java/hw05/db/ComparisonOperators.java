package hr.fer.zemris.java.hw05.db;

/**
 * Class which stores operators for caomparing fields of StudentRecord with a
 * String literal.
 * 
 * @author Jura Milić
 *
 */
public class ComparisonOperators {
	/**
	 * Returns true if value1 is less than value2.
	 */
	public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;
	/**
	 * Returns true if value1 is less than or equal to value2.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
	/**
	 * Returns true if value1 is greater than value2.
	 */
	public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
	/**
	 * Returns true if value1 is greater than or equal to value2.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
	/**
	 * Returns true if value1 is equal to value2.
	 */
	public static final IComparisonOperator EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;
	/**
	 * Returns true if value1 is not equal to value2.
	 */
	public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;
	/**
	 * Returns true if value1 contains a substring of value2. Value2 can contain
	 * only one wildcard '*'.
	 * 
	 * @throws IllegalArgumentException if there are more than one wildcards.
	 */
	public static final IComparisonOperator LIKE = (value1, value2) -> {
		boolean wildcard = false;
		for (int j = 0; j < value2.length(); j++) {
			if (value2.charAt(j) == '*') {
				if (wildcard) {
					throw new IllegalArgumentException();
				} else {
					wildcard = true;
				}
			}
		}

		int i = 0;
		int j = 0;
		wildcard = false;

		if (value2.charAt(j) == '*') {
			j++;
			i = value1.indexOf(value2.charAt(j));
			if (i == -1)
				return false;
			wildcard = true;
		}

		while (i != value1.length()) {
			if (value1.charAt(i) != value2.charAt(j)) {
				if (value1.indexOf(value2.charAt(j)) == -1 && value2.charAt(j) == '*') {
					if (wildcard)
						throw new IllegalArgumentException();

					int saved = value1.substring(i).indexOf(value2.charAt(j)) + i;
					j++;
					i = value1.length() - 1;
					j = value2.length() - 1;

					while (value1.charAt(i) == value2.charAt(j)) {
						i--;
						j--;
					}

					if (saved <= i) {
						return true;
					}
				}
				return false;
			} else {
				i++;
				j++;
			}
		}
		if (j == value2.length())
			return true;
		if (value2.charAt(j) == '*') {
			if (wildcard) {
				throw new IllegalArgumentException();
			}
			return true;
		}
		return false;
	};
}