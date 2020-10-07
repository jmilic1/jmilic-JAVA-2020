package hr.fer.zemris.java.custom.collections;

/**
 * Base template for processing a variety of different objects based on
 * implementation.
 * 
 * @author Jura Milić
 */
public interface Processor<T> {
	/**
	 * Method template for processing.
	 * 
	 * @param value object to be processed
	 */
	void process(T value);
}