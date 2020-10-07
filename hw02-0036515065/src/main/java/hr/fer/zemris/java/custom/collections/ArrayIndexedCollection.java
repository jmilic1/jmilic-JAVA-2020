package hr.fer.zemris.java.custom.collections;

/**
 * A collection which uses a single array as storage. Collection can store
 * multiple duplicates of the same object and doesn't store null references.
 * 
 * @author Jura Milić
 *
 */
public class ArrayIndexedCollection extends Collection {
	/**
	 * The number of elements in the collection.
	 */
	private int size;

	/**
	 * An array of elements in which content is stored.
	 */
	private Object[] elements;

	/**
	 * Constructs an ArrayIndexedCollection with array size set to the given
	 * capacity and add copies of content from given collection into it.
	 * 
	 * @param other           given collection
	 * @param initialCapacity given capacity
	 * @throws NullPointerException if given collection is null
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other == null) {
			throw new NullPointerException();
		}

		if (initialCapacity < 1) {
			throw new IllegalArgumentException();
		}

		this.elements = new Object[Math.max(other.size(), initialCapacity)];
		size = 0;

		addAll(other);
	}

	/**
	 * Constructs an ArrayIndexedCollection by copying the content from given
	 * collection. Array size of constructed ArrayIndexedCollection is set to the
	 * size of given collection.
	 * 
	 * @param other given collection
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, 1);
	}

	/**
	 * Constructs an empty ArrayIndexedCollection with array size of the given
	 * capacity.
	 * 
	 * @param initialCapacity given capacity
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		
		size = 0;
		elements = new Object[initialCapacity];
	}

	/**
	 * Constructs an empty ArrayIndexedCollection with array size set to 16.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}

	public int size() {
		return size;
	}

	/**
	 * @throws NullPointerException if given value is null
	 */
	public void add(Object value) {
		if (value == null) {
			throw new NullPointerException();
		}

		if (size >= elements.length) {
			ArrayIndexedCollection arrayColl = new ArrayIndexedCollection(this, size * 2);
			elements = arrayColl.elements;
		}
		elements[size] = value;
		size++;

	}

	public boolean contains(Object value) {
		if (indexOf(value) == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Removes one occurrence of given object from collection. All elements indexed
	 * greater than the given object are shifted by one position towards the smaller
	 * values.
	 * 
	 * @param value given object
	 * @return true if object is in collection, false otherwise, as determined by
	 *         equals method
	 */
	public boolean remove(Object value) {
		int i = indexOf(value);

		if (i == -1) {
			return false;
		}
		
		remove(i);
		return true;
	}

	public Object[] toArray() {
		Object[] array = new Object[size];

		for (int i = 0; i < size; i++) {
			array[i] = elements[i];
		}
		return array;
	}

	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(elements[i]);
		}
	}

	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}

		size = 0;
	}

	/**
	 * Checks if given integer is within given range [min,max].
	 * 
	 * @param arg given integer
	 * @param min allowed lowest value
	 * @param max allowed highest value
	 * @throws IndexOutOfBoundsException if integer is not in range
	 */
	private void checkRange(int arg, int min, int max) {
		if (arg < min || arg > max) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Returns the object located at index of array. Index must be in range
	 * [0,size-1]
	 * 
	 * @param index location within array
	 * @return found object
	 */
	public Object get(int index) {
		checkRange(index, 0, size - 1);

		return elements[index];
	}

	/**
	 * Inserts given object into index of array. All elements indexed greater than
	 * the given index are shifted by one position towards the greater values. Index
	 * must be in range [0,size]
	 * 
	 * @param value    given object
	 * @param position given index
	 * @throws NullPointerException thrown if value is null
	 */
	public void insert(Object value, int position) {
		checkRange(position, 0, size);

		if (value == null) {
			throw new NullPointerException();
		}

		add(elements[size - 1]);

		for (int i = size - 1; i > position; i--) {
			elements[i] = elements[i - 1];
		}

		elements[position] = value;
	}

	/**
	 * Searches for the first occurrence of given value within Collection and
	 * returns its index.
	 * 
	 * @param value given value
	 * @return -1 if object was not found or given object is null, otherwise return
	 *         its index inside the array
	 */
	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		int i = 0;

		while (i != size) {
			if (elements[i].equals(value)) {
				return i;
			} else {
				i++;
			}
		}
		return -1;
	}

	/**
	 * Removes object located at given index from Collection. All elements indexed
	 * greater than the given index are shifted by one position towards the smaller
	 * values. Index must be in range [0,size-1]
	 * 
	 * @param index given index
	 */
	public void remove(int index) {
		checkRange(index, 0, size - 1);

		while (index < size - 1) {
			elements[index] = elements[index + 1];
			index++;
		}

		elements[size - 1] = null;
		size--;
	}
}