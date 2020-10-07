package hr.fer.zemris.math;

/**
 * Simple class which represents a three-component Vector and offers
 * vector-based operations. Components are stored as double values.
 * 
 * @author Jura Milić
 *
 */
public class Vector3 {
	/**
	 * X-component
	 */
	private double x;
	/**
	 * Y-component
	 */
	private double y;
	/**
	 * Z-component
	 */
	private double z;

	/**
	 * Constructs a 3-component Vector with components set appropriately.
	 * 
	 * @param x given x-value
	 * @param y given y-value
	 * @param z given z-value
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the length of this Vector.
	 * 
	 * @return length
	 */
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns a new Vector which has same orientation as this Vector but it's
	 * length is equal to zero
	 * 
	 * @return new normalized Vector
	 */
	public Vector3 normalized() {
		double length = this.norm();
		return new Vector3(x / length, y / length, z / length);
	}

	/**
	 * Returns a new Vector that is a result of adding the given Vector to this
	 * Vector.
	 * 
	 * @param other given Vector
	 * @return result Vector
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Returns a new Vector that is a result of subtracting the given Vector from
	 * this Vector. (this - other)
	 * 
	 * @param other given Vector
	 * @return result Vector
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Calculates the dot product of given Vector and this Vector.
	 * 
	 * @param other given Vector
	 * @return product value
	 */
	public double dot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Returns the Vector result which is the cross product of this Vector and given
	 * Vector (this X other).
	 * 
	 * @param other
	 * @return cross Vector
	 */
	public Vector3 cross(Vector3 other) {
		double newX = y * other.z - other.y * z;
		double newY = x * other.z - other.x * z;
		double newZ = x * other.y - other.x * y;
		return new Vector3(newX, newY, newZ);
	}

	/**
	 * Returns a new Vector which is a result of scaling this Vector by given
	 * scaler.
	 * 
	 * @param s given scaler
	 * @return scaled Vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Returns the cosine value of the angle between this Vector and given Vector.
	 * 
	 * @param other given Vector
	 * @return value of cosine of angle
	 */
	public double cosAngle(Vector3 other) {
		return dot(other) / (norm() * other.norm());
	}

	/**
	 * @return X
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return Y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return Z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns a double array representation of Vector: [x, y, z]
	 * 
	 * @return array of component values
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}
}
