package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple class which represents a Complex Number and offers basic operations
 * for working with complex numbers as well as some simple common static complex
 * numbers.
 * 
 * @author Jura Milić
 *
 */
public class Complex {
	/**
	 * Real value of number.
	 */
	private double re;
	/**
	 * Imaginary value of number
	 */
	private double im;
	/**
	 * Static 0+i0 Complex Number
	 */
	public static final Complex ZERO = new Complex(0, 0);
	/**
	 * Static 1+i0 Complex Number
	 */
	public static final Complex ONE = new Complex(1, 0);
	/**
	 * Static -1+i0 Complex Number
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);
	/**
	 * Static 0+i1 Complex Number
	 */
	public static final Complex IM = new Complex(0, 1);
	/**
	 * Static 0-i1 Complex Number
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Constructs a Complex Number with it's values set to zero.
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Constructs a Complex Number with values set appropriately.
	 * 
	 * @param re given real value
	 * @param im given imaginary value
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates the module of this Complex Number
	 * 
	 * @return calculated module
	 */
	public double module() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Multiplies this Complex Number with the given one
	 * 
	 * @param c given Complex Number
	 * @return result of multiplication
	 */
	public Complex multiply(Complex c) {
		return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	/**
	 * Divides this Complex Number by the given one
	 * 
	 * @param c given Complex Number
	 * @return result of division
	 */
	public Complex divide(Complex c) {
		double a = re;
		double b = im;
		double d = c.re;
		double e = c.im;
		return new Complex((a * d + b * e) / (d * d + e * e), (b * d - a * e) / (d * d + e * e));
	}

	/**
	 * Adds given Complex Number to this one.
	 * 
	 * @param c given Complex Number
	 * @return result of addition
	 */
	public Complex add(Complex c) {
		return new Complex(re + c.re, im + c.im);
	}

	/**
	 * Subtracts given Complex Number from this one.
	 * 
	 * @param c given Complex Number
	 * @return result of subtraction
	 */
	public Complex sub(Complex c) {
		return new Complex(re - c.re, im - c.im);
	}

	/**
	 * Returns the negated number of this Complex Number
	 * 
	 * @return negated Complex Number
	 */
	public Complex negate() {
		return this.multiply(ONE_NEG);
	}

	/**
	 * Calculates the n-th power of this Complex Number
	 * 
	 * @param n given argument
	 * @return Complex Number of n-th power of this number
	 */
	public Complex power(int n) {
		return fromMagnitudeAndAngle(Math.pow(getMagnitude(), n), n * getAngle());
	}

	/**
	 * Calculates all of the n-th roots of this Complex Number. The roots are stored
	 * and returned as a List of Complex Numbers.
	 * 
	 * @param n given argument
	 * @return list of roots
	 */
	public List<Complex> root(int n) {
		List<Complex> list = new ArrayList<>();
		double b = (double) n;
		double magnitude = Math.pow(getMagnitude(), 1 / b);

		for (int i = 0; i < n; i++) {
			double angle = (getAngle() + 2 * i * Math.PI) / n;
			list.add(fromMagnitudeAndAngle(magnitude, angle));
		}
		return list;
	}

	@Override
	public String toString() {
		String real = re == -0 ? Double.toString(0) : Double.toString(re);
		String imaginary = im == -0 ? Double.toString(0) : Double.toString(Math.abs(im));

		if (im < 0) {
			return real + "-i" + imaginary;
		} else {
			return real + "+i" + imaginary;
		}
	}

	/**
	 * Calculates the magnitude of this Complex Number
	 * 
	 * @return magnitude of this number
	 */
	private double getMagnitude() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Calculates the angle of this Complex Number
	 * 
	 * @return angle of this number
	 */
	private double getAngle() {
		if (Math.abs(re - 0) < 1E-4) {
			if (im < 0) {
				return 3 * Math.PI / 2;
			}
			if (im > 0) {
				return Math.PI / 2;
			}
		}
		double angle = Math.atan(im / re);
		if (re < 0) {
			angle += Math.PI;
		}

		if (angle < 0) {
			angle += 2 * Math.PI;
		} else {
			if (angle > 2 * Math.PI) {
				angle -= 2 * Math.PI;
			}
		}
		return angle;
	}

	/**
	 * Creates a new ComplexNumber calculated using given magnitude and angle
	 * 
	 * @param magnitude given magnitude
	 * @param angle     given angle
	 * @return created Complex Number
	 */
	private Complex fromMagnitudeAndAngle(double magnitude, double angle) {
		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
}
