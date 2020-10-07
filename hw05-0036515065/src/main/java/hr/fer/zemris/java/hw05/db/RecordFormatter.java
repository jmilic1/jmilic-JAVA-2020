package hr.fer.zemris.java.hw05.db;

import java.util.List;

/**
 * Used for formating a tidy output of records.
 * 
 * @author Jura Milić
 *
 */
public class RecordFormatter {
	/**
	 * Formats given record list into a readable output.
	 * 
	 * @param records given list
	 * @return String to be written to user.
	 */
	public static String format(List<StudentRecord> records) {
		StringBuilder sb = new StringBuilder();
		if (records.size() != 0) {
			sb.append(writeHorisontalBorder(records));
			sb.append(writeRows(records));
			sb.append(writeHorisontalBorder(records));
		}
		sb.append("Records selected: " + records.size() + "\n");
		return sb.toString();
	}

	/**
	 * Writes top or bottom part of output table according to longest Strings of
	 * each value for every field.
	 * 
	 * @param records given list of records
	 * @return String of ceiling or floor of table
	 */
	private static String writeHorisontalBorder(List<StudentRecord> records) {
		int maxJMBAG = getMax(records, FieldValueGetters.JMBAG);
		int maxLast = getMax(records, FieldValueGetters.LAST_NAME);
		int maxFirst = getMax(records, FieldValueGetters.FIRST_NAME);
		StringBuilder sb = new StringBuilder();

		sb.append("+");
		sb.append(writeEqualsSigns(maxJMBAG + 2));
		sb.append("+");
		sb.append(writeEqualsSigns(maxLast + 2));
		sb.append("+");
		sb.append(writeEqualsSigns(maxFirst + 2));
		sb.append("+");
		sb.append(writeEqualsSigns(3));
		sb.append("+\n");
		return sb.toString();
	}

	/**
	 * Writes a given amount of '=' characters
	 * 
	 * @param number given amount
	 * @return String of a row of '=' characters
	 */
	private static String writeEqualsSigns(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append(("="));
		}
		return sb.toString();
	}

	/**
	 * Writes rows for every record. Values are shifted according to longest value
	 * String for every field of given list of records
	 * 
	 * @param records given list
	 * @return String containing rows of records
	 */
	private static String writeRows(List<StudentRecord> records) {
		int maxJMBAG = getMax(records, FieldValueGetters.JMBAG);
		int maxLast = getMax(records, FieldValueGetters.LAST_NAME);
		int maxFirst = getMax(records, FieldValueGetters.FIRST_NAME);
		StringBuilder sb = new StringBuilder();

		for (StudentRecord record : records) {
			sb.append("| ");
			sb.append(record.getJmbag() + " | ");
			for (int i = 0; i < maxJMBAG - record.getJmbag().length(); i++) {
				sb.append(" ");
			}
			sb.append(record.getLastName());
			for (int i = 0; i < maxLast - record.getLastName().length(); i++) {
				sb.append(" ");
			}
			sb.append(" | ");
			sb.append(record.getFirstName());
			for (int i = 0; i < maxFirst - record.getFirstName().length(); i++) {
				sb.append(" ");
			}
			sb.append(" | " + record.getFinalGrade() + " |\n");
		}
		return sb.toString();
	}

	/**
	 * Returns the largest length of a given field from list of records
	 * 
	 * @param records given list
	 * @param getter  given value getter
	 * @return largest length
	 */
	private static int getMax(List<StudentRecord> records, IFieldValueGetter getter) {
		int max = 0;
		for (StudentRecord record : records) {
			max = Math.max(max, getter.get(record).length());
		}
		return max;
	}
}
