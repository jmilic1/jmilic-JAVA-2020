package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A database used for storing and extracting StudentRecords. Database offers a
 * O(1) search for a StudentRecord with a given JMBAG. Duplicate JMBAGs are not
 * allowed. Valid values for grades are in range [2,5].
 * 
 * @author Jura Milić
 *
 */
public class StudentDatabase {
	/**
	 * List of StudentRecords
	 */
	private List<StudentRecord> list;
	/**
	 * Map used for storing the index of every StudentRecord.
	 */
	private HashMap<String, Integer> map;

	/**
	 * Constructs a StudentDatabase with given list of Strings. Every String
	 * contains one StudentRecord that needs to be read and added to database.
	 * 
	 * @param objects given list of Strings
	 * @throws IllegalArgumentException if there is an invalid number of fields in a
	 *                                  String, there are duplicate JMBAGs or given
	 *                                  grade is invalid
	 */
	public StudentDatabase(List<String> objects) {
		list = new ArrayList<>();
		map = new HashMap<>();

		for (String line : objects) {
			String[] data = line.split("\\s+");
			if (data.length < 4 || data.length > 5)
				throw new IllegalArgumentException("Invalid number of fields.");

			String name = "";
			String lastName = "";
			int grade;

			if (data.length == 5) {
				lastName = data[1] + " " + data[2];
				name = data[3];
				grade = Integer.parseInt(data[4]);
			} else {
				lastName = data[1];
				name = data[2];
				grade = Integer.parseInt(data[3]);
			}

			if (grade < 2 || grade > 5)
				throw new IllegalArgumentException("Invalid grade.");

			StudentRecord record = new StudentRecord(data[0], lastName, name, grade);
			if (list.contains(record))
				throw new IllegalArgumentException();
			list.add(record);
			map.put(record.getJmbag(), list.size() - 1);
		}
	}

	/**
	 * Returns a StudentRecord with given JMBAG
	 * 
	 * @param string given JMBAG
	 * @return found StudentRecord
	 */
	public StudentRecord forJMBAG(String string) {
		try {
			return list.get(map.get(string));
		} catch (NullPointerException ex) {
			return null;
		}

	}

	/**
	 * Filters through all StudentRecords in database using given filter. Accepted
	 * StudentRecords are put into a list.
	 * 
	 * @param filter given filter
	 * @return list of filtered StudentRecords
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> filteredList = new ArrayList<>();

		for (StudentRecord record : list) {
			if (filter.accepts(record))
				filteredList.add(record);
		}
		return filteredList;
	}
}
