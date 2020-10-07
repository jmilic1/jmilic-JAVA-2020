package hr.fer.zemris.java.custom.collections;

/**
 * Tester interface. Tests if an object satisfies a condition.
 * 
 * @author Jura Milić
 *
 */
public interface Tester<T> {
	/**
	 * Tests if given object satisfies the implemented condition.
	 * 
	 * @param obj given object
	 * @return true if object satisfies the condition, false otherwise
	 */
	boolean test(T obj);
}
