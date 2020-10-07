package hr.fer.zemris.java.gui.charts;

/**
 * Class which models a pair of X and Y values.
 * 
 * @author Jura Milić
 *
 */
public class XYValue {
	/**
	 * X value
	 */
	private int x;
	/**
	 * Y value
	 */
	private int y;

	/**
	 * Constructs an XYValue with given x and y values.
	 * 
	 * @param x given x value
	 * @param y given y value
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return y value
	 */
	public int getY() {
		return y;
	}
}
