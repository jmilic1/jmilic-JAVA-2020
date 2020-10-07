package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexPolynomialTest {

	@Test
	void exampleTest() {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2, 0), Complex.ONE, Complex.ONE_NEG,
				Complex.IM, Complex.IM_NEG);
		ComplexPolynomial cp = crp.toComplexPolynom();

		assertEquals("(2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0))", crp.toString());
		assertEquals("(2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)", cp.toString());
		assertEquals("(8.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(0.0+i0.0)", cp.derive().toString());
	}

	@Test
	void complexPolynomialStringTest() {
		ComplexPolynomial cp = new ComplexPolynomial(new Complex(2, 1));

		assertEquals("(2.0+i1.0)", cp.toString());

		cp = new ComplexPolynomial(new Complex(5, 1), new Complex(10, -23));
		assertEquals("(10.0-i23.0)*z^1+(5.0+i1.0)", cp.toString());

		cp = new ComplexPolynomial(new Complex(-23, 1), new Complex(4, 10), new Complex(31, -43));
		assertEquals("(31.0-i43.0)*z^2+(4.0+i10.0)*z^1+(-23.0+i1.0)", cp.toString());
	}

	@Test
	void polynomTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(new Complex(2, 3));
		ComplexPolynomial cp2 = new ComplexPolynomial(new Complex(-1, 5));
		
		ComplexPolynomial res = cp1.multiply(cp2);
		assertEquals(0, res.order());
		assertEquals("(-17.0+i7.0)", res.toString());
		
		res = cp2.multiply(cp1);
		assertEquals("(-17.0+i7.0)", res.toString());
		
		cp1 = new ComplexPolynomial(new Complex(2,3), new Complex(-2, 4));
		res = cp1.multiply(cp2);
		assertEquals("(-18.0-i14.0)*z^1+(-17.0+i7.0)", res.toString());
		assertEquals("(-18.0-i14.0)*z^1+(-17.0+i7.0)", cp2.multiply(cp1).toString());
		
		cp2 = new ComplexPolynomial(new Complex(-1, 5), new Complex(3, -6));
		res = cp1.multiply(cp2);
		assertEquals("(18.0+i24.0)*z^2+(6.0-i17.0)*z^1+(-17.0+i7.0)", res.toString());
	}

}
