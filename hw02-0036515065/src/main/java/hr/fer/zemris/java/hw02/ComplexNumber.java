package hr.fer.zemris.java.hw02;

/**
 * Represents a complex number. Stores information of the real and imaginary
 * part of the complex number along with its corresponding polar magnitude and
 * angle
 * 
 * @author Jura Milić
 *
 */
public class ComplexNumber {

	/**
	 * Real value of number
	 */
	private double real;
	/**
	 * Imaginary value of number
	 */
	private double imaginary;

	/**
	 * Constructs a complex number with given real and imaginary values.
	 * 
	 * @param real      given real value
	 * @param imaginary given imaginary value
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * Makes a new complex number from given real value and then returns it.
	 * 
	 * @param real given real value
	 * @return new complex number
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0.0);
	}

	/**
	 * Makes a new complex number from given imaginary value and then returns it.
	 * 
	 * @param imaginary given imaginary value
	 * @return new complex number
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0.0, imaginary);
	}

	/**
	 * Makes a new complex number from given magnitude and angle.
	 * 
	 * @param magnitude given magnitude
	 * @param angle     given angle
	 * @return new complex number
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {

		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);

		return new ComplexNumber(real, imaginary);
	}

	/**
	 * Makes a new complex number by reading given string. Decimal point must be
	 * written with with a period(.) and not a comma(,). Multiple pluses and minuses
	 * in a row are not allowed.
	 * 
	 * @param s given string
	 * @return new complex number
	 */
	public static ComplexNumber parse(String s) {
		StringBuilder sb = new StringBuilder();
		int index = 0;

		if (s.charAt(index) == '+') {
			if (s.charAt(index + 1) == '+' || s.charAt(index + 1) == '-') {
				throw new NumberFormatException();
			}
			index++;
		}

		if (s.charAt(index) == '-') {
			if (s.charAt(index + 1) == '+' || s.charAt(index + 1) == '-') {
				throw new NumberFormatException();
			}
			sb.append("-");
			index++;
		}

		double real = 0;
		double imaginary = 0;

		while (index < s.length()) {
			char c = s.charAt(index);

			if (c == 'i') {
				if (sb.length() == 0) {
					imaginary = 1;
				} else {
					if (sb.toString().equals("-")) {
						imaginary = -1;
					} else {
						try {
							imaginary = Double.parseDouble(sb.toString());
						} catch (Exception ex) {
							throw new NumberFormatException();
						}
					}
				}
			}
			if (c == '+' || c == '-') {
				if (s.charAt(index + 1) == '+' || s.charAt(index + 1) == '-') {
					throw new NumberFormatException();
				}

				try {
					real = Double.parseDouble(sb.toString());
				} catch (Exception ex) {
					throw new NumberFormatException();
				}

				sb = new StringBuilder();
				sb.append("");
				if (c == '-') {
					sb.append(c);
				}
			} else {
				sb.append(c);
			}
			if (index == s.length() - 1 && c != 'i') {
				try {
					real = Double.parseDouble(sb.toString());
				} catch (Exception ex) {
					throw new NumberFormatException();
				}
			}
			index++;
		}
		return new ComplexNumber(real, imaginary);
	}

	/**
	 * @return real value of number
	 */
	public double getReal() {
		return real;
	}

	/**
	 * @return imaginary value of number
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * @return magnitude of number
	 */
	public double getMagnitude() {
		return Math.sqrt(Math.pow(real, 2) + Math.pow(imaginary, 2));
	}

	/**
	 * @return angle of number
	 */
	public double getAngle() {

		double angle = 0;

		if (Math.abs(real - 0) < 1E-4) {
			if (imaginary < 0) {
				return 3 * Math.PI / 2;
			}
			if (imaginary > 0) {
				return Math.PI / 2;
			}
		} else {
			angle = Math.atan(imaginary / real);
			if (angle < 0 && real < 0) {
				angle += Math.PI;
			} else {
				if (angle > 0 && real < 0) {
					angle += Math.PI;
				}
			}

			if (angle < 0) {
				angle += 2 * Math.PI;
			} else {
				if (angle > 2 * Math.PI) {
					angle -= 2 * Math.PI;
				}
			}
		}

		return angle;
	}

	/**
	 * Adds this complex number with given complex number and returns the new
	 * number.
	 * 
	 * @param c given complex number
	 * @return new complex number (this + c)
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(real + c.real, imaginary + c.imaginary);
	}

	/**
	 * Subtracts given complex number from this complex number and returns the new
	 * number.
	 * 
	 * @param c given complex number
	 * @return new complex number (this - c)
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(real - c.real, imaginary - c.imaginary);
	}

	/**
	 * Multiplies this complex number with given complex number and returns the new
	 * number.
	 * 
	 * @param c given complex number
	 * @return new complex number (this * c)
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return ComplexNumber.fromMagnitudeAndAngle(this.getMagnitude() * c.getMagnitude(),
				this.getAngle() + c.getAngle());
	}

	/**
	 * Divides this complex number with given complex number and returns the new
	 * number.
	 * 
	 * @param c given complex number
	 * @return new complex number (this / c)
	 */
	public ComplexNumber div(ComplexNumber c) {
		return ComplexNumber.fromMagnitudeAndAngle(this.getMagnitude() / c.getMagnitude(),
				this.getAngle() - c.getAngle());
	}

	/**
	 * Calculates this complex number to the given integer and returns the
	 * calculated value.
	 * 
	 * @param n given integer
	 * @return new complex number (this ^ n)
	 * @throws IllegalArgumentException if n < 0
	 */
	public ComplexNumber power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		return fromMagnitudeAndAngle(Math.pow(getMagnitude(), n), n * getAngle());
	}

	/**
	 * Calculates the nth root of this ComplexNumber and returns the calculated
	 * value.
	 * 
	 * @param n root
	 * @return new complex number (this ^ 1/n)
	 * @throws IllegalArgumentException if n <= 0
	 */
	public ComplexNumber[] root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}

		double b = (double) n;
		double magnitude = Math.pow(getMagnitude(), 1 / b);
		double[] angles = new double[n];
		ComplexNumber[] numbers = new ComplexNumber[n];

		for (int i = 0; i < n; i++) {
			angles[i] = (getAngle() + 2 * i * Math.PI) / n;
			numbers[i] = fromMagnitudeAndAngle(magnitude, angles[i]);
		}
		return numbers;
	}

	public String toString() {
		if (imaginary < 0) {
			return Double.toString(real) + Double.toString(imaginary) + "i";
		} else {
			return Double.toString(real) + "+" + Double.toString(imaginary) + "i";
		}
	}
}
