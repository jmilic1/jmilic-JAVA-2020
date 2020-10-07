package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Types of Tokens used for tokenising a query input.
 * 
 * @author Jura Milić
 *
 */
public enum TokenType {
	/**
	 * Field attribute of StudentRecord
	 */
	FIELD,
	/**
	 * String literal
	 */
	LITERAL,
	/**
	 * Comparison operator
	 */
	OPERATOR,
	/**
	 * Expression merger
	 */
	AND,
	/**
	 * End of file
	 */
	EOF
}
