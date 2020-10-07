package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Basic Exception thrown when Lexer encounters an error.
 * 
 * @author Jura Milić
 *
 */
public class LexerException extends RuntimeException{
	static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a LexerException with no message.
	 */
	public LexerException() {
	}

	/**
	 * Constructs a LexerException with given message
	 * 
	 * @param message given message
	 */
	public LexerException(String message) {
		super(message);
	}
}