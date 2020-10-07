package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * A Collection that is also a List which uses Linked Lists as storage.
 * Collection can store multiple duplicates of the same object and doesn't store
 * null references.
 * 
 * @author Jura Milić
 *
 */
public class LinkedListIndexedCollection<T> implements List<T> {

	/**
	 * A node containing content.
	 */
	private static class ListNode<T> {
		/**
		 * Reference to previous node
		 */
		ListNode<T> prev;
		/**
		 * Reference to next node
		 */
		ListNode<T> next;
		/**
		 * Stored content within a single node.
		 */
		T value;
	}

	/**
	 * The number of elements in the collection.
	 */
	private int size;
	/**
	 * Reference to the first element in the collection.
	 */
	private ListNode<T> first;
	/**
	 * Reference to the last element in the collection.
	 */
	private ListNode<T> last;

	/**
	 * Stores number of times the Collection was modified. Used for ElementsGetter
	 * of LinkedListIndexedCollection.
	 */
	private long modificationCount;

	/**
	 * An implementation of ElementsGetter. Goes through elements of a
	 * LinkedListIndexedCollection indexed from 0 to size-1 in that order.
	 * 
	 * @author Jura Milić
	 *
	 */
	private static class LinkedListGetter<E> implements ElementsGetter<E> {
		/**
		 * The next node that should be returned.
		 */
		ListNode<E> current;

		/**
		 * The Collection whose elements are being returned.
		 */
		LinkedListIndexedCollection<E> coll;

		/**
		 * Number of modifications the Collection went through when constructing the
		 * ElementsGetter.
		 */
		private long savedModificationCount;

		/**
		 * Constructs a LinkedListGetter by setting the first node from which the Getter
		 * will start from a given LinkedListIndexedCollection.
		 * 
		 * @param collection given LinkedListIndexedCollection
		 */
		public LinkedListGetter(LinkedListIndexedCollection<E> collection) {
			coll = collection;
			savedModificationCount = coll.modificationCount;
			current = coll.first;
		}

		/**
		 * @throws ConcurrentModificationException if the Collection which is being
		 *                                         cycled through by this ElementGetter
		 *                                         was modified after constructing the
		 *                                         ElementGetter
		 */
		public boolean hasNextElement() {
			if (savedModificationCount != coll.modificationCount) {
				throw new ConcurrentModificationException();
			}
			return current != null;
		}

		/**
		 * @throws NoSuchElementException if there are no more elements to return.
		 */
		public E getNextElement() {
			if (hasNextElement()) {
				E value = current.value;
				current = current.next;
				return value;
			} else {
				throw new NoSuchElementException();
			}
		}
	}

	/**
	 * Constructs an empty LinkedListIndexedCollection.
	 */
	public LinkedListIndexedCollection() {
		size = 0;
		first = null;
		last = null;

		modificationCount = 0;
	}

	/**
	 * Constructs a LinkedListIndexedCollection with content copied from given
	 * collection.
	 * 
	 * @param other given collection
	 * @throws NullPointerException if given collection is null
	 */
	public LinkedListIndexedCollection(Collection<? extends T> other) {
		if (other == null) {
			throw new NullPointerException();
		}
		addAll(other);

		modificationCount = 0;
	}

	public ElementsGetter<T> createElementsGetter() {
		return new LinkedListGetter<T>(this);
	}

	public int size() {
		return size;
	}

	/**
	 * @throws NullPointerException if given value is null
	 */
	public void add(T value) {
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
	private void remove(ListNode<T> node) {
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

		modificationCount++;
	}

	public boolean remove(Object value) {
		ListNode<T> currentNode = first;

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
		ListNode<T> currentNode = first;

		for (int i = 0; i < size; i++) {
			array[i] = currentNode.value;
			currentNode = currentNode.next;
		}
		return array;
	}

	public void clear() {
		if (size == 0) {
			return;
		}

		ListNode<T> currentNode = first;

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

		modificationCount++;
	}

	/**
	 * Searches for the node given by index within the Collection with complexity
	 * O(2/n+1).
	 * 
	 * @param index given index
	 * @return found node
	 */
	private ListNode<T> searchFor(int index) {
		ListNode<T> currentNode;
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

	public T get(int index) {
		checkRange(index, 0, size - 1);

		return searchFor(index).value;
	}

	/**
	 * @throws NullPointerException thrown if value is null
	 */
	public void insert(T value, int position) {
		checkRange(position, 0, size);

		ListNode<T> currentNode = searchFor(position - 1);

		if (currentNode == null) {
			currentNode = new ListNode<T>();
			first = currentNode;
			last = currentNode;
			currentNode.value = value;
		} else {
			ListNode<T> newNode = new ListNode<T>();
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

		modificationCount++;
	}

	public int indexOf(Object value) {
		if (value == null) {
			return -1;
		}

		if (first == null) {
			return -1;
		}

		ListNode<T> currentNode = first;
		int index = 0;

		while (index < size - 1) {
			if (currentNode.value.equals(value)) {
				return index;
			}
			index++;
			currentNode = currentNode.next;
		}

		if (currentNode != null) {
			if (currentNode.value.equals(value)) {
				return index;
			}
		}

		return -1;
	}

	public void remove(int index) {
		checkRange(index, 0, size - 1);

		remove(searchFor(index));
	}
}
