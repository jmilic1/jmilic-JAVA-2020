package hr.fer.zemris.java.custom.collections;

/**
 * Template for general collection of objects
 * 
 * @author Jura Milić
 * 
 * @param <T> type of data the Collection will store
 */
public interface Collection<T> {

	/**
	 * Checks if this collection contains no elements.
	 * 
	 * @return true if collection contains no objects, false otherwise.
	 */
	default boolean isEmpty() {
		if (this.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks number of stored objects in collection.
	 * 
	 * @return number objects
	 */
	int size();

	/**
	 * Adds the given object into the collection.
	 * 
	 * @param value given object
	 */
	void add(T value);

	/**
	 * Checks if the given object is in the collection.
	 * 
	 * @param value given object
	 * @return true if collection contains value, false otherwise.
	 */
	boolean contains(Object value);

	/**
	 * Removes one occurrence of given object from the collection.
	 * 
	 * @param value given object
	 * @return true if object is in collection, false otherwise
	 */
	boolean remove(Object value);

	/**
	 * Allocates new array with size equal to the size of collection and fills it
	 * with collection content.
	 * 
	 * @return new array
	 */
	Object[] toArray();

	/**
	 * Processes each element of collection using process of given processor
	 * 
	 * @param processor given processor
	 */
	default void forEach(Processor<? super T> processor) {
		ElementsGetter<T> getter = this.createElementsGetter();
		getter.processRemaining(processor);
	}

	/**
	 * Adds into current collection all content from the given collection. Other
	 * collection is unchanged. (thisCollection += otherCollection)
	 * 
	 * @param other given collection
	 */
	default void addAll(Collection<? extends T> other) {
		Processor<T> addProcessor = value -> add(value);

		other.forEach(addProcessor);
	}

	/**
	 * Removes all content from this collection.
	 */
	void clear();

	/**
	 * Creates an ElementsGetter for this collection.
	 * 
	 * @return new ElementsGetter
	 */
	ElementsGetter<T> createElementsGetter();

	/**
	 * Adds all elements into THIS Collection from given Collection that satisfy the
	 * given Tester.
	 * 
	 * @param col    given Collection
	 * @param tester given Tester
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		ElementsGetter<? extends T> getter = col.createElementsGetter();
		Processor<T> addProcessor = value -> {
			if (tester.test(value))
				this.add(value);
		};

		getter.processRemaining(addProcessor);

	}
}