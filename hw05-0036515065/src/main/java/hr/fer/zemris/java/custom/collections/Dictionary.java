package hr.fer.zemris.java.custom.collections;

import java.util.NoSuchElementException;

/**
 * A class that stores key-value pairs. It implements basic Dictionary
 * functionality. Stored keys cannot be NULL, but stored values can.
 * 
 * @author Jura Milić
 *
 * @param <K> type of value used for keys
 * @param <V> type of value used for values
 */
public class Dictionary<K, V> {

	/**
	 * Static class used for storing one key-value pair. The stored pair is typed as
	 * (E key, F value).
	 * 
	 * @author Jura Milić
	 *
	 * @param <E> type of value used for keys
	 * @param <F> type of value used for values
	 */
	private static class Word<E, F> {
		/**
		 * key of type E
		 */
		private E key;
		/**
		 * value of type F
		 */
		private F value;

		/**
		 * Constructs a Word with given key and value.
		 * 
		 * @param key   given key
		 * @param value given value
		 */
		private Word(E key, F value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			return obj.equals(key);
		}
	}

	/**
	 * Collection used for storing Words of Dictionary.
	 */
	private ArrayIndexedCollection<Word<K, V>> coll;

	/**
	 * Constructs an empty Dictionary.
	 */
	public Dictionary() {
		coll = new ArrayIndexedCollection<Word<K, V>>();
	}

	/**
	 * Checks if Dictionary is empty.
	 * 
	 * @return true if Dictionary is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return coll.isEmpty();
	}

	/**
	 * Checks the size of Dictionary.
	 * 
	 * @return the size of Dictionary
	 */
	public int size() {
		return coll.size();
	}

	/**
	 * Erases the contents of Dictionary.
	 */
	public void clear() {
		coll.clear();
	}

	/**
	 * Puts a pair of given key and given value into Dictionary. If a pair with
	 * given key already exists, the old value will be overwritten with the new one.
	 * If a pair with the given given key does not exist, the new pair is placed at
	 * the end of the Dictionary.
	 * 
	 * @param key   given key
	 * @param value given value
	 * @throws NullPointerException if given key is null
	 */
	public void put(K key, V value) {
		if (key == null)
			throw new NullPointerException();

		int index = coll.indexOf(key);
		if (index != -1) {
			Word<K, V> word = coll.get(index);
			word.value = value;
		} else {
			Word<K, V> word = new Word<>(key, value);
			coll.add(word);
		}
	}

	/**
	 * Searches for a pair within Dictionary that matches the given key and returns
	 * the value of found pair.
	 * 
	 * @param key given key
	 * @return found value
	 * 
	 * @throws NoSuchElementException if given key does not exist in Dictionary.
	 */
	public V get(Object key) {
		int index = coll.indexOf(key);

		if (index == -1)
			throw new NoSuchElementException();

		return coll.get(index).value;
	}
}
