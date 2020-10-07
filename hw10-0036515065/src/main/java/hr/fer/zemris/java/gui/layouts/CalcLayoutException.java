package hr.fer.zemris.java.gui.layouts;

/**
 * An Exception used to indicate that an error occurred while laying out a
 * Calculator gui.
 * 
 * @author Jura Milić
 *
 */
public class CalcLayoutException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a CalcLayoutExcepion
	 */
	public CalcLayoutException() {
		super();
	}

	/**
	 * Constructs a CalcLayoutException with given message to be show to user.
	 * 
	 * @param message given message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}

}
