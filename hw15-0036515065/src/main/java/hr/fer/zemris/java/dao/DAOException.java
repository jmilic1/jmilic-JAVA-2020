package hr.fer.zemris.java.dao;

/**
 * Exception which will be thrown whenever a DAO error occurrs.
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Generates a DAOException
	 * 
	 * @param message given message
	 * @param cause   given cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Generates a DAOException
	 * 
	 * @param message given message
	 */
	public DAOException(String message) {
		super(message);
	}
}