package hr.fer.zemris.math;

/**
 * Simple class used for representing 2D vectors and doing simple vector
 * operations.
 * 
 * @author Jura Milić
 *
 */
public class Vector2D {
	/**
	 * Vector component on x-axis
	 */
	private double x;
	/**
	 * Vector component on y-axis
	 */
	private double y;

	/**
	 * Constructs a Vector2D with given x and y values.
	 * 
	 * @param x given x value
	 * @param y given y value
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return x component of Vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y component of Vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * Translates this Vector by offset as defined by given Vector offset.
	 * 
	 * @param offset given Vector offset
	 */
	public void translate(Vector2D offset) {
		this.x += offset.x;
		this.y += offset.y;
	}

	/**
	 * Returns a new Vector calculated by translating this Vector by offset as
	 * defined by given Vector offset.
	 * 
	 * @param offset given Vector offset
	 * @return new translated Vector
	 */
	public Vector2D translated(Vector2D offset) {
		return new Vector2D(x + offset.x, y + offset.y);
	}

	/**
	 * Rotates this Vector by given angle.
	 * 
	 * @param angle given angle
	 */
	public void rotate(double angle) {
		double temp = x;
		x = Math.cos(angle) * x - Math.sin(angle) * y;
		y = Math.sin(angle) * temp + Math.cos(angle) * y;
	}

	/**
	 * Returns a new Vector calculated by rotating this Vector by given angle
	 * 
	 * @param angle given angle
	 * @return new rotated Vector
	 */
	public Vector2D rotated(double angle) {
		return new Vector2D(Math.cos(angle) * x - Math.sin(angle) * y, Math.sin(angle) * x + Math.cos(angle) * y);
	}

	/**
	 * Scales this vector by given scaler.
	 * 
	 * @param scaler given scaler
	 */
	public void scale(double scaler) {
		x = scaler * x;
		y = scaler * y;
	}

	/**
	 * Returns a new Vector calculated by scaling this Vector by given scaler.
	 * 
	 * @param scaler given scaler
	 * @return new scaled Vector
	 */
	public Vector2D scaled(double scaler) {
		return new Vector2D(scaler * x, scaler * y);
	}

	/**
	 * Makes a new Vector with values copied from this Vector.
	 * 
	 * @return new Vector with copied values.
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
}
