package hr.fer.zemris.java.hw05.db;

/**
 * Basic interface for getting a value of a specific field of given
 * StudentRecord based on implementation.
 * 
 * @author Jura Milić
 *
 */
public interface IFieldValueGetter {
	/**
	 * Gets value from a certain field.
	 * 
	 * @param record given record
	 * @return value
	 */
	public String get(StudentRecord record);
}
