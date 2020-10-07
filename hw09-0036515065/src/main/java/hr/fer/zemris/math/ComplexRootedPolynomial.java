package hr.fer.zemris.math;

/**
 * Class which represents roots of a complex polynomial which are interpreted as
 * C * (Z-r1) * (Z-r2) * (Z-r3)... where C is a constant and r1,r2,r3... are
 * roots of the polynomial.
 * 
 * @author Jura Milić
 *
 */
public class ComplexRootedPolynomial {
	/**
	 * Constant by which the roots should be multiplied
	 */
	private Complex constant;
	/**
	 * An array containing roots of the polynomial
	 */
	private Complex[] roots;

	/**
	 * Constructs a ComplexRootedPolynomial with values set appropriately
	 * 
	 * @param constant given constant
	 * @param roots    given roots
	 */
	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		this.constant = constant;
		this.roots = new Complex[roots.length];
		for (int i = 0; i < roots.length; i++) {
			this.roots[i] = roots[i];
		}
	}

	/**
	 * Applies given Complex Number to roots and returns the result
	 * 
	 * @param z given Complex Number
	 * @return result of applying
	 */
	public Complex apply(Complex z) {
		Complex result = z.sub(roots[0]);
		for (int i = 1; i < roots.length; i++) {
			result = result.multiply(z.sub(roots[i]));
		}
		return constant.multiply(result);
	}

	/**
	 * Creates a corresponding Complex Polynom for the roots
	 * 
	 * @return created Complex Polynom
	 */
	public ComplexPolynomial toComplexPolynom() {
		int k = roots.length;
		Complex[] factors = new Complex[k + 1];
		if (k == 0) {
			factors[0] = constant;
		} else {
			Complex neg = Complex.ONE_NEG;
			factors[0] = constant.multiply(roots[0]).multiply(neg);
			factors[1] = constant;

			for (int i = 1; i < k; i++) {
				Complex root = roots[i];

				factors[i + 1] = constant;
				for (int j = i; j > 0; j--) {
					factors[j] = factors[j].multiply(root).multiply(neg).add(factors[j - 1]);
				}
				factors[0] = factors[0].multiply(root).multiply(neg);
			}
		}
		return new ComplexPolynomial(factors);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + constant + ")");
		for (Complex root : roots) {
			sb.append("*(z-(" + root + "))");
		}
		return sb.toString();
	}

	/**
	 * Finds the closest root to the given Complex Number within the given treshold
	 * and returns it's index in the array
	 * 
	 * @param z        given Complex Number
	 * @param treshold given treshold
	 * @return index of found root
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index = -1;
		double diff = 0;
		treshold = Math.abs(treshold);

		for (int i = 0; i < roots.length; i++) {
			double tempDiff = z.sub(roots[i]).module();
			if (Math.abs(tempDiff) < treshold) {
				if (index == -1) {
					diff = tempDiff;
					index = i;
				} else {
					if (tempDiff < diff) {
						diff = tempDiff;
						index = i;
					}
				}
			}
		}
		return index;
	}
}
