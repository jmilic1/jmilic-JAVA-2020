package hr.fer.zemris.java.dao;

/**
 * Exception which is thrown when an error occurs while extracting rows from
 * database.
 * 
 * @author Jura Milić
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a DAOException.
	 */
	public DAOException() {
	}

	/**
	 * Constructs a DAOException with given message, cause and booleans for enabling
	 * suppression and making the stack trace writable.
	 * 
	 * @param message            given message
	 * @param cause              given cause
	 * @param enableSuppression  true if supression should be enabled
	 * @param writableStackTrace true if stack trace should be writable
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a DAOException with given message and cause.
	 * 
	 * @param message given message
	 * @param cause   given cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a DAOException with given message.
	 * 
	 * @param message given message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructs a DAOException with given cause.
	 * 
	 * @param cause given cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}