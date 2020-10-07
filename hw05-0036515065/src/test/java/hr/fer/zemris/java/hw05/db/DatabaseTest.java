package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class DatabaseTest {
	@Test
	public void recordEqualsTest() {
		StudentRecord rec1 = new StudentRecord("36", "Ratko", "Milić", 4);
		StudentRecord rec2 = new StudentRecord("36", "Ratko", "Milić", 4);
		StudentRecord rec3 = new StudentRecord("364", "Ratko", "Milić", 4);
		StudentRecord rec4 = new StudentRecord("36", "Alan", "Ford", 5);

		assertEquals(rec1, rec2);
		assertNotEquals(rec1, rec3);
		assertEquals(rec1, rec4);
	}

	@Test
	public void databaseInputThrowTest() {
		List<String> lines = readDoc("src/test/resources/databaseTests/duplicateJMBAG.txt");

		try {
			StudentDatabase database = new StudentDatabase(lines);
			fail("Should throw");
		} catch (IllegalArgumentException ex) {
		}

		lines = readDoc("src/test/resources/databaseTests/invalidGrade1.txt");

		try {
			StudentDatabase database = new StudentDatabase(lines);
			fail("Should throw");
		} catch (IllegalArgumentException ex) {
		}

		lines = readDoc("src/test/resources/databaseTests/invalidGrade2.txt");

		try {
			StudentDatabase database = new StudentDatabase(lines);
			fail("Should throw");
		} catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void forJmbagTest() {
		List<String> lines = readDoc("src/test/resources/database.txt");
		StudentDatabase database = new StudentDatabase(lines);

		StudentRecord actual = database.forJMBAG("0000000013");
		StudentRecord expected = new StudentRecord("0000000013", "Gagić", "Mateja", 2);

		assertEquals(expected, actual);

		actual = database.forJMBAG("2");
		assertEquals(null, actual);
	}

	@Test
	public void filterTest() {
		List<String> text = readDoc("src/test/resources/database.txt");
		StudentDatabase database = new StudentDatabase(text);

		List<StudentRecord> records = database.filter(record -> true);
		assertEquals(text.size(), records.size());

		records = database.filter(record -> false);
		assertEquals(0, records.size());
	}

	@Test
	public void lessTest() {
		IComparisonOperator oper = ComparisonOperators.LESS;

		assertTrue(oper.satisfied("Less", "greater"));
		assertFalse(oper.satisfied("greater", "Less"));
		assertFalse(oper.satisfied("Less", "Less"));

		assertTrue(oper.satisfied("Ana", "Jasna"));
	}

	@Test
	public void lessEqualsTest() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;

		assertTrue(oper.satisfied("Less", "greater"));
		assertFalse(oper.satisfied("greater", "Less"));
		assertTrue(oper.satisfied("Less", "Less"));

		assertTrue(oper.satisfied("Ana", "Jasna"));
	}

	@Test
	public void greaterTest() {
		IComparisonOperator oper = ComparisonOperators.GREATER;

		assertFalse(oper.satisfied("Less", "greater"));
		assertTrue(oper.satisfied("greater", "Less"));
		assertFalse(oper.satisfied("Less", "Less"));

		assertFalse(oper.satisfied("Ana", "Jasna"));
	}

	@Test
	public void greaterEqualsTest() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;

		assertFalse(oper.satisfied("Less", "greater"));
		assertTrue(oper.satisfied("greater", "Less"));
		assertTrue(oper.satisfied("Less", "Less"));

		assertFalse(oper.satisfied("Ana", "Jasna"));
	}

	@Test
	public void equalsTest() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;

		assertFalse(oper.satisfied("Less", "greater"));
		assertFalse(oper.satisfied("greater", "Less"));
		assertTrue(oper.satisfied("Less", "Less"));

		assertFalse(oper.satisfied("Ana", "Jasna"));
	}

	@Test
	public void notEqualsTest() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;

		assertTrue(oper.satisfied("Less", "greater"));
		assertTrue(oper.satisfied("greater", "Less"));
		assertFalse(oper.satisfied("Less", "Less"));

		assertTrue(oper.satisfied("Ana", "Jasna"));
	}

	@Test
	public void likeTest() {
		IComparisonOperator oper = ComparisonOperators.LIKE;

		assertFalse(oper.satisfied("Zagreb", "Aba*"));
		assertFalse(oper.satisfied("AAA", "AA*AA"));
		assertTrue(oper.satisfied("AAAA", "AA*AA"));

		assertTrue(oper.satisfied("AAA", "AAA*"));
		assertTrue(oper.satisfied("Abc", "Abc*"));
		assertTrue(oper.satisfied("Abcdefgh", "Abc*"));

		assertFalse(oper.satisfied("Ana", "Jasna"));

		assertFalse(oper.satisfied("Brezović", "Be*"));
		assertTrue(oper.satisfied("Bosnić", "B*"));
		assertTrue(oper.satisfied("Bakamović", "B*"));
		assertTrue(oper.satisfied("Božić", "B*"));
		assertTrue(oper.satisfied("Brezović", "B*"));
		assertTrue(oper.satisfied("Operatoratori", "Opera*tori"));

	}

	@Test
	public void fieldGetterTest() {
		StudentRecord rec = new StudentRecord("42", "Šegrt", "Hlapić", 3);

		assertEquals("42", FieldValueGetters.JMBAG.get(rec));
		assertEquals("Šegrt", FieldValueGetters.LAST_NAME.get(rec));
		assertEquals("Hlapić", FieldValueGetters.FIRST_NAME.get(rec));
	}

	@Test
	public void conditionalExpressionTest() {
		List<String> text = readDoc("src/test/resources/database.txt");
		StudentDatabase database = new StudentDatabase(text);

		ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*",
				ComparisonOperators.LIKE);

		StudentRecord recordBos = database.forJMBAG("0000000003");
		StudentRecord notBos = database.forJMBAG("0000000010");

		boolean recordSatisfies = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(recordBos),
				expr.getStringLiteral());

		boolean recordNotSatisfies = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(notBos),
				expr.getStringLiteral());

		assertTrue(recordSatisfies);
		assertFalse(recordNotSatisfies);
	}

	@Test
	public void queryParserTest() {
		QueryParser qp1 = new QueryParser(" jmbag       =\"0123456789\"    ");
		assertTrue(qp1.isDirectQuery());
		assertEquals("0123456789", qp1.getQueriedJMBAG());
		assertEquals(1, qp1.getQuery().size());

		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		assertFalse(qp2.isDirectQuery());
		assertEquals(2, qp2.getQuery().size());

		try {
			System.out.println(qp2.getQueriedJMBAG()); // would throw!
			fail("Should throw");
		} catch (IllegalStateException ex) {
		}
	}

	@Test
	public void queryJmbagTest() {
		List<String> text = readDoc("src/test/resources/database.txt");
		StudentDatabase db = new StudentDatabase(text);

		QueryParser parser = new QueryParser("jmbag=\"0000000003\"");
		if (parser.isDirectQuery()) {
			StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
			assertEquals("Bosnić", r.getLastName());
			assertEquals("Andrea", r.getFirstName());
			assertEquals(4, r.getFinalGrade());
		} else {
			fail();
		}
	}

	@Test
	public void queryLastNameTest() {
		List<String> text = readDoc("src/test/resources/database.txt");
		StudentDatabase db = new StudentDatabase(text);

		QueryParser parser = new QueryParser("   lastName    =   \"Blažić\"");
		if (parser.isDirectQuery()) {
			fail();
		} else {
			List<StudentRecord> records = new ArrayList<>();
			for (StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
				records.add(r);
			}
			assertEquals(0, records.size());
		}

		QueryParser parser2 = new QueryParser("  lastName    =   \"Katunarić\"");
		if (parser2.isDirectQuery()) {
			fail();
		} else {
			List<StudentRecord> records = new ArrayList<>();
			for (StudentRecord r : db.filter(new QueryFilter(parser2.getQuery()))) {
				records.add(r);
			}

			assertEquals(1, records.size());
			StudentRecord r = records.get(0);
			assertEquals("0000000026", r.getJmbag());
			assertEquals("Katunarić", r.getLastName());
			assertEquals("Zoran", r.getFirstName());
			assertEquals(3, r.getFinalGrade());
		}
	}

	@Test
	public void queryLegalExamplesTest() {
		List<String> text = readDoc("src/test/resources/database.txt");
		StudentDatabase db = new StudentDatabase(text);

		QueryParser parser = new QueryParser("firstName>\"A\" and lastName LIKE \"B*ć\"");
		if (parser.isDirectQuery()) {
			fail();
		} else {
			List<StudentRecord> records = new ArrayList<>();
			for (StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
				records.add(r);
			}

			StudentRecord rec1 = records.get(0);
			StudentRecord rec2 = records.get(1);
			StudentRecord rec3 = records.get(2);
			StudentRecord rec4 = records.get(3);

			assertEquals("0000000002", rec1.getJmbag());
			assertEquals("0000000003", rec2.getJmbag());
			assertEquals("0000000004", rec3.getJmbag());
			assertEquals("0000000005", rec4.getJmbag());
		}

		parser = new QueryParser(
				"firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		if (parser.isDirectQuery()) {
			fail();
		} else {
			List<StudentRecord> records = new ArrayList<>();
			for (StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
				records.add(r);
			}

			StudentRecord rec1 = records.get(0);

			assertEquals("0000000003", rec1.getJmbag());
		}
	}

	private List<String> readDoc(String file) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.out.println("Unable to read document");
		}
		return lines;
	}

}
