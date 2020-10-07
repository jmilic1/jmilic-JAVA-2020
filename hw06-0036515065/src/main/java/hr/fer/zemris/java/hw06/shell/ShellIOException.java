package hr.fer.zemris.java.hw06.shell;

/**
 * Exception which is thrown if there is ever an error in reading an input from
 * user of writing an output to user when using Environment.
 * 
 * @author Jura Milić
 *
 */
public class ShellIOException extends RuntimeException {
	/**
	 * Serial version of ShellIOException
	 */
	static final long serialVersionUID = 1L;

	/**
	 * Constructs a ShellIOException.
	 */
	public ShellIOException() {
		super();
	}

	/**
	 * Constructs a ShellIOException with given message.
	 * 
	 * @param message given message
	 */
	public ShellIOException(String message) {
		super(message);
	}
}
