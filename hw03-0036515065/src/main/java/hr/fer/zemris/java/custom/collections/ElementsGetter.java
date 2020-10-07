package hr.fer.zemris.java.custom.collections;

/**
 * Implementations of this interface can return elements one by one from a
 * collection to the user.
 * 
 * @author Jura Milić
 *
 */
public interface ElementsGetter {
	/**
	 * Checks if next element exists in collection.
	 * 
	 * @return
	 */
	boolean hasNextElement();

	/**
	 * Returns the next element that was not yet returned.
	 * 
	 * @return element
	 */
	Object getNextElement();
	
	default void processRemaining(Processor p) {
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
