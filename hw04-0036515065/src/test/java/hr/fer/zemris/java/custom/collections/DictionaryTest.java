package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class DictionaryTest {

	@Test
	void isEmptyTest() {
		Dictionary<String, Integer> word = new Dictionary<>();
		assertTrue(word.isEmpty());
		word.put("Ivica", 2);
		assertFalse(word.isEmpty());
	}

	@Test
	void sizeTest() {
		Dictionary<String, Integer> dictionary = new Dictionary<>();
		assertEquals(0, dictionary.size());
		dictionary.put("Ivica", 2);
		assertEquals(1, dictionary.size());
		dictionary.put("Marica", 4);
		assertEquals(2, dictionary.size());
		dictionary.put("Ivica", 1);
		assertEquals(2, dictionary.size());
	}

	@Test
	void clearTest() {
		Dictionary<String, Integer> dictionary = new Dictionary<>();
		dictionary.put("Ivica", 2);
		dictionary.put("Marica", 4);
		dictionary.clear();
		dictionary.isEmpty();
		assertEquals(0, dictionary.size());
	}

	@Test
	void putTest() {
		Dictionary<String, Integer> dictionary = new Dictionary<>();
		assertEquals(0, dictionary.size());
		dictionary.put("Ivica", 2);
		assertEquals(1, dictionary.size());
		dictionary.put("Marica", 4);
		assertEquals(2, dictionary.size());
		dictionary.put("Ivica", 1);
		assertEquals(2, dictionary.size());
		dictionary.put("Jožica", 5);
		assertEquals(3, dictionary.size());
		dictionary.put("Marica", 20);
		assertEquals(3, dictionary.size());

		try {
			dictionary.put(null, 2);
			fail("Should throw");
		} catch (NullPointerException ex) {
		}
	}

	@Test
	void getTest() {
		Dictionary<String, Integer> dictionary = new Dictionary<>();
		dictionary.put("Ivica", 2);
		assertEquals(2, dictionary.get("Ivica"));
		dictionary.put("Marica", 4);
		assertEquals(2, dictionary.get("Ivica"));
		assertEquals(4, dictionary.get("Marica"));
		dictionary.put("Ivica", 1);
		assertEquals(1, dictionary.get("Ivica"));
		assertEquals(4, dictionary.get("Marica"));
		dictionary.put("Jožica", 5);
		assertEquals(1, dictionary.get("Ivica"));
		assertEquals(4, dictionary.get("Marica"));
		assertEquals(5, dictionary.get("Jožica"));
		dictionary.put("Marica", 20);
		assertEquals(1, dictionary.get("Ivica"));
		assertEquals(20, dictionary.get("Marica"));
		assertEquals(5, dictionary.get("Jožica"));

		try {
			dictionary.get(null);
			fail("Should throw");
		} catch (NoSuchElementException ex) {
		}
		
		dictionary.clear();
		try {
			dictionary.get("Ivica");
			fail("Should throw");
		} catch (NoSuchElementException ex) {
		}
	}

}
