package hr.fer.oprpp1.custom.collections;

/**
 * Stores key-value pairs
 * 
 * @author sbolsec
 *
 */
public class Dictionary<K, V> {
	/** Internal list that stores the key-value pairs of the dictionary **/
	private ArrayIndexedCollection<DictionaryEntry<? extends K, ? extends V>> elements;
	
	/**
	 * One key-value pair of the dictionary.
	 * @author sbolsec
	 *
	 * @param <K> key
	 * @param <V> value
	 */
	private static class DictionaryEntry<K, V> {
		/** Key of the pair **/
		private K key;
		/** Value of the pair **/
		private V value;
		
		/**
		 * Constructor which sets the key and value.
		 * @param key key of the entry
		 * @param value value of the entry
		 * @throws IllegalArgumentException if the key is null
		 */
		public DictionaryEntry(K key, V value) {
			if (key == null) throw new IllegalArgumentException("Key can not be null!");
			this.key = key;
			this.value = value;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof DictionaryEntry<?, ?>))
				return false;
			DictionaryEntry<K, V> other = (DictionaryEntry<K, V>) obj;
			
			return this.key==null ? other.key==null : this.key.equals(other.key);
		}
	}
	
	/**
	 * Creates empty dictionary.
	 */
	public Dictionary() {
		elements = new ArrayIndexedCollection<>();
	}
	
	/**
	 * Checks whether the dictionary is empty.
	 * @return true if the dictionary is empty, false otherwise
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	/**
	 * Returns the number of elements in the dictionary.
	 * @return number of elements in the dictionary
	 */
	public int size() {
		return elements.size();
	}
	
	/**
	 * Removes all elements from the dictionary.
	 */
	public void clear() {
		elements.clear();
	}
	
	/**
	 * Adds the key-value pair into the dictionary. If the key already exists, the old value will be
	 * overwritten by the new value. Returns the old value, or null if this key-value pair didn't exist. 
	 * @param key key of the pair
	 * @param value value of the pair
	 * @return old value for this key or null if there was no value for this key
	 */
	public V put(K key, V value) {
		DictionaryEntry<K, V> entry = new DictionaryEntry<>(key, value);
		int index = elements.indexOf(entry);
		if (index != -1) {
			V res = elements.get(index).value;
			elements.remove(index);
			elements.add(entry);
			return res;
		}
		elements.add(entry);
		return null;
	}
	
	/**
	 * Returns the value from the given key or null if the key does not exist.
	 * @param key key from which to return value
	 * @return value from the given key or null if the key does not exist
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		DictionaryEntry<K, V> entry = new DictionaryEntry<>((K) key, null);
		int index = elements.indexOf(entry);
		if (index != -1) {
			return elements.get(index).value;
		}
		return null;
	}
	
	/**
	 * Removes the key-value pair with the given key and returns the value of the pair
	 * or null if it didn't exist.
	 * @param key key of the pair which to remove
	 * @return value of the pair to be deleted or null if it didn't exist
	 */
	public V remove(K key) {
		DictionaryEntry<K, V> entry = new DictionaryEntry<>(key, null);
		int index = elements.indexOf(entry);
		if (index != -1) {
			V res = elements.get(index).value;
			elements.remove(index);
			return res;
		}
		return null;
	}
}
