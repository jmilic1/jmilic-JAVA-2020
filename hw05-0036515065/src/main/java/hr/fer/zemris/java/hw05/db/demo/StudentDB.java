package hr.fer.zemris.java.hw05.db.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw05.db.ConditionalExpression;
import hr.fer.zemris.java.hw05.db.QueryFilter;
import hr.fer.zemris.java.hw05.db.QueryParser;
import hr.fer.zemris.java.hw05.db.QueryParserException;
import hr.fer.zemris.java.hw05.db.RecordFormatter;
import hr.fer.zemris.java.hw05.db.StudentDatabase;
import hr.fer.zemris.java.hw05.db.StudentRecord;

/**
 * Main program for using a database of students. The database input should be
 * stored in a txt in current directory. Program offers a query through console.
 * When entering "exit" into console, the program will be terminated. Valid
 * commands operators are <, <=, >, >=, =, !=, LIKE.
 * 
 * @author Jura Milić
 *
 */
public class StudentDB {
	/**
	 * Generates a database of student records and reads query commands
	 * 
	 * @param args not used
	 * @throws IOException if document could not be read
	 */
	public static void main(String[] args) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/main/resources/database.txt"), StandardCharsets.UTF_8);

		StudentDatabase db = new StudentDatabase(lines);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.printf("> ");
		String line = reader.readLine();

		while (!line.equals("exit")) {
			String[] str = line.split("\\s+");
			List<StudentRecord> records = null;

			if (str[0].equals("query")) {
				try {
					records = getRecordsBasedOnConditions(line.substring(5), db);
					String output = RecordFormatter.format(records);
					System.out.println(output);
				} catch (QueryParserException ex) {
					System.out.println("Invalid input.");
				}
			} else {
				System.out.println("Unkown command.");
			}
			System.out.printf("> ");
			line = reader.readLine();

		}
		System.out.println("Goodbye!");

	}

	/**
	 * Extracts student records from database based on input String
	 * 
	 * @param line given input
	 * @param db   database
	 * @return List of records
	 */
	private static List<StudentRecord> getRecordsBasedOnConditions(String line, StudentDatabase db) {
		QueryParser qp = new QueryParser(line);
		List<StudentRecord> records = new ArrayList<StudentRecord>();

		if (qp.isDirectQuery()) {
			System.out.println("Using index for record retrieval.");
			StudentRecord rec = db.forJMBAG(qp.getQueriedJMBAG());
			if (rec != null) {
				records.add(rec);
			}
		} else {
			List<ConditionalExpression> expressions = qp.getQuery();
			QueryFilter qf = new QueryFilter(expressions);
			try {
				records = db.filter(qf);
			} catch (IllegalArgumentException ex) {
				System.out.println("Too many wildcards were given");
			}
		}
		return records;
	}

}
