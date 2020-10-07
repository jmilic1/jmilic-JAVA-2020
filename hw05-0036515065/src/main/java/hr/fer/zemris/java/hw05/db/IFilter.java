package hr.fer.zemris.java.hw05.db;

/**
 * Basic interface for checking if a StudentRecord satisfies an implemented
 * condition.
 * 
 * @author Jura Milić
 *
 */
public interface IFilter {
	/**
	 * Checks if Filter can accept the given record
	 * 
	 * @param record given record
	 * @return true if record was accepted, false otherwise
	 */
	public boolean accepts(StudentRecord record);
}
