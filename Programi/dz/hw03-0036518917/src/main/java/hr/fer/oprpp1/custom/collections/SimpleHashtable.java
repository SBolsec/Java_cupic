package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple implementation of a hash table.
 * @author sbolsec
 *
 * @param <K> type of the key
 * @param <V> type of the value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	
	/** Initial capacity of the table which stores the values **/
	private static final int INITIAL_CAPACITY = 16;
	/** Percentage of how full this table can get before doubling its size **/
	private static final double OVERFLOW = 0.75;
	/** Array of table entry slots **/
	private TableEntry<K, V>[] table;
	/** Number of elements in table **/
	private int size;
	/** Keeps count of all structural modifications to internal array **/
	private int modificationCount;
	
	/**
	 * Default constructor which initializes the table to have 16 slots.
	 */
	public SimpleHashtable() {
		this(INITIAL_CAPACITY);
	}
	
	/**
	 * Constructor which sets the table size to be the first power of 2 which
	 * is greater or equal to the given input capacity.
	 * @param capacity minimum capacity
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) throw new IllegalArgumentException("Capacity can not be smaller than 0, it was: " + capacity + ".");
		int newCapacity = findFirstBiggerPowerOfTwo(capacity);
		this.table = (TableEntry<K, V>[]) new TableEntry[newCapacity];
		this.size = 0;
	}
	
	/**
	 * Adds the key-value pair to the table. If the key already exists in the table it updates the value
	 * and returns the old value. Otherwise it just adds the pair and returns null.
	 * @param key key to be added
	 * @param value value to be added
	 * @return old value for the specified key or null
	 * @throws NullPointerException if the key is null
	 */
	public V put(K key, V value) {
		if (key == null) throw new NullPointerException("Key can not be null!");
		
		int index = Math.abs(key.hashCode()) % table.length;		
		TableEntry<K, V> head = table[index];
		if (head == null) { // First element to be added
			if (checkForOverflow()) {
				realocateTable();
				return put(key, value);
			}
			table[index] = new TableEntry<>(key, value, null);
			this.size++;
			this.modificationCount++;
			return null;
		}
		for (; head != null; head = head.next) {
			if (head.key.equals(key)) { // Update the value
				V res = head.getValue();
				head.setValue(value);
				return res;
			}
			if (head.next == null) { // No more elements, have to create a new one
				if (checkForOverflow()) {
					realocateTable();
					return put(key, value);
				}
				head.next = new TableEntry<>(key, value, null);
				this.size++;
				this.modificationCount++;
				break;
			}
		}
		return null;
	}
	
	/**
	 * Returns the value for the given key.
	 * @param key key for which to return value
	 * @return value for the given key or null
	 */
	public V get(Object key) {
		if (key == null) return null;
		int index = Math.abs(key.hashCode()) % table.length;
		TableEntry<K, V> head = table[index];
		for (; head != null && !head.key.equals(key); head = head.next) ;
		if (head == null) return null;
		return head.value;
	}
	
	public int size() {
		return this.size;
	}
	
	/**
	 * Checks whether the given key exists in the table.
	 * @param key key to be checked
	 * @return true if the key exists in the table, false otherwise
	 */
	public boolean containsKey(Object key) {
		if (key == null) return false;
		int index = Math.abs(key.hashCode()) % table.length;
		TableEntry<K, V> head = table[index];
		for (; head != null && !head.key.equals(key); head = head.next) ;
		if (head == null) return false;
		return true;
	}
	
	/**
	 * Checks whether the given value exists in the table.
	 * @param value value to be checked
	 * @return true if the value exists in the table, false otherwise
	 */
	public boolean containsValue(Object value) {
		TableEntry<K, V> head; 
		for (int i = 0; i < table.length; i++) {
			head = table[i];
			for (; head != null && !head.value.equals(value); head = head.next) ;
			if (head != null) return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public V remove(Object key) {
		if (key == null) return null;
		int index = Math.abs(key.hashCode()) % table.length;
		TableEntry<K, V> head = table[index];
		TableEntry<K, V> previous = null;
		for (; head != null && !head.key.equals(key); previous = head, head = head.next) ;
		// Key was not in list
		if (head == null) return null;
		if (previous == null) { // First element will be removed
			table[index] = head.next;
		} else { // Any other element
			previous.next = head.next;
		}
		V res = head.value;
		this.size--;
		this.modificationCount++;
		return res;
	}
	
	/**
	 * Checks whether the table is empty.
	 * @return true if table is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	/**
	 * Returns list of all elements in the table.
	 * eq. "[key1=value1, key2=value2, key3=value3]"
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		TableEntry<K, V> head; 
		for (int i = 0; i < table.length; i++) {
			head = table[i];
			for (; head != null; head = head.next) {
				sb.append(head.toString());
				if (head.next != null) {
					sb.append(", ");
				}
			}
			if (table[i] != null && i+1 < table.length) {
				while (++i < table.length && table[i] == null);
				if (i >= table.length) {
					break;
				} else if (table[i] != null) {
					sb.append(", ");
					i--;
				} else {
					break;
				}
			}
		}
		
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Returns an array of all the elements in the table.
	 * @return array of all the elements in the table
	 */
	@SuppressWarnings("unchecked")
	public TableEntry<K,V>[] toArray() {
		TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];
		
		TableEntry<K, V> head;
		int count = 0;
		for (int i = 0; i < table.length; i++) {
			head = table[i];
			for (; head != null; head = head.next) {
				array[count++] = head;
			}
		}
		
		return array;
	}
	
	/**
	 * Deletes all the elements from the table.
	 */
	public void clear() {
		for (int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		this.size = 0;
	}
	
	/**
	 * Returns the first power of 2 which is larger or equal to the input number.
	 * @param input input number from which to find the first greater or equal power of 2
	 * @return first power of 2 which is larger or equal to the input number
	 */
	private int findFirstBiggerPowerOfTwo(int input) {
		int res = 1;
		while (res < input) {
			res *= 2;
		}
		return res;
	}
	
	/**
	 * Checks whether the percentage of elements in the table is greater 
	 * than OVERFLOW (default 75%)
	 * @return true if table is almost full, false otherwise
	 */
	private boolean checkForOverflow() {
		if ((double) this.size / (double) table.length >= OVERFLOW) {
			return true;
		}
		return false;
	}
	
	/**
	 * Doubles the size of the table array which stores all the elements
	 * and adds all the elements to the new, bigger, array.
	 */
	@SuppressWarnings("unchecked")
	private void realocateTable() {
		TableEntry<K, V>[] elements = this.toArray();
		
		this.table = (TableEntry<K, V>[]) new TableEntry[table.length * 2];
		this.size = 0;
		
		for (int i = 0; i < elements.length; i++) {
			this.put(elements[i].key, elements[i].value);
		}
	}
	
	/**
	 * Table entry which contains key and value pair.
	 * @author sbolsec
	 *
	 * @param <K> type of key
	 * @param <V> type of value
	 */
	public static class TableEntry<K, V> {
		/** Key of the pair **/
		private K key;
		/** Value of the pair **/
		private V value;
		/** Reference to next element in list **/
		private TableEntry<K, V> next;
		
		/**
		 * Creates new key-value pair with given data.
		 * @param key key of the pair
		 * @param value value of the pair
		 * @param next next element in the list
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			if (key == null) throw new IllegalArgumentException("Key can not be null!");
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Returns the key of the pair.
		 * @return key key of the pair
		 */
		public K getKey() {
			return this.key;
		}
		
		/**
		 * Sets the value to the specified value.
		 * @param value value to be set
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Returns the value of the pair.
		 * @return value value of the pair
		 */
		public V getValue() {
			return this.value;
		}
		
		/**
		 * Formats the pair as "key=value"
		 */
		@Override
		public String toString() {
			return String.format("%s=%s", key, value);
		}
	}

	@Override
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}
	
	/**
	 * Models an iterator which iterates over the table
	 * @author sbolsec
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/** Number of modifications done at the time of creating this iterator **/
		private int currentModificationsCount;
		/** Index of slot where the current element is **/
		private int indexOfSlot;
		/** Current element **/
		private TableEntry<K, V> current;
		/** Next element to return **/
		private TableEntry<K, V> next;
		
		/**
		 * Initializes the iterator
		 */
		public IteratorImpl() {
			currentModificationsCount = modificationCount;
			current = next = null;
			indexOfSlot = 0;
			for (int i = 0; i < table.length; i++) {
				if (table[i] != null) {
					next = table[i];
					indexOfSlot = i;
					break;
				}
			}
		}
		
		/**
		 * Checks whether there is a next element to iterate to.
		 * @throws ConcurrentModificationException if there were modifications to the table from outside of the iterator
		 */
		@Override
		public boolean hasNext() {
			if (modificationCount != currentModificationsCount)
				throw new ConcurrentModificationException("There were modifications to the table!");
			
			return next != null;
		}

		/**
		 * Returns the next element in the table or throws an exception if there is no more items.
		 * @throws ConcurrentModificationException if there were modifications to the table from outside of the iterator
		 * @throws NoSuchElementException if there is no more elements in the table
		 */
		@Override
		public SimpleHashtable.TableEntry<K, V> next() {
			if (modificationCount != currentModificationsCount)
				throw new ConcurrentModificationException("There were modifications to the table!");
			if (next == null)
				throw new NoSuchElementException("There is no more elements to return!");
			
			TableEntry<K, V> res = next;
			current = next;
			next = next.next;
			if (next == null && table != null) {
				do {} while (indexOfSlot + 1 < table.length && (next = table[++indexOfSlot]) == null);
			}
			return res;
		}
		
		/**
		 * Deletes the current element in the table.
		 * @throws ConcurrentModificationException if there were modifications to the table from outside of the iterator
		 * @throws IllegalStateException if there is no element to delete
		 */
		public void remove() {
			if (current == null)
				throw new IllegalStateException("No element to delete!");
			if (modificationCount != currentModificationsCount)
				throw new ConcurrentModificationException("There were modifications to the table!");
			
			TableEntry<K, V> temp = current;
			current = null;
			SimpleHashtable.this.remove(temp.key);
			currentModificationsCount = modificationCount;
		}
	}
}
