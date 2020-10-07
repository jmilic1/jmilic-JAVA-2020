package hr.fer.zemris.math.complexParser;

/**
 * Class which models simple Tokens used in lexing a Complex Number input.
 * 
 * @author Jura Milić
 *
 */
public class Token {
	/**
	 * Type of Token
	 */
	private TokenType type;
	/**
	 * Value of Token
	 */
	private Object value;

	/**
	 * Constructs a Token with values set accordingly.
	 * 
	 * @param type given type
	 * @param value given value
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @return value of Token
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return type of Token
	 */
	public TokenType getType() {
		return type;
	}
}
