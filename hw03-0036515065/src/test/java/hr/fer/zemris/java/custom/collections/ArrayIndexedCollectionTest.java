package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ArrayIndexedCollectionTest {

	@Test
	public void emptyColl() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();

		assertTrue(arrayColl.isEmpty());
		assertEquals(0, arrayColl.size());
	}

	@Test
	public void addRemove() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();
		assertFalse(arrayColl.remove("Ivica"));

		arrayColl.add("Ivica");
		assertFalse(arrayColl.isEmpty());
		assertEquals("Ivica", arrayColl.toArray()[0]);
		assertEquals(1, arrayColl.size());

		assertTrue(arrayColl.remove("Ivica"));
		assertTrue(arrayColl.isEmpty());
		assertEquals(0, arrayColl.size());

		assertFalse(arrayColl.remove("Ivica"));
	}

	@Test
	public void addRemoveAllocate() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection(2);
		assertFalse(arrayColl.remove("Ivica"));

		arrayColl.add("Ivica");
		arrayColl.add(42);
		arrayColl.add(true);
		arrayColl.add(false);

		assertFalse(arrayColl.isEmpty());
		assertEquals("Ivica", arrayColl.toArray()[0]);
		assertEquals(42, arrayColl.toArray()[1]);
		assertEquals(true, arrayColl.toArray()[2]);
		assertEquals(false, arrayColl.toArray()[3]);
		assertEquals(4, arrayColl.size());

		assertTrue(arrayColl.remove("Ivica"));
		assertFalse(arrayColl.isEmpty());
		assertEquals(3, arrayColl.size());

		assertFalse(arrayColl.remove("Ivica"));
		
		try { 
			arrayColl.add(null);
			fail("Should throw exception");
		} catch (NullPointerException ex) {
			
		}
	}

	@Test
	public void containsTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();
		assertFalse(arrayColl.contains("Ivica"));

		arrayColl.add("value");
		arrayColl.add("Ivica");
		assertTrue(arrayColl.contains("Ivica"));
		assertFalse(arrayColl.contains("Marica"));

		arrayColl.add("Marica");
		assertTrue(arrayColl.contains("Ivica"));
		assertTrue(arrayColl.contains("Marica"));

		assertTrue(arrayColl.remove("Ivica"));
		assertFalse(arrayColl.contains("Ivica"));
		assertTrue(arrayColl.contains("Marica"));
	}

	@Test
	public void containsNull() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();
		assertFalse(arrayColl.contains(null));

		arrayColl.add("Ivica");
		assertFalse(arrayColl.contains(null));

		assertTrue(arrayColl.remove("Ivica"));
		assertFalse(arrayColl.contains(null));
	}

	@Test
	public void toArrayTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();

		int a = 42;
		String b = "Ivica";
		boolean c = true;
		arrayColl.add(a);
		arrayColl.add(b);
		arrayColl.add(c);
		Object[] array = arrayColl.toArray();

		assertEquals(a, array[0]);
		assertEquals(b, array[1]);
		assertEquals(c, array[2]);

		assertEquals(42, array[0]);
		assertEquals("Ivica", array[1]);
		assertEquals(true, array[2]);

		assertTrue(arrayColl.remove(b));
		array = arrayColl.toArray();
		assertEquals(a, array[0]);
		assertEquals(c, array[1]);
	}

	@Test
	public void clearTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();
		arrayColl.add(42);
		arrayColl.add("Ivica");
		arrayColl.add(true);

		arrayColl.clear();
		assertTrue(arrayColl.isEmpty());
		assertEquals(0, arrayColl.size());
		
		arrayColl.clear();
		assertTrue(arrayColl.isEmpty());
		assertEquals(0, arrayColl.size());
		
		arrayColl.add("Ivica");
		
		arrayColl.clear();
		assertTrue(arrayColl.isEmpty());
		assertEquals(0, arrayColl.size());
	}

	@Test
	public void getTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();

		arrayColl.add(42);
		arrayColl.add("Ivica");
		arrayColl.add(true);

		assertEquals(42, arrayColl.get(0));
		assertEquals("Ivica", arrayColl.get(1));
		assertEquals(true, arrayColl.get(2));

		try {
			arrayColl.get(-1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}

		try {
			arrayColl.get(arrayColl.size());
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}

	}

	@Test
	public void insertTest() {
		ArrayIndexedCollection A = new ArrayIndexedCollection();
		ArrayIndexedCollection B = new ArrayIndexedCollection(3);

		A.add(42);
		A.add("Ivica");
		A.add(true);

		B.add(42);
		B.add("Ivica");
		B.add(true);

		A.insert('s', 1);
		B.insert('s', 1);

		A.insert('z', A.size());
		B.insert('z', B.size());

		assertEquals(42, A.get(0));
		assertEquals(42, B.get(0));
		assertEquals('s', A.get(1));
		assertEquals('s', B.get(1));
		assertEquals("Ivica", A.get(2));
		assertEquals("Ivica", B.get(2));
		assertEquals(true, A.get(3));
		assertEquals(true, B.get(3));
		assertEquals('z', A.get(4));
		assertEquals('z', B.get(4));

		try {
			A.insert("junk", -1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}

		try {
			A.insert("junk", A.size() + 1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}

		try {
			B.insert("junk", -1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}

		try {
			B.insert("junk", B.size() + 1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}
		
		try {
			A.insert(null, 1);
			fail("Should throw exception");
		} catch (NullPointerException e) {

		}
		
		try {
			B.insert(null, 1);
			fail("Should throw exception");
		} catch (NullPointerException e) {

		}
	}

	@Test
	public void indexOfTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();

		arrayColl.add(42);
		arrayColl.add("Ivica");
		arrayColl.add(true);

		assertEquals(0, arrayColl.indexOf(42));
		assertEquals(1, arrayColl.indexOf("Ivica"));
		assertEquals(2, arrayColl.indexOf(true));

		arrayColl.insert("Marica", 1);

		assertEquals(0, arrayColl.indexOf(42));
		assertEquals(1, arrayColl.indexOf("Marica"));
		assertEquals(2, arrayColl.indexOf("Ivica"));
		assertEquals(3, arrayColl.indexOf(true));

		arrayColl.add(true);

		assertEquals(3, arrayColl.indexOf(true));

		arrayColl.insert(true, 0);

		assertEquals(0, arrayColl.indexOf(true));

		assertEquals(-1, arrayColl.indexOf("suma"));
		assertEquals(-1, arrayColl.indexOf(null));

	}

	@Test
	public void removeIndexTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();

		arrayColl.add(42);
		arrayColl.add("Ivica");
		arrayColl.add(true);
		arrayColl.add("Marica");

		arrayColl.remove(1);

		assertEquals(0, arrayColl.indexOf(42));
		assertEquals(-1, arrayColl.indexOf("Ivica"));
		assertEquals(1, arrayColl.indexOf(true));
		assertEquals(2, arrayColl.indexOf("Marica"));

		try {
			arrayColl.get(-1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}

		try {
			arrayColl.get(arrayColl.size());
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e) {

		}
	}

	@Test
	public void constructorsTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();
		
		arrayColl.add(42);
		arrayColl.add("Ivica");
		arrayColl.add(true);
		arrayColl.add("Marica");

		ArrayIndexedCollection a = new ArrayIndexedCollection(arrayColl);
		
		assertEquals(0, a.indexOf(42));
		assertEquals(1, a.indexOf("Ivica"));
		assertEquals(2, a.indexOf(true));
		assertEquals(3, a.indexOf("Marica"));
		assertEquals(4, a.size());
		
		ArrayIndexedCollection b = new ArrayIndexedCollection(arrayColl, 20);
		
		assertEquals(0, b.indexOf(42));
		assertEquals(1, b.indexOf("Ivica"));
		assertEquals(2, b.indexOf(true));
		assertEquals(3, b.indexOf("Marica"));
		assertEquals(4, b.size());
		
		ArrayIndexedCollection c = new ArrayIndexedCollection(arrayColl, 2);
		
		assertEquals(0, c.indexOf(42));
		assertEquals(1, c.indexOf("Ivica"));
		assertEquals(2, c.indexOf(true));
		assertEquals(3, c.indexOf("Marica"));
		assertEquals(4, c.size());
		
		try{
			c = new ArrayIndexedCollection(null, 5);
			fail("Should throw exception.");
		} catch(NullPointerException ex) {
			
		}
	}

}
