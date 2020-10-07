package hr.fer.zemris.math.complexParser;

/**
 * Enumeration for types of tokens used in parsing Complex Numbers
 * 
 * @author Jura Milić
 *
 */
public enum TokenType {
	/**
	 * Type for + or - operators
	 */
	OPERATOR, 
	/**
	 * Type for 'i' character
	 */
	i, 
	/**
	 * Type for numbers
	 */
	NUMBER,
	/**
	 * Type for end of input
	 */
	EOF
}
