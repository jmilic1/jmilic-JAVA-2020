package hr.fer.zemris.java.custom.collections;

/**
 * Basic Exception thrown when ObjectStack encounters an error
 * 
 * @author Jura Milić
 *
 */
public class EmptyStackException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs new EmptyStackException with no message.
	 */
	public EmptyStackException() {

	}

	/**
	 * Constructs new EmptyStackException with given message
	 * 
	 * @param message given message
	 */
	public EmptyStackException(String message) {
		super(message);
	}
}
