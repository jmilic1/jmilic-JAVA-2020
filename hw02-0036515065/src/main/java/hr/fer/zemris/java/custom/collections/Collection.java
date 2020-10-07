package hr.fer.zemris.java.custom.collections;

/**
 * Template for general collection of objects
 * 
 * @author Jura Milić
 *
 */
public class Collection {

	/**
	 * Checks if this collection contains no elements.
	 * 
	 * @return true if collection contains no objects, false otherwise.
	 */
	public boolean isEmpty() {
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
	public int size() {
		return 0;
	}

	/**
	 * Adds the given object into the collection.
	 * 
	 * @param value given object
	 */
	public void add(Object value) {

	}

	/**
	 * Checks if the given object is in the collection.
	 * 
	 * @param value given object
	 * @return true if collection contains value, false otherwise.
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes one occurrence of given object from the collection.
	 * 
	 * @param value given object
	 * @return true if object is in collection, false otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Allocates new array with size equal to the size of collection and fills it
	 * with collection content.
	 * 
	 * @return new array
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Processes each element of collection using process of given processor
	 * 
	 * @param processor given processor
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Adds into current collection all content from the given collection. Other
	 * collection is unchanged. (thisCollection += otherCollection)
	 * 
	 * @param other given collection
	 */
	public void addAll(Collection other) {

		class AddProcessor extends Processor {
			public void process(Object value) {
				add(value);
			}
		}
		AddProcessor addProcessor = new AddProcessor();

		other.forEach(addProcessor);
	}

	/**
	 * Removes all content from this collection.
	 */
	public void clear() {

	}

	protected Collection() {

	}
}