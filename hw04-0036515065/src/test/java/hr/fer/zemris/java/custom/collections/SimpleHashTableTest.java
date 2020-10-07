package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class SimpleHashTableTest {

	@Test
	void testConstruct() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(4);
		assertEquals(0, table.size());

		SimpleHashtable<String, Integer> table2 = new SimpleHashtable<>();
		assertEquals(0, table2.size());
	}

	@Test
	void getKeyTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(4);

		SimpleHashtable<String, Integer> table2 = new SimpleHashtable<>();

		table.put("Ivana", 2);
		table2.put("Ivana", 2);

		assertEquals(2, table.get("Ivana"));
		assertEquals(2, table2.get("Ivana"));

		table.put("Ante", 2);
		table2.put("Ante", 2);

		assertEquals(2, table.get("Ante"));
		assertEquals(2, table2.get("Ante"));

		table.put("Jasna", 2);
		table2.put("Jasna", 2);

		assertEquals(2, table.get("Jasna"));
		assertEquals(2, table2.get("Jasna"));

		table.put("Kristina", 5);
		table2.put("Kristina", 5);

		assertEquals(5, table.get("Kristina"));
		assertEquals(5, table2.get("Kristina"));

		table.put("Ivana", 5);
		table2.put("Ivana", 5);

		assertEquals(5, table.get("Ivana"));
		assertEquals(5, table2.get("Ivana"));
	}

	@Test
	void sizeTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

		assertEquals(0, table.size());
		table.put("Ivana", 2);
		assertEquals(1, table.size());
		table.put("Ante", 2);
		assertEquals(2, table.size());
		table.put("Jasna", 2);
		assertEquals(3, table.size());
		table.put("Kristina", 5);
		assertEquals(4, table.size());
		table.put("Ivana", 5);
		assertEquals(4, table.size());

		table.remove("Ivana");
		assertEquals(3, table.size());
		table.remove("Kristina");
		assertEquals(2, table.size());
		table.remove("Jasna");
		assertEquals(1, table.size());
		table.remove("Ante");
		assertEquals(0, table.size());
	}

	@Test
	void containsKeyTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

		assertFalse(table.containsKey("Ivana"));
		table.put("Ivana", 2);
		assertTrue(table.containsKey("Ivana"));
		table.put("Ante", 2);
		assertTrue(table.containsKey("Ivana"));
		assertTrue(table.containsKey("Ante"));
		table.put("Jasna", 2);
		assertTrue(table.containsKey("Ivana"));
		assertTrue(table.containsKey("Ante"));
		assertTrue(table.containsKey("Jasna"));
		table.put("Ivana", 5);
		assertTrue(table.containsKey("Ivana"));
		assertTrue(table.containsKey("Ante"));
		assertTrue(table.containsKey("Jasna"));
		table.remove("Ivana");
		assertFalse(table.containsKey("Ivana"));
		assertTrue(table.containsKey("Ante"));
		assertTrue(table.containsKey("Jasna"));
		table.remove("Ante");
		assertFalse(table.containsKey("Ivana"));
		assertFalse(table.containsKey("Ante"));
		assertTrue(table.containsKey("Jasna"));
		table.remove("Jasna");
		assertFalse(table.containsKey("Ivana"));
		assertFalse(table.containsKey("Ante"));
		assertFalse(table.containsKey("Jasna"));
	}

	@Test
	void containsValueTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();

		assertFalse(table.containsValue(2));
		table.put("Ivana", 2);
		assertTrue(table.containsValue(2));
		table.put("Ante", 2);
		assertTrue(table.containsValue(2));
		table.put("Kristina", 5);
		assertTrue(table.containsValue(2));
		assertTrue(table.containsValue(5));
		table.put("Ivana", 5);
		assertTrue(table.containsValue(2));
		assertTrue(table.containsValue(5));
		table.put("Ante", 5);
		assertFalse(table.containsValue(2));
		assertTrue(table.containsValue(5));
		table.remove("Ante");
		assertTrue(table.containsValue(5));
		table.remove("Ivana");
		assertTrue(table.containsValue(5));
		table.remove("Kristina");
		assertFalse(table.containsValue(5));
	}

	@Test
	void removeTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(4);

		table.remove("Ivana");
		table.put("Ivana", 2);
		table.put("Ante", 2);
		table.put("Jasna", 2);
		table.put("Kristina", 5);
		table.remove("Ivana");
		assertEquals(3, table.size());
		table.remove("Ante");
		assertEquals(2, table.size());
		table.remove("Jasna");
		assertEquals(1, table.size());
		table.remove("Kristina");
		assertEquals(0, table.size());

		assertFalse(table.containsKey("Ivana"));
		assertFalse(table.containsKey("Ante"));
		assertFalse(table.containsKey("Jasna"));
		assertFalse(table.containsKey("Kristina"));

		table.put("Ivana", 2);
		table.put("Ante", 2);
		table.put("Jasna", 2);
		table.put("Kristina", 5);

		table.remove("Jasna");
		assertEquals(3, table.size());
		table.remove("Kristina");
		assertEquals(2, table.size());
	}

	@Test
	void toStringTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(4);
		table.put("Ivana", 2);
		assertEquals("[Ivana=2]", table.toString());

		table.put("Ivana", 3);
		assertEquals("[Ivana=3]", table.toString());

		table.put("Kristina", 3);
		assertEquals("[Ivana=3, Kristina=3]", table.toString());

		table.put("Ante", 5);
		assertEquals("[Ante=5, Ivana=3, Kristina=3]", table.toString());
	}

	@Test
	void allocationTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(8);

		table.put("Ivana", 2);
		table.put("Ante", 2);
		table.put("Kristina", 5);
		table.put("Mateja", 6);
		table.put("Ivan", 3);

		String str1 = table.toString();
		table.put("Kornati", 44);
		table.remove("Kornati");
		String str2 = table.toString();

		assertNotEquals(str1, str2);
	}

	@Test
	void iteratorExample1Test() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		StringBuilder sb = new StringBuilder();
		for (SimpleHashtable.TableEntry<String, Integer> pair : examMarks) {
			sb.append(pair.getKey() + " => " + pair.getValue() + "\n");
		}

		assertEquals("Ante => 2\nIvana => 5\nJasna => 2\nKristina => 5\n", sb.toString());

	}

	@Test
	public void iteratorExample2Test() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		StringBuilder sb = new StringBuilder();
		for (SimpleHashtable.TableEntry<String, Integer> pair1 : examMarks) {
			for (SimpleHashtable.TableEntry<String, Integer> pair2 : examMarks) {
				sb.append("(" + pair1.getKey() + " => " + pair1.getValue() + ") - (" + pair2.getKey() + " => "
						+ pair2.getValue() + ")\n");
			}
		}

		assertEquals("(Ante => 2) - (Ante => 2)\n" + "(Ante => 2) - (Ivana => 5)\n" + "(Ante => 2) - (Jasna => 2)\n"
				+ "(Ante => 2) - (Kristina => 5)\n" + "(Ivana => 5) - (Ante => 2)\n" + "(Ivana => 5) - (Ivana => 5)\n"
				+ "(Ivana => 5) - (Jasna => 2)\n" + "(Ivana => 5) - (Kristina => 5)\n" + "(Jasna => 2) - (Ante => 2)\n"
				+ "(Jasna => 2) - (Ivana => 5)\n" + "(Jasna => 2) - (Jasna => 2)\n" + "(Jasna => 2) - (Kristina => 5)\n"
				+ "(Kristina => 5) - (Ante => 2)\n" + "(Kristina => 5) - (Ivana => 5)\n"
				+ "(Kristina => 5) - (Jasna => 2)\n" + "(Kristina => 5) - (Kristina => 5)\n", sb.toString());

	}

	@Test
	public void iteratorExample3Test() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		assertTrue(examMarks.containsKey("Ivana"));
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			if (pair.getKey().equals("Ivana")) {
				iter.remove(); // sam iterator kontrolirano uklanja trenutni element}}
			}
		}
		assertFalse(examMarks.containsKey("Ivana"));
	}

	@Test
	public void iteratorExample4Test() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		assertTrue(examMarks.containsKey("Ivana"));
		try {
			Iterator<SimpleHashtable.TableEntry<String, Integer>> iter2 = examMarks.iterator();
			while (iter2.hasNext()) {
				SimpleHashtable.TableEntry<String, Integer> pair = iter2.next();
				if (pair.getKey().equals("Ivana")) {
					iter2.remove();
					iter2.remove();
				}
			}
			fail("Should throw");
		} catch (IllegalStateException ex) {

		}
		assertFalse(examMarks.containsKey("Ivana"));
	}

	@Test
	public void iteratorExample5Test() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		assertTrue(examMarks.containsKey("Ivana"));
		try {
			Iterator<SimpleHashtable.TableEntry<String, Integer>> iter3 = examMarks.iterator();
			while (iter3.hasNext()) {
				SimpleHashtable.TableEntry<String, Integer> pair = iter3.next();
				if (pair.getKey().equals("Ivana")) {
					examMarks.remove("Ivana");
				}
			}
			fail("Should throw");
		} catch (ConcurrentModificationException ex) {

		}
		assertFalse(examMarks.containsKey("Ivana"));

	}

	@Test
	public void iteratorExample6Test() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		examMarks.remove("Ivana");
		examMarks.put("Ivana", 2);

		StringBuilder sb = new StringBuilder();
		assertTrue(examMarks.containsKey("Ivana"));
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter4 = examMarks.iterator();
		while (iter4.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter4.next();
			sb.append(pair.getKey() + " => " + pair.getValue() + "\n");
			iter4.remove();
		}
		assertEquals("Ante => 2\n" + "Jasna => 2\n" + "Kristina => 5\n" + "Ivana => 2\n", sb.toString());
		assertEquals(0, examMarks.size());
	}

	@Test
	public void example() {
		// create collection:
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana

		// query collection:
		Integer kristinaGrade = examMarks.get("Kristina");
		assertEquals(5, kristinaGrade);

		// What is collection's size? Must be four!
		assertEquals(4, examMarks.size());
	}
}
