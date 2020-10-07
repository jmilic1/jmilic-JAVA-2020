package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexNumberTest {

	@Test
	void simpleTest() {
		ComplexNumber c = new ComplexNumber(3, 4);
		assertEquals(3, c.getReal());
		assertEquals(4, c.getImaginary());
		assertEquals(5, c.getMagnitude());
		assertTrue(Math.abs(c.getAngle() - 0.9272) < 1E-4);

		c = new ComplexNumber(3, -4);
		assertEquals(3, c.getReal());
		assertEquals(-4, c.getImaginary());
		assertEquals(5, c.getMagnitude());
		assertTrue(Math.abs(c.getAngle() - 5.35598) < 1E-4);

		c = new ComplexNumber(-3, 4);
		assertEquals(-3, c.getReal());
		assertEquals(4, c.getImaginary());
		assertEquals(5, c.getMagnitude());
		assertTrue(Math.abs(c.getAngle() - 2.21429) < 1E-4);

		c = new ComplexNumber(-3, -4);
		assertEquals(-3, c.getReal());
		assertEquals(-4, c.getImaginary());
		assertEquals(5, c.getMagnitude());
		assertTrue(Math.abs(c.getAngle() - 4.06887) < 1E-4);

		c = new ComplexNumber(0, 0);
		assertEquals(0, c.getReal());
		assertEquals(0, c.getImaginary());
		assertEquals(0, c.getMagnitude());
		assertTrue(Math.abs(c.getAngle() - 0) < 1E-4);
	}

	@Test
	void staticTest() {
		ComplexNumber c = ComplexNumber.fromReal(2);
		assertEquals(2, c.getReal());
		assertEquals(0, c.getImaginary());
		assertEquals(2, c.getMagnitude());
		assertEquals(0, c.getAngle());

		c = ComplexNumber.fromImaginary(2);
		assertEquals(0, c.getReal());
		assertEquals(2, c.getImaginary());
		assertEquals(2, c.getMagnitude());
		assertEquals(Math.PI / 2, c.getAngle());
	}

	@Test
	void magnitudeAngleTest() {
		ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(4, 2.5);
		assertTrue(Math.abs(c.getReal() + 3.20457446219) < 1E-4);
		assertTrue(Math.abs(c.getImaginary() - 2.39388857642) < 1E-4);
		assertTrue(Math.abs(c.getMagnitude() - 4) < 1E-4);
		assertTrue(Math.abs(c.getAngle() - 2.5) < 1E-4);

		c = ComplexNumber.fromMagnitudeAndAngle(4, 0);
		assertEquals(4, c.getReal());
		assertEquals(0, c.getImaginary());
		assertEquals(4, c.getMagnitude());
		assertEquals(0, c.getAngle());

		c = ComplexNumber.fromMagnitudeAndAngle(4, Math.PI / 2);
		assertTrue(Math.abs(c.getReal() + 0) < 1E-4);
		assertTrue(Math.abs(c.getImaginary() - 4) < 1E-4);
		assertEquals(4, c.getMagnitude());
		assertEquals(Math.PI / 2, c.getAngle());

		c = ComplexNumber.fromMagnitudeAndAngle(4, Math.PI);
		assertTrue(Math.abs(c.getReal() + 4) < 1E-4);
		assertTrue(Math.abs(c.getImaginary() - 0) < 1E-4);
		assertEquals(4, c.getMagnitude());
		assertEquals(Math.PI, c.getAngle());

		c = ComplexNumber.fromMagnitudeAndAngle(4, 3 * Math.PI / 2);
		assertTrue(Math.abs(c.getReal() + 0) < 1E-4);
		assertTrue(Math.abs(c.getImaginary() + 4) < 1E-4);
		assertEquals(4, c.getMagnitude());
		assertEquals(3 * Math.PI / 2, c.getAngle());

		c = ComplexNumber.fromMagnitudeAndAngle(4, 2 * Math.PI);
		assertTrue(Math.abs(c.getReal() - 4) < 1E-4);
		assertTrue(Math.abs(c.getImaginary() - 0) < 1E-4);
		assertEquals(4, c.getMagnitude());
		assertEquals(2 * Math.PI, c.getAngle());
	}

	@Test
	void parseTest() {
		ComplexNumber a = ComplexNumber.parse("3.51");
		assertEquals(3.51, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("-3.17");
		assertEquals(-3.17, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("-2.71i");
		assertEquals(0, a.getReal());
		assertEquals(-2.71, a.getImaginary());

		a = ComplexNumber.parse("i");
		assertEquals(0, a.getReal());
		assertEquals(1, a.getImaginary());

		a = ComplexNumber.parse("-i");
		assertEquals(0, a.getReal());
		assertEquals(-1, a.getImaginary());

		a = ComplexNumber.parse("1");
		assertEquals(1, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("-1");
		assertEquals(-1, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("-2.71-3.15i");
		assertEquals(-2.71, a.getReal());
		assertEquals(-3.15, a.getImaginary());

		a = ComplexNumber.parse("351");
		assertEquals(351, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("-317");
		assertEquals(-317, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("3.51");
		assertEquals(3.51, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("-3.17");
		assertEquals(-3.17, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("351i");
		assertEquals(0, a.getReal());
		assertEquals(351, a.getImaginary());

		a = ComplexNumber.parse("-317i");
		assertEquals(0, a.getReal());
		assertEquals(-317, a.getImaginary());

		a = ComplexNumber.parse("3.51i");
		assertEquals(0, a.getReal());
		assertEquals(3.51, a.getImaginary());

		a = ComplexNumber.parse("-3.17i");
		assertEquals(0, a.getReal());
		assertEquals(-3.17, a.getImaginary());

		a = ComplexNumber.parse("-2.71-3.15i");
		assertEquals(-2.71, a.getReal());
		assertEquals(-3.15, a.getImaginary());

		a = ComplexNumber.parse("31+24i");
		assertEquals(31, a.getReal());
		assertEquals(24, a.getImaginary());

		a = ComplexNumber.parse("-1-1i");
		assertEquals(-1, a.getReal());
		assertEquals(-1, a.getImaginary());

		a = ComplexNumber.parse("+2.71");
		assertEquals(2.71, a.getReal());
		assertEquals(0, a.getImaginary());

		a = ComplexNumber.parse("+2.71+3.15i");
		assertEquals(2.71, a.getReal());
		assertEquals(3.15, a.getImaginary());

		a = ComplexNumber.parse("+i");
		assertEquals(0, a.getReal());
		assertEquals(1, a.getImaginary());
	}

	@Test
	public void rootTest() {
		ComplexNumber a = new ComplexNumber(1.732, -1);

		ComplexNumber[] array = a.root(3);

		assertTrue(Math.abs(array[0].getReal() + 0.43091) < 1E-4);
		assertTrue(Math.abs(array[0].getImaginary() - 1.18393) < 1E-4);

		assertTrue(Math.abs(array[1].getReal() + 0.809859) < 1E-4);
		assertTrue(Math.abs(array[1].getImaginary() + 0.965145) < 1E-4);

		assertTrue(Math.abs(array[2].getReal() - 1.2407699) < 10E-4);
		assertTrue(Math.abs(array[2].getImaginary() + 0.218786) < 1E-4);

		ComplexNumber b = new ComplexNumber(2.234, 8.191);
		array = b.root(3);

		assertTrue(Math.abs(array[0].getReal() - 1.8502) < 1E-4);
		assertTrue(Math.abs(array[0].getImaginary() - 0.85941) < 1E-4);

		assertTrue(Math.abs(array[1].getReal() + 1.6694) < 1E-4);
		assertTrue(Math.abs(array[1].getImaginary() - 1.1726) < 1E-4);

		assertTrue(Math.abs(array[2].getReal() + 0.18082) < 10E-4);
		assertTrue(Math.abs(array[2].getImaginary() + 2.0320) < 1E-4);
	}

	@Test
	public void addTest() {
		ComplexNumber a = new ComplexNumber(1.732, -1);
		ComplexNumber b = new ComplexNumber(2.234, 8.191);
		ComplexNumber c = new ComplexNumber(3, 4);

		ComplexNumber ab = a.add(b);
		ComplexNumber ac = a.add(c);
		ComplexNumber bc = b.add(c);
		ComplexNumber abc = a.add(b).add(c);

		assertEquals(1.732 + 2.234, ab.getReal());
		assertEquals(-1 + 8.191, ab.getImaginary());

		assertEquals(1.732 + 3, ac.getReal());
		assertEquals(-1 + 4, ac.getImaginary());

		assertEquals(3 + 2.234, bc.getReal());
		assertEquals(4 + 8.191, bc.getImaginary());

		assertEquals(1.732 + 2.234 + 3, abc.getReal());
		assertEquals(-1 + 8.191 + 4, abc.getImaginary());
	}

	@Test
	public void subTest() {
		ComplexNumber a = new ComplexNumber(1.732, -1);
		ComplexNumber b = new ComplexNumber(2.234, 8.191);
		ComplexNumber c = new ComplexNumber(3, 4);

		ComplexNumber ab = a.sub(b);
		ComplexNumber ac = a.sub(c);
		ComplexNumber bc = b.sub(c);
		ComplexNumber abc = a.sub(b).sub(c);

		assertEquals(1.732 - 2.234, ab.getReal());
		assertEquals(-1 - 8.191, ab.getImaginary());

		assertEquals(1.732 - 3, ac.getReal());
		assertEquals(-1 - 4, ac.getImaginary());

		assertEquals(-3 + 2.234, bc.getReal());
		assertEquals(-4 + 8.191, bc.getImaginary());

		assertEquals(1.732 - 2.234 - 3, abc.getReal());
		assertEquals(-1 - 8.191 - 4, abc.getImaginary());
	}

	@Test
	public void mulTest() {
		ComplexNumber a = ComplexNumber.fromMagnitudeAndAngle(1.732, -1);
		ComplexNumber b = ComplexNumber.fromMagnitudeAndAngle(2.234, 8.191);
		ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(3, 4);

		ComplexNumber ab = a.mul(b);
		ComplexNumber ac = a.mul(c);
		ComplexNumber bc = b.mul(c);
		ComplexNumber abc = a.mul(b).mul(c);

		assertTrue(Math.abs(ab.getMagnitude() - 1.732 * 2.234) < 1E-4);
		assertTrue(Math.abs(ab.getAngle() - 8.191 + 1 + 2 * Math.PI) < 1E-4);

		assertEquals(1.732 * 3, ac.getMagnitude());
		assertTrue(Math.abs(ac.getAngle() - 4 + 1) < 1E-4);

		assertTrue(Math.abs(bc.getMagnitude() - 3 * 2.234) < 1E-4);
		assertTrue(Math.abs(bc.getAngle() - 4 - 8.191 + 2 * Math.PI) < 1E-4);

		assertTrue(Math.abs(abc.getMagnitude() - 3 * 2.234 * 1.732) < 1E-4);
		assertTrue(Math.abs(abc.getAngle() - 4 - 8.191 + 1 + 2 * Math.PI) < 1E-4);
	}
	
	@Test
	public void divTest() {
		ComplexNumber a = ComplexNumber.fromMagnitudeAndAngle(173.2, -1);
		ComplexNumber b = ComplexNumber.fromMagnitudeAndAngle(22.34, 8.191);
		ComplexNumber c = ComplexNumber.fromMagnitudeAndAngle(3, 4);

		ComplexNumber ab = a.div(b);
		ComplexNumber ac = a.div(c);
		ComplexNumber bc = b.div(c);
		ComplexNumber abc = a.div(b).div(c);

		assertTrue(Math.abs(ab.getMagnitude() - 173.2 / 22.34) < 1E-4);
		assertTrue(Math.abs(ab.getAngle() - (-1 - 8.191 + 2 * Math.PI + 2 * Math.PI)) < 1E-4);

		assertTrue(Math.abs(ac.getMagnitude() - 173.2 / 3) < 1E-4);
		assertTrue(Math.abs(ac.getAngle() - (-1 - 4 + 2*Math.PI)) < 1E-4);

		assertTrue(Math.abs(bc.getMagnitude() - 22.34 / 3) < 1E-4);
		assertTrue(Math.abs(bc.getAngle() - (8.191 -4)) < 1E-4);

		assertTrue(Math.abs(abc.getMagnitude() - 173.2 / 22.34 / 3) < 1E-4);
		assertTrue(Math.abs(abc.getAngle() - (-1 - 8.191 - 4 + 6 * Math.PI)) < 1E-4);
	}
	
	@Test
	public void powTest() {
		ComplexNumber a = ComplexNumber.fromMagnitudeAndAngle(17.32, -1);
		ComplexNumber b = ComplexNumber.fromMagnitudeAndAngle(3, 4);

		ComplexNumber aZero = a.power(0);
		ComplexNumber aOne = a.power(1);
		ComplexNumber aTwo = a.power(2);
		ComplexNumber aThree = a.power(3);
		ComplexNumber aFive = a.power(5);
		ComplexNumber aTen = a.power(10);
		ComplexNumber aTwenty = a.power(10).power(2);
		
		ComplexNumber bZero = b.power(0);
		ComplexNumber bOne = b.power(1);
		ComplexNumber bTwo = b.power(2);
		ComplexNumber bThree = b.power(3);
		ComplexNumber bFive = b.power(5);
		ComplexNumber bTen = b.power(10);
		ComplexNumber bTwenty = b.power(10).power(2);
		

		assertTrue(Math.abs(aZero.getMagnitude() - 1) < 1E-4);
		assertTrue(Math.abs(aZero.getAngle() - 0) < 1E-4);

		assertTrue(Math.abs(aOne.getMagnitude() - 17.32) < 1E-4);
		assertTrue(Math.abs(aOne.getAngle() - (-1 + 2*Math.PI)) < 1E-4);

		assertTrue(Math.abs(aTwo.getMagnitude() - Math.pow(17.32, 2)) < 1E-4);
		assertTrue(Math.abs(aTwo.getAngle() - ((-1 + 2*Math.PI)*2 - 2*Math.PI)) < 1E-4);
		
		assertTrue(Math.abs(aThree.getMagnitude() - Math.pow(17.32, 3)) < 1E-4);
		assertTrue(Math.abs(aThree.getAngle() - ((-1+2*Math.PI)*3 - 4*Math.PI)) < 1E-4);

		assertTrue(Math.abs(aFive.getMagnitude() - Math.pow(17.32, 5))< 1E-4);
		assertTrue(Math.abs(aFive.getAngle() - ((-1 + 2*Math.PI)*5 - 8*Math.PI)) < 1E-4);

		assertTrue(Math.abs(aTen.getMagnitude() - Math.pow(17.32, 10)) < 1E-4);
		assertTrue(Math.abs(aTen.getAngle() - ((-1 + 2*Math.PI)*10 - 16*Math.PI)) < 1E-4);
		
		assertTrue(Math.abs(aTwenty.getMagnitude() - Math.pow(17.32, 20)) < 1E-4);
		assertTrue(Math.abs(aTwenty.getAngle() - ((-1 + 2*Math.PI)*20 - 32*Math.PI)) < 1E-4);
		
		
		
		assertTrue(Math.abs(bZero.getMagnitude() - 1) < 1E-4);
		assertTrue(Math.abs(bZero.getAngle() - 0) < 1E-4);

		assertTrue(Math.abs(bOne.getMagnitude() - 3) < 1E-4);
		assertTrue(Math.abs(bOne.getAngle() - 4) < 1E-4);

		assertTrue(Math.abs(bTwo.getMagnitude() - Math.pow(3, 2)) < 1E-4);
		assertTrue(Math.abs(bTwo.getAngle() - (4*2 - 2*Math.PI)) < 1E-4);
		
		assertTrue(Math.abs(bThree.getMagnitude() - Math.pow(3, 3)) < 1E-4);
		assertTrue(Math.abs(bThree.getAngle() - (4*3 - 2*Math.PI)) < 1E-4);

		assertTrue(Math.abs(bFive.getMagnitude() - Math.pow(3, 5))< 1E-4);
		assertTrue(Math.abs(bFive.getAngle() - (4*5 - 6*Math.PI)) < 1E-4);

		assertTrue(Math.abs(bTen.getMagnitude() - Math.pow(3, 10)) < 1E-4);
		assertTrue(Math.abs(bTen.getAngle() - (4*10 - 12*Math.PI)) < 1E-4);
		
		assertTrue(Math.abs(bTwenty.getMagnitude() - Math.pow(3, 20)) < 1E-4);
		assertTrue(Math.abs(bTwenty.getAngle() - (4*20 - 24*Math.PI)) < 1E-4);
	}
	
	@Test
	public void toStringTest() {
		ComplexNumber a = new ComplexNumber(1, 1);
		ComplexNumber b = new ComplexNumber(0, 1);
		ComplexNumber c = new ComplexNumber(1, 0);
		ComplexNumber d = new ComplexNumber(0, 0);
		ComplexNumber e = new ComplexNumber(-1, -1);
		ComplexNumber f = new ComplexNumber(0, -1);
		ComplexNumber g = new ComplexNumber(-1, 0);
		ComplexNumber h = new ComplexNumber(-1, 1);
		ComplexNumber i = new ComplexNumber(23.123, 82.213);
		assertTrue(a.toString().equals("1.0+1.0i"));
		assertTrue(b.toString().equals("0.0+1.0i"));
		assertTrue(c.toString().equals("1.0+0.0i"));
		assertTrue(d.toString().equals("0.0+0.0i"));
		assertTrue(e.toString().equals("-1.0-1.0i"));
		assertTrue(f.toString().equals("0.0-1.0i"));
		assertTrue(g.toString().equals("-1.0+0.0i"));
		assertTrue(h.toString().equals("-1.0+1.0i"));
		assertTrue(i.toString().equals("23.123+82.213i"));
	}
}
