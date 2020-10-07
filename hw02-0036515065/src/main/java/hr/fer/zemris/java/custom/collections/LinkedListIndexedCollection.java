package hr.fer.zemris.java.custom.collections;

/**
 * A collection which uses Linked Lists as storage. Collection can store
 * multiple duplicates of the same object and doesn't store null references.
 * 
 * @author Jura Milić
 *
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * A node containing content.
	 */
	private static class ListNode {
		/**
		 * Reference to previous node
		 */
		ListNode prev;
		/**
		 * Reference to next node
		 */
		ListNode next;
		/**
		 * Stored content within a single node.
		 */
		Object value;
	}

	/**
	 * The number of elements in the collection.
	 */
	private int size;
	/**
	 * Reference to the first element in the collection.
	 */
	private ListNode first;
	/**
	 * Reference to the last element in the collection.
	 */
	private ListNode last;

	/**
	 * Constructs an empty LinkedListIndexedCollection.
	 */
	public LinkedListIndexedCollection() {
		size = 0;
		first = null;
		last = null;
	}

	/**
	 * Constructs a LinkedListIndexedCollection with content copied from given
	 * collection.
	 * 
	 * @param other given collection
	 * @throws NullPointerException if given collection is null
	 */
	public LinkedListIndexedCollection(Collection other) {
		if (other == null) {
			throw new NullPointerException();
		}
		addAll(other);
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

		insert(value, size);
	}

	public boolean contains(Object value) {
		if (indexOf(value) == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Removes given node from Collection. All elements indexed greater than the
	 * given node are shifted by one position towards the smaller values.
	 * 
	 * @param node given node
	 */
	private void remove(ListNode node) {
		if (node.prev != null) {
			node.prev.next = node.next;
		}
		if (node.next != null) {
			node.next.prev = node.prev;
		}
		if (first == node) {
			first = node.next;
		}
		if (last == node) {
			last = node.prev;
		}
		node = null;
		size--;
	}

	public boolean remove(Object value) {
		ListNode currentNode = first;

		while (currentNode != null) {
			if (currentNode.value == value) {
				remove(currentNode);
				return true;
			}
			currentNode = currentNode.next;
		}
		return false;
	}

	public Object[] toArray() {
		Object[] array = new Object[size];
		ListNode currentNode = first;

		for (int i = 0; i < size; i++) {
			array[i] = currentNode.value;
			currentNode = currentNode.next;
		}
		return array;
	}

	public void forEach(Processor processor) {
		ListNode currentNode = first;

		while (currentNode != null) {
			processor.process(currentNode.value);
			currentNode = currentNode.next;
		}
	}

	public void clear() {
		if (size == 0) {
			return;
		}

		ListNode currentNode = first;

		while (currentNode.next != null) {
			currentNode.value = null;
			currentNode = currentNode.next;
			currentNode.prev.next = null;
			currentNode.prev = null;
		}

		currentNode.value = null;
		first = null;
		last = null;
		size = 0;
	}

	/**
	 * Searches for the node given by index within the Collection with complexity
	 * O(2/n+1).
	 * 
	 * @param index given index
	 * @return found node
	 */
	private ListNode searchFor(int index) {
		ListNode currentNode;
		if (size / 2 > index) {
			currentNode = first;

			for (int i = 0; i < index; i++) {
				currentNode = currentNode.next;
			}
		} else {
			currentNode = last;

			for (int i = size - 1; i > index; i--) {
				currentNode = currentNode.prev;
			}
		}
		return currentNode;
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
	 * Returns the value found at given index. Index must be in range [0,size-1].
	 * 
	 * @param index given index
	 * @return value found at index
	 */
	public Object get(int index) {
		checkRange(index, 0, size - 1);

		return searchFor(index).value;
	}

	/**
	 * Inserts given object into Linked List. All elements indexed greater than the
	 * given index are shifted by one position towards the greater values. Index
	 * must be in range [0,size]
	 * 
	 * @param value    given object
	 * @param position given index
	 * @throws NullPointerException thrown if value is null
	 */
	public void insert(Object value, int position) {
		checkRange(position, 0, size);

		ListNode currentNode = searchFor(position - 1);

		if (currentNode == null) {
			currentNode = new ListNode();
			first = currentNode;
			last = currentNode;
			currentNode.value = value;
		} else {
			ListNode newNode = new ListNode();
			newNode.value = value;
			if (position == 0) {
				first = newNode;
				currentNode.prev = newNode;
				newNode.next = currentNode;
			} else {
				newNode.prev = currentNode;
			}
			if (position == size) {
				last = newNode;
				currentNode.next = newNode;
			} else {
				if (position != 0) {
				newNode.next = currentNode.next;
				}
			}
			if (position != 0) {
				currentNode.next = newNode;
			}
			
		}
		size++;

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
		
		if (first == null) {
			return -1;
		}
		
		ListNode currentNode = first;
		int index = 0;

		while (index < size - 1) {
			if (currentNode.value == value) {
				return index;
			}
			index++;
			currentNode = currentNode.next;
		}

		if (currentNode != null) {
			if (currentNode.value == value) {
				return index;
			}
		}

		return -1;
	}

	/**
	 * Removes object located at given index from Collection. Index must be in range
	 * [0,size-1]
	 * 
	 * @param index given index
	 */
	public void remove(int index) {
		checkRange(index, 0, size - 1);

		remove(searchFor(index));
	}
}
