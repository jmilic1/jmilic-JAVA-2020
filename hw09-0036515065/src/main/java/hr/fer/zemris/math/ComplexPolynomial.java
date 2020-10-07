package hr.fer.zemris.math;

/**
 * Class which represents a polynomial with complex coefficients (ie. aZ^(n) +
 * bZ^(n-1) + cZ^(n-2)...) This polynomial is defined by it's factors or
 * coefficients. Class offers polynomic multiplication and derive methods.
 * 
 * @author Jura Milić
 *
 */
public class ComplexPolynomial {
	/**
	 * An array of complex factors
	 */
	private Complex[] factors;

	/**
	 * Constructs a ComplexPolynomial with given factors. The first factor in the
	 * array is the factor of the zeroth power, the second factor is the factor of
	 * the first power and so on. [x1, x2, x3,...] => x1Z^0 + x2Z^1 + x3Z^3...
	 * 
	 * @param factors array of given factors
	 */
	public ComplexPolynomial(Complex... factors) {
		this.factors = new Complex[factors.length];
		for (int i = 0; i < factors.length; i++) {
			this.factors[i] = factors[i];
		}
	}

	/**
	 * Returns the order of this polynomial
	 * 
	 * @return order
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplies this polynomial with the given one and creates a new
	 * ComplexPolynomial as a result
	 * 
	 * @param p given polynomial
	 * @return result of multiplication
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int len = order() + p.order();
		Complex[] mulFactors = null;

		if (len == 0) {
			mulFactors = new Complex[] { factors[0].multiply(p.factors[0]) };
		} else {
			mulFactors = new Complex[len + 1];
			Complex[] longer = order() >= p.order() ? factors : p.factors;
			Complex[] shorter = order() < p.order() ? factors : p.factors;

			for (int i = 0; i < len + 1; i++) {
				Complex com = new Complex();

				for (int j = 0; j < longer.length; j++) {
					for (int k = 0; k < shorter.length; k++) {
						if (j + k == i) {
							com = com.add(longer[j].multiply(shorter[k]));
						}
					}
				}
				mulFactors[i] = com;
			}
		}
		return new ComplexPolynomial(mulFactors);
	}

	/**
	 * Returns the first derivative of this polynomial
	 * 
	 * @return result derivative
	 */
	public ComplexPolynomial derive() {
		short n = order();
		Complex[] factors = new Complex[n];

		for (int i = 0; i < n; i++) {
			factors[i] = this.factors[i+1].multiply(new Complex(i+1, 0));
		}
		return new ComplexPolynomial(factors);
	}

	/**
	 * Applies given Complex Number to this polynomial and calculates the resulting
	 * Complex Number
	 * 
	 * @param z given Complex Number
	 * @return result of applying
	 */
	public Complex apply(Complex z) {
		Complex com = new Complex();
		short n = (short) (order());

		for (int i = 0; i <= n; i++) {
			com = com.add(z.power(i).multiply(factors[i]));
		}
		return com;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int n = order();
		if (n == 0) {
			sb.append("(" + factors[0] + ")");
		} else {
			int i = n;
			sb.append("(" + factors[n] + ")*z^" + Integer.valueOf(i));
			i--;
			while (i > 0) {
				factors[i].toString();
				sb.append("+(" + factors[i] + ")*z^" + Integer.valueOf(i));
				i--;
			}
			sb.append("+(" + factors[i] + ")");
		}
		return sb.toString();
	}
}
