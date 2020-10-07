package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Token represents a lexical token in lexing of Lexer class. It consists of a
 * value that was read by Lexer and a type that was determined by Lexer.
 * 
 * @author Jura Milić
 *
 */
public class Token {
	/**
	 * Type of read object. TokenType is an enumeration defined elsewhere.
	 */
	private TokenType type;
	/**
	 * Value of read object.
	 */
	private Object value;

	/**
	 * Constructs a Token with given type and given value.
	 * 
	 * @param type  given type
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
	 * @return type of Token (TokenType enumeration)
	 */
	public TokenType getType() {
		return type;
	}
}
