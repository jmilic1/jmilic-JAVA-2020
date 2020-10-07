package hr.fer.zemris.java.hw05.db;

/**
 * Exception used thrown when QueryParser encounters an error.
 * 
 * @author Jura Milić
 *
 */
public class QueryParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a QueryParserException
	 */
	public QueryParserException() {
		super();
	}

	/**
	 * Constructs a QueryParserException with given message.
	 * 
	 * @param message given message
	 */
	public QueryParserException(String message) {
		super(message);
	}
}
