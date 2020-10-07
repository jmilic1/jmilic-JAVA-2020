package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * A Collection that is also a List which uses a single array as storage.
 * Collection can store multiple duplicates of the same object and doesn't store
 * null references.
 * 
 * @author Jura Milić
 *
 */
public class ArrayIndexedCollection implements List {
	/**
	 * The number of elements in the Collection.
	 */
	private int size;

	/**
	 * An array of elements in which content is stored.
	 */
	private Object[] elements;

	/**
	 * Stores number of times the Collection was modified. Used for ElementsGetter
	 * of ArrayIndexedCollection.
	 */
	private long modificationCount;

	/**
	 * An implementation of ElementsGetter. Goes through elements of an
	 * ArrayIndexedCollection indexed from 0 to size-1 in that order.
	 * 
	 * @author Jura Milić
	 *
	 */
	private static class ArrayGetter implements ElementsGetter {
		/**
		 * The Collection whose elements are being returned.
		 */
		private ArrayIndexedCollection coll;

		/**
		 * The index of the next element to be returned.
		 */
		private int position;

		/**
		 * Number of modifications the Collection went through when constructing the
		 * ElementsGetter.
		 */
		private long savedModificationCount;

		/**
		 * Constructs an ArrayGetter by setting the position and setting a reference to
		 * the array of elements of a given ArrayIndexedCollection.
		 * 
		 * @param collection given ArrayIndexedCollection
		 */
		public ArrayGetter(ArrayIndexedCollection collection) {
			coll = collection;
			savedModificationCount = collection.modificationCount;
		}

		/**
		 * @throws ConcurrentModificationException if the Collection which is being
		 *                                         cycled through by this ElementGetter
		 *                                         was modified after constructing the
		 *                                         ElementGetter
		 */
		public boolean hasNextElement() {
			if (savedModificationCount == coll.modificationCount) {
				try {
					coll.get(position);
					return true;
				} catch (IndexOutOfBoundsException ex) {
					return false;
				}
			}
			throw new ConcurrentModificationException(
					"The getter's collection was modified after the getter was constructed.");
		}

		/**
		 * @throws NoSuchElementException if there are no more elements to return.
		 */
		public Object getNextElement() {
			if (hasNextElement()) {
				return coll.get(position++);
			} else {
				throw new NoSuchElementException("The getter was called while it was empty.");
			}
		}
	}

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

		modificationCount = 0;
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

		modificationCount = 0;
	}

	/**
	 * Constructs an empty ArrayIndexedCollection with array size set to 16.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}

	public ElementsGetter createElementsGetter() {
		return new ArrayGetter(this);
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
		modificationCount++;
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

	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}

		size = 0;
		modificationCount++;
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

	public Object get(int index) {
		checkRange(index, 0, size - 1);

		return elements[index];
	}

	/**
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

	public void remove(int index) {
		checkRange(index, 0, size - 1);

		while (index < size - 1) {
			elements[index] = elements[index + 1];
			index++;
		}

		elements[size - 1] = null;
		size--;
		modificationCount++;
	}
}