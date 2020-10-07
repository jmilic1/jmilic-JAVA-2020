package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * A map with String keys and ValueWrapper values that stores its values of same
 * keys in a stack. In other words, each key has its own personal stack which
 * stores its associated values.
 * 
 * @author Jura Milić
 *
 */
public class ObjectMultistack {
	/**
	 * A single entry in ObjectMultistack.
	 * 
	 * @author Jura Milić
	 *
	 */
	private static class MultistackEntry {
		/**
		 * Reference to previous entry in this key-stack
		 */
		private MultistackEntry prev;
		/**
		 * The value of this entry.
		 */
		private ValueWrapper value;

		/**
		 * Constructs a MultistackEntry with given value
		 * 
		 * @param value given value
		 * @param prev  previous entry
		 */
		private MultistackEntry(ValueWrapper value, MultistackEntry prev) {
			this.value = value;
			this.prev = prev;
		}
	}

	/**
	 * Internal map of ObjectMultistack used for storing the first added value for a
	 * certain key.
	 */
	private Map<String, MultistackEntry> map;

	/**
	 * Constructs an ObjectMultistack
	 */
	public ObjectMultistack() {
		map = new HashMap<String, MultistackEntry>();
	}

	/**
	 * Pushes the given value onto given key's stack.
	 * 
	 * @param keyName      given key's name
	 * @param valueWrapper given value
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		if (isEmpty(keyName)) {
			map.put(keyName, new MultistackEntry(valueWrapper, null));
		} else {
			map.put(keyName, new MultistackEntry(valueWrapper, map.get(keyName)));
		}
	}

	/**
	 * Pops the last added value from given key's stack.
	 * 
	 * @param keyName given key's name
	 * @return popped value
	 * @throws RuntimeException if key's stack is empty
	 */
	public ValueWrapper pop(String keyName) {
		if (isEmpty(keyName)) {
			throw new IllegalArgumentException("Multistack is empty!");
		}

		MultistackEntry lastEntry = map.get(keyName);
		map.put(keyName, lastEntry.prev);
		return lastEntry.value;
	}

	/**
	 * Peeks the last added value from given key's stack.
	 * 
	 * @param keyName given key's name
	 * @return peeked value
	 * @throws RuntimeException if key's stack is empty
	 */
	public ValueWrapper peek(String keyName) {
		if (isEmpty(keyName)) {
			throw new RuntimeException("Multistack is empty!");
		}

		return map.get(keyName).value;
	}

	/**
	 * Checks if given key's stack is empty.
	 * 
	 * @param keyName given key's name
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty(String keyName) {
		if (map.get(keyName) == null) {
			return true;
		}
		return false;
	}
}
