package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	public void hextobyteTest() {
		byte[] expected = new byte[] {1, -82, 34};
		byte[] actual = Util.hextobyte("01aE22");
		
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		assertEquals(expected[2], actual[2]);
		
		expected = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		actual = Util.hextobyte("000102030405060708090a");
		
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
	
	@Test
	public void bytetohexTest() {
		String expected = "01ae22";
		String actual = Util.bytetohex(new byte[] {1, -82, 34});
		
		assertEquals(actual, expected);
		
		expected = "000102030405060708090a";
		actual = Util.bytetohex(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
		assertEquals(actual, expected);
	}

}
