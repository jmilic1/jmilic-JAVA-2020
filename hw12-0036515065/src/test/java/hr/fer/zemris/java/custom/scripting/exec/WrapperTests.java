package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WrapperTests {

	@Test
	void myTest() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue()); // v1 now stores Integer(0); v2 still stores null.
		assertEquals(Integer.valueOf(0), v1.getValue());
		assertEquals(null, v2.getValue());

		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue()); // v3 now stores Double(13); v4 still stores Integer(1).
		assertEquals(Double.valueOf(13), v3.getValue());
		assertEquals(Integer.valueOf(1), v4.getValue());

		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue()); // v5 now stores Integer(13); v6 still stores Integer(1).
		assertEquals(Integer.valueOf(13), v5.getValue());
		assertEquals(Integer.valueOf(1), v6.getValue());

		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));

		try {
			v7.add(v8.getValue()); // throws RuntimeException
			fail("Should throw");
		} catch (RuntimeException ex) {
		}
	}

}
