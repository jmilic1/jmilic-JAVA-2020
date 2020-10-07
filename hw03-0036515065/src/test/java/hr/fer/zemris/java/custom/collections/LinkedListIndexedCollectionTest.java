package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {

	@Test
	public void emptyColl() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();

		assertTrue(listColl.isEmpty());
		assertEquals(0, listColl.size());
	}

	@Test
	public void addRemove() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		assertFalse(listColl.remove("Ivica"));

		listColl.add("Ivica");
		assertFalse(listColl.isEmpty());
		assertEquals("Ivica", listColl.toArray()[0]);
		assertEquals(1, listColl.size());

		assertTrue(listColl.remove("Ivica"));
		assertTrue(listColl.isEmpty());
		assertEquals(0, listColl.size());
		assertFalse(listColl.remove("Ivica"));
		
		listColl.add("Ivica");
		listColl.add(42);
		listColl.add(true);
		listColl.add(false);

		assertFalse(listColl.isEmpty());
		assertEquals("Ivica", listColl.toArray()[0]);
		assertEquals(42, listColl.toArray()[1]);
		assertEquals(true, listColl.toArray()[2]);
		assertEquals(false, listColl.toArray()[3]);
		assertEquals(4, listColl.size());

		assertTrue(listColl.remove("Ivica"));
		assertFalse(listColl.isEmpty());
		assertEquals(3, listColl.size());
		assertFalse(listColl.remove("Ivica"));
		
		assertTrue(listColl.remove(true));
		assertFalse(listColl.isEmpty());
		assertEquals(2, listColl.size());
		assertFalse(listColl.remove(true));
	}

	@Test
	public void containsTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		assertFalse(listColl.contains("Ivica"));

		listColl.add("Ivica");
		assertTrue(listColl.contains("Ivica"));
		assertFalse(listColl.contains("Marica"));

		listColl.add("Marica");
		assertTrue(listColl.contains("Ivica"));
		assertTrue(listColl.contains("Marica"));

		assertTrue(listColl.remove("Ivica"));
		assertFalse(listColl.contains("Ivica"));
		assertTrue(listColl.contains("Marica"));
		
		listColl.add("Ivica");
		listColl.add("suma");
		assertTrue(listColl.contains("Ivica"));
		assertTrue(listColl.contains("Marica"));
		assertTrue(listColl.contains("suma"));
	}

	@Test
	public void containsNull() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		assertFalse(listColl.contains(null));

		listColl.add("Ivica");
		assertFalse(listColl.contains(null));

		assertTrue(listColl.remove("Ivica"));
		assertFalse(listColl.contains(null));
	}

	@Test
	public void toArrayTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();

		int a = 42;
		String b = "Ivica";
		boolean c = true;
		listColl.add(a);
		listColl.add(b);
		listColl.add(c);
		Object[] array = listColl.toArray();

		assertEquals(a, array[0]);
		assertEquals(b, array[1]);
		assertEquals(c, array[2]);

		assertEquals(42, array[0]);
		assertEquals("Ivica", array[1]);
		assertEquals(true, array[2]);
		
		assertTrue(listColl.remove((Integer) 42));
		array = listColl.toArray();
		assertEquals("Ivica", array[0]);
		assertEquals(true, array[1]);
		
		assertTrue(listColl.remove("Ivica"));
		array = listColl.toArray();
		assertEquals(true, array[0]);
	}

	@Test
	public void clearTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		listColl.add(42);
		listColl.add("Ivica");
		listColl.add(true);

		listColl.clear();
		assertTrue(listColl.isEmpty());
		assertEquals(0, listColl.size());
		
		listColl.clear();
		assertTrue(listColl.isEmpty());
		assertEquals(0, listColl.size());
		
		listColl.add("Ivica");
		
		listColl.clear();
		assertTrue(listColl.isEmpty());
		assertEquals(0, listColl.size());
		
	}

	@Test
	public void getTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		
		listColl.add(42);
		listColl.add("Ivica");
		listColl.add(true);

		assertEquals(42, listColl.get(0));
		assertEquals("Ivica", listColl.get(1));
		assertEquals(true, listColl.get(2));
		
		try {
			listColl.get(-1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e){
			
		}
		
		try {
			listColl.get(listColl.size());
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e){
			
		}
		
	}

	@Test
	public void insertTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();

		listColl.add(42);
		listColl.add("Ivica");
		listColl.add(true);

		listColl.insert('s', 1);
		
		listColl.insert('z', listColl.size());

		assertEquals(42, listColl.get(0));
		assertEquals('s', listColl.get(1));
		assertEquals("Ivica", listColl.get(2));
		assertEquals(true, listColl.get(3));
		assertEquals('z', listColl.get(4));
		
		try {
			listColl.insert("junk", -1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e){
			
		}
		
		try {
			listColl.insert("junk", listColl.size() + 1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e){
			
		}
	}
	
	@Test
	public void indexOfTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		assertEquals(-1, listColl.indexOf("junk"));
		
		listColl.add(42);
		listColl.add("Ivica");
		listColl.add(true);

		assertEquals(0, listColl.indexOf(42));
		assertEquals(1, listColl.indexOf("Ivica"));
		assertEquals(2, listColl.indexOf(true));
		
		listColl.insert("Marica", 1);
		
		assertEquals(0, listColl.indexOf(42));
		assertEquals(1, listColl.indexOf("Marica"));
		assertEquals(2, listColl.indexOf("Ivica"));
		assertEquals(3, listColl.indexOf(true));
		
		listColl.add(true);
		
		assertEquals(3, listColl.indexOf(true));
		
		listColl.insert(true, 0);
		
		assertEquals(0, listColl.indexOf(true));
		
		assertEquals(-1, listColl.indexOf("suma"));
		assertEquals(-1, listColl.indexOf(null));
		
	}
	
	@Test
	public void removeIndexTest() {
		LinkedListIndexedCollection listColl = new LinkedListIndexedCollection();
		
		listColl.add(42);
		listColl.add("Ivica");
		listColl.add(true);
		listColl.add("Marica");

		listColl.remove(1);
		
		assertEquals(0, listColl.indexOf(42));
		assertEquals(-1, listColl.indexOf("Ivica"));
		assertEquals(1, listColl.indexOf(true));
		assertEquals(2, listColl.indexOf("Marica"));
		
		try {
			listColl.get(-1);
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e){
			
		}
		
		try {
			listColl.get(listColl.size());
			fail("Should throw exception");
		} catch (IndexOutOfBoundsException e){
			
		}
	}
	
	@Test
	public void constructorsTest() {
		ArrayIndexedCollection arrayColl = new ArrayIndexedCollection();
		
		arrayColl.add(42);
		arrayColl.add("Ivica");
		arrayColl.add(true);
		arrayColl.add("Marica");

		LinkedListIndexedCollection a = new LinkedListIndexedCollection(arrayColl);
		
		assertEquals(0, a.indexOf(42));
		assertEquals(1, a.indexOf("Ivica"));
		assertEquals(2, a.indexOf(true));
		assertEquals(3, a.indexOf("Marica"));
		assertEquals(4, a.size());
	}

}