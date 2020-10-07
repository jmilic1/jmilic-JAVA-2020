package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple class which implements basic HashTable functionality. Keys are not
 * allowed to be null, but values are. If the entry key already exists, its
 * value is overwritten.
 * 
 * @author Jura Milić
 *
 * @param <K> data type of key
 * @param <V> data type of value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Table capacity for default Constructor
	 */
	final static private int DEFAULT_CAPACITY = 16;

	/**
	 * Allowed used storage of hash table
	 */
	final static private double ALLOWED_STORAGE = 0.75;

	/**
	 * How many times the table should be increased if it comes to overflow
	 */
	final static private int TABLE_ALLOCATION = 2;

	/**
	 * Static class used for storing a key-value pair. It stores a reference to the
	 * next TableEntry with the same calculated hash code.
	 * 
	 * @author Jura Milić
	 *
	 * @param <E> data type of key
	 * @param <F> data type of value
	 */
	public static class TableEntry<E, F> {
		/**
		 * key of type E
		 */
		private E key;
		/**
		 * value of type F
		 */
		private F value;
		/**
		 * Reference to next TableEntry
		 */
		private TableEntry<E, F> next;

		/**
		 * Constructs a TableEntry with given key and value. If non-null TableEntry
		 * parameter is given the newly constructed TableEntry shall be added to the
		 * TableEntry parameters list.
		 * 
		 * @param key        given key
		 * @param value      given value
		 * @param tableEntry previous tableEntry
		 */
		public TableEntry(E key, F value, TableEntry<E, F> tableEntry) {
			this.key = key;
			this.value = value;

			if (tableEntry != null) {
				tableEntry.next = this;
			}
		}

		/**
		 * @return key
		 */
		public E getKey() {
			return key;
		}

		/**
		 * @return value
		 */
		public F getValue() {
			return value;
		}

		/**
		 * @param value new value
		 */
		public void setValue(F value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}

	/**
	 * The slots in which pairs are stored.
	 */
	private TableEntry<K, V>[] tableSlots;

	/**
	 * Amount of pairs stored in HashTable.
	 */
	private int size;

	/**
	 * Number of modifications the table went through.
	 */
	private int modificationCount;

	/**
	 * Constructs a hash table with initial capacity set to the given parameter
	 * 
	 * @param capacity given capacity
	 * @throws IllegalArgumentException if given capacity is less than 1
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1)
			throw new IllegalArgumentException("Given number was less than 1.");

		this.size = 0;
		tableSlots = new TableEntry[calculateCapacity(capacity)];
		modificationCount = 0;
	}

	/**
	 * Constructs a hash table with default capacity of 16.
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Calculates the initial capacity by using given integer. Initial capacity is
	 * the smallest power of 2 that is greater or equal to the given integer.
	 * 
	 * @param arg given integer
	 * @return calculated initial capacity
	 */
	private int calculateCapacity(int arg) {
		int initCapacity = 0;
		int i = 0;

		while (initCapacity < arg) {
			initCapacity = (int) Math.pow(2, i);
			i++;
		}
		return initCapacity;
	}

	/**
	 * Adds the given key-value pair to hash table. If the key already exists inside
	 * table, the paired value will be overwritten with the new one.
	 * 
	 * @param key   given key
	 * @param value given value
	 * @throws NullPointerException if given key is null
	 */
	public void put(K key, V value) {
		if (key == null)
			throw new NullPointerException("Given key is a null value");

		TableEntry<K, V> tableEntry = findLastOrKey(key);

		if (tableEntry == null) {
			size++;
			tableSlots[getHash(key, tableSlots.length)] = new TableEntry<K, V>(key, value, tableEntry);
		} else {
			if (tableEntry.key.equals(key))
				tableEntry.value = value;
			else {
				size++;
				modificationCount++;
				new TableEntry<>(key, value, tableEntry);
			}
		}
		checkCapacity();
	}

	/**
	 * Searches for the given key in table and returns the paired value.
	 * 
	 * @param key given key
	 * @return found value
	 */
	public V get(Object key) {
		TableEntry<K, V> tableEntry = findLastOrKey(key);

		if (tableEntry == null)
			return null;
		return tableEntry.value;
	}

	/**
	 * Checks the amount of pairs stored inside table.
	 * 
	 * @return number of stored pairs
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if table contains given key.
	 * 
	 * @param key given key
	 * @return true if key was found, false otherwise
	 */
	public boolean containsKey(Object key) {
		TableEntry<K, V> tableEntry = findLastOrKey(key);

		if (tableEntry == null)
			return false;
		if (tableEntry.key.equals(key))
			return true;
		return false;
	}

	/**
	 * Checks if table contains given value.
	 * 
	 * @param value given value
	 * @return true if value was found, false otherwise
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> tableSlot : tableSlots) {
			if (tableSlot == null)
				continue;

			TableEntry<K, V> tableEntry = tableSlot;

			while (tableEntry != null) {
				if (tableEntry.value.equals(value))
					return true;
				tableEntry = tableEntry.next;
			}
		}
		return false;
	}

	/**
	 * Removes the pair that contains given key from table.
	 * 
	 * @param key given key
	 */
	public void remove(Object key) {
		if (key == null)
			return;

		int hash = getHash(key, tableSlots.length);

		if (tableSlots[hash] == null)
			return;

		if (tableSlots[hash].key.equals(key)) {
			size--;
			modificationCount++;
			tableSlots[hash] = tableSlots[hash].next;
		} else {
			TableEntry<K, V> tableEntry = findLastOrBeforeKey(key);

			if (tableEntry != null)
				if (tableEntry.next != null) {
					tableEntry.next = tableEntry.next.next;
					size--;
					modificationCount++;
				}
		}
	}

	/**
	 * Checks if table contains no elements
	 * 
	 * @return true if table is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("[");
		int stringedEntries = 0;

		for (TableEntry<K, V> tableSlot : tableSlots) {
			if (tableSlot == null)
				continue;

			TableEntry<K, V> tableEntry = tableSlot;

			while (tableEntry != null) {
				sb.append(tableEntry.toString());
				stringedEntries++;
				tableEntry = tableEntry.next;
				if (stringedEntries != size)
					sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Deletes all elements of the table
	 */
	public void clear() {
		for (TableEntry<K, V> tableSlot : tableSlots) {
			tableSlot = null;
		}
		modificationCount++;
	}

	/**
	 * Creates an Iterator which will go through elements of SimpleHashtable
	 */
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Simple Iterator implementation which iterates through elements of its table.
	 * Iterator checks if table was modified from outside sources.
	 * 
	 * @author Jura Milić
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/**
		 * Number of modifications the table went through before constructing
		 * IteratorImpl + number of modifications the IteratorImpl made to the table
		 */
		private int modificationCount;

		/**
		 * Reference to the last TableEntry the Iterator returned.
		 */
		private SimpleHashtable.TableEntry<K, V> current;

		/**
		 * Constructs an IteratorImpl
		 */
		public IteratorImpl() {
			this.modificationCount = SimpleHashtable.this.modificationCount;
		}

		/**
		 * Returns true if the iterator has more table elements.
		 * 
		 * @return true if there are more elements, false otherwise
		 * @throws ConcurrentModificationException if iterators table was modified by
		 *                                         outside sources
		 */
		public boolean hasNext() {
			compareModifications();
			@SuppressWarnings("unchecked")
			SimpleHashtable.TableEntry<K, V> entry = getNext();

			if (entry == null)
				return false;
			return true;
		}

		/**
		 * Returns the next table element in iteration.
		 * 
		 * @return next table element
		 * @throws ConcurrentModificationException if iterators table was modified by
		 *                                         outside sources
		 * @throws NoSuchElementException          if there are no more table elements
		 */
		@SuppressWarnings("rawtypes")
		public SimpleHashtable.TableEntry next() {
			compareModifications();
			@SuppressWarnings("unchecked")
			SimpleHashtable.TableEntry<K, V> entry = getNext();

			if (entry == null)
				throw new NoSuchElementException();
			current = entry;
			return current;
		}

		/**
		 * Removes from the last element returned by this iterator from the iterators
		 * table. This method can be called only once per call to next.
		 * 
		 * @throws IllegalStateException           if method was called twice on same
		 *                                         element
		 * @throws ConcurrentModificationException if iterators table was modified by
		 *                                         outside sources
		 */
		public void remove() {
			compareModifications();

			SimpleHashtable.this.remove(current.key);
			modificationCount++;
			try {
				compareModifications();
			} catch (ConcurrentModificationException ex) {
				throw new IllegalStateException();
			}
		}

		/**
		 * Searches for the next table element in iteration. If there are no more
		 * elements, returns null
		 * 
		 * @return next table element or null if there no more
		 */
		@SuppressWarnings("rawtypes")
		private SimpleHashtable.TableEntry getNext() {
			if (current == null) {
				return goThroughSlots(0);
			} else {
				if (current.next != null)
					return current.next;

				@SuppressWarnings("unchecked")
				SimpleHashtable.TableEntry<K, V> entry = goThroughSlots(
						SimpleHashtable.this.getHash(current.key, SimpleHashtable.this.tableSlots.length) + 1);

				return entry;
			}
		}

		/**
		 * Iterates through next table slots in iteration until it finds a non-null
		 * element. If there are no more elements in iteration, returns null
		 * 
		 * @param currentIndex slot index of current element in iteration
		 * @return next non-null table slot or null if there no more elements
		 */
		@SuppressWarnings("rawtypes")
		private SimpleHashtable.TableEntry goThroughSlots(int currentIndex) {
			if (currentIndex >= SimpleHashtable.this.tableSlots.length)
				return null;

			SimpleHashtable.TableEntry<K, V> entry = SimpleHashtable.this.tableSlots[currentIndex];

			while (entry == null) {
				currentIndex++;
				if (currentIndex >= SimpleHashtable.this.tableSlots.length)
					return null;
				entry = SimpleHashtable.this.tableSlots[currentIndex];
			}
			return entry;

		}

		/**
		 * Compares the iterators modification count and its tables modification count.
		 * If values of modification counts do not match an exception is thrown
		 * 
		 * @throws ConcurrentModificationException if values do not match
		 */
		private void compareModifications() {
			if (this.modificationCount != SimpleHashtable.this.modificationCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * Searches for the given key in table. If the key is found, the found pair is
	 * returned. If table does not contain the key, the last pair of the searched
	 * slot is returned.
	 * 
	 * @param key given key
	 * @return found pair or last pair in slot
	 */
	private TableEntry<K, V> findLastOrKey(Object key) {
		TableEntry<K, V> tableEntry = findLastOrBeforeKey(key);

		if (tableEntry == null)
			return tableEntry;
		if (tableEntry.next == null || tableEntry.key.equals(key))
			return tableEntry;
		return tableEntry.next;
	}

	/**
	 * Searches for the given key in table. If the pair is found, the pair before it
	 * is returned. If the found pair is the first in the slot, the found pair is
	 * returned. If pair was not found, the last pair of the searched slot is
	 * returned.
	 * 
	 * @param key
	 * @return
	 */
	private TableEntry<K, V> findLastOrBeforeKey(Object key) {
		TableEntry<K, V> tableEntry = findSlot(key);

		if (tableEntry == null)
			return tableEntry;
		if (tableEntry.next == null || tableEntry.key.equals(key))
			return tableEntry;
		while (!tableEntry.next.key.equals(key)) {
			tableEntry = tableEntry.next;
			if (tableEntry.next == null)
				break;
		}
		return tableEntry;
	}

	/**
	 * Returns the first pair of the given keys appropriate slot.
	 * 
	 * @param key given key
	 * @return first pair of slot
	 */
	private TableEntry<K, V> findSlot(Object key) {
		int hash = getHash(key, tableSlots.length);
		return tableSlots[hash];
	}

	/**
	 * Calculates hash code for the given key and capacity.
	 * 
	 * @param key      given key
	 * @param capacity given capacity
	 * @return calculated hash code
	 */
	private int getHash(Object key, int capacity) {
		return Math.abs(key.hashCode() % capacity);
	}

	/**
	 * Checks if table has too many elements relative to its capacity and allocates
	 * a larger storage capacity if that is the case.
	 */
	private void checkCapacity() {
		if (size >= tableSlots.length * ALLOWED_STORAGE) {
			allocateNewSlots();
		}
	}

	/**
	 * Allocates more storage capacity for table.
	 */
	private void allocateNewSlots() {
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] tableSlots = new TableEntry[this.tableSlots.length * TABLE_ALLOCATION];

		for (TableEntry<K, V> tableEntry : this.tableSlots) {
			if (tableEntry == null)
				continue;

			int hash = getHash(tableEntry.key, tableSlots.length);

			if (tableSlots[hash] == null)
				tableSlots[hash] = tableEntry;
			else {
				TableEntry<K, V> slotEntry = tableSlots[hash];

				while (slotEntry.next != null) {
					slotEntry = slotEntry.next;
				}
				slotEntry = tableEntry;
			}
		}
		this.tableSlots = tableSlots;
	}
}
