package hr.fer.zemris.java.custom.collections;

/**
 * Template for Collections	 that have the List functionality.
 * 
 * @author Jura Milić
 *
 */
public interface List extends Collection {
	/**
	 * Returns the value found at given index. Index must be in range [0,size-1].
	 * 
	 * @param index given index
	 * @return value found at index
	 */
	Object get(int index);

	/**
	 * Inserts given object into List. All elements indexed greater than the
	 * given index are shifted by one position towards the greater values. Index
	 * must be in range [0,size]
	 * 
	 * @param value    given object
	 * @param position given index
	 */
	void insert(Object value, int position);

	/**
	 * Searches for the first occurrence of given value within List and
	 * returns its index.
	 * 
	 * @param value given value
	 * @return -1 if object was not found or given object is null, otherwise return
	 *         its index inside the array
	 */
	int indexOf(Object value);

	/**
	 * Removes object located at given index from List. All elements indexed
	 * greater than the given index are shifted by one position towards the smaller
	 * values. Index must be in range [0,size-1]
	 * 
	 * @param index given index
	 */
	void remove(int index);
}
