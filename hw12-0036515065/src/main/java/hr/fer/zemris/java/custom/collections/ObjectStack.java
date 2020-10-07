package hr.fer.zemris.java.custom.collections;

/**
 * Stack storage with LIFO (Last In First Out) functionality. Stack can hold
 * deuplicate Objects, but null values are not allowed.
 * 
 * @author Jura Milić
 *
 */
public class ObjectStack {

	/**
	 * Collection used to store the elements.
	 */
	private ArrayIndexedCollection arrayColl;

	/**
	 * Constructs a new Stack.
	 */
	public ObjectStack() {
		arrayColl = new ArrayIndexedCollection();
	}

	/**
	 * Checks if Stack contains no elements.
	 * 
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return arrayColl.isEmpty();
	}

	/**
	 * Checks the number of elements in Stack.
	 * 
	 * @return number of elements
	 */
	public int size() {
		return arrayColl.size();
	}

	/**
	 * Given element is added into Stack.
	 * 
	 * @param value given element
	 */
	public void push(Object value) {
		arrayColl.add(value);
	}

	/**
	 * Checks the last element added to Stack and removes it from Stack.
	 * 
	 * @return last added element
	 */
	public Object pop() {
		if (arrayColl.isEmpty()) {
			throw new EmptyStackException();
		}

		Object value = arrayColl.get(size() - 1);
		arrayColl.remove(size() - 1);
		return value;
	}

	/**
	 * Checks the last element added to Stack
	 * 
	 * @return last added element
	 */
	public Object peek() {
		if (arrayColl.isEmpty()) {
			throw new EmptyStackException();
		}

		return arrayColl.get(size() - 1);
	}

	public void clear() {
		arrayColl.clear();
	}
}
