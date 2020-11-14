package hr.fer.oprpp1.custom.collections;

/**
 * Resizable array-backed collection which extends class <code>Collection</code>.
 * Duplicate elements are allowed, storage of <code>null</code> references is not allowed.
 * 
 * @author sbolsec
 *
 */
public class ArrayIndexedCollection extends Collection {

	/** Initial capacity of the collection **/
	private static final int INITIAL_CAPACITY = 16;
	/** Current size of collection (number of elements actually stored in elements array). **/
	private int size;
	/** An array of object references **/
	private Object[] elements;
	
	/**
	 * Default constructor, creates an instance of the collection with capacity 16.
	 */
	public ArrayIndexedCollection() {
		this(INITIAL_CAPACITY);
	}

	/**
	 * Creates an instance of the collection with the desired capacity.
	 * 
	 * @param initialCapacity initial capacity of the collection
	 * @throws IllegalArgumentException if the initial capacity is less than 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) throw new IllegalArgumentException("The initial capacity can not be less than 1!");
		
		this.size = 0;
		this.elements = new Object[initialCapacity];
	}

	/**
	 * Creates a new collection and copies the elements of the passed collection 'other' into it.
	 * 
	 * @param other collection whose items will be copied
	 * @throws NullPointerException <code>other</code> can not be null
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, 1);
	}
	
	/**
	 * Creates a new collection and copies the elements of the passed collection 'other' into it.
	 * If the <code>initialCapacity</code> is smaller that the size of the given collection, the size of the given collection will be used.
	 * 
	 * @param other collection whose items will be copied
	 * @param initialCapacity initial capacity of the new collection
	 * @throws NullPointerException <code>other</code> can not be null 
	 * @throws IllegalArgumentException if the initial capacity is less than 1
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other == null) throw new NullPointerException("Given collection can not be null!");
		if (initialCapacity < 1) throw new IllegalArgumentException("The initial capacity can not be less than 1!");
		
		int capacity = initialCapacity < other.size() ? other.size() : initialCapacity;
		elements = new Object[capacity];
		this.addAll(other);
	}
	
	@Override
	public int size() {
		return this.size;
	}
	
	/**
	 * Adds the given object into this collection.
	 * (reference is added into first empty place in the elements array; if the elements array is full, it is reallocated by doubling its size).
	 * The time complexity is O(1)
	 * 
	 * @param value object to be added into this collection
	 * @throws NullPointerException the object to be added can not be null
	 */
	@Override
	public void add(Object value) {
		if (value == null) throw new NullPointerException("The object to be added can not be null!");

		reallocateArray();

		elements[this.size++] = value;
	}
	
	/**
	 * Returns the object that is stored in backing array at position <code>index</code>.
	 * The average complexity of this method is O(1)
	 * 
	 * @param index position from which the object will be returned
	 * @return the object that is stored at position <code>index</code>
	 * @throws IndexOutOfBoundsException index must be between 0 and size-1
	 */
	public Object get(int index) {
		if (index < 0 || index >= size) throw new IndexOutOfBoundsException("The index must be between 0 and size-1 (" + (this.size-1) + "), it was: " + index + ".");
		
		return elements[index];
	}
	
	/**
	 * Inserts the object at the specified position. It does not overwrite the given value at the given position, 
	 * it shifts the elements at greater positions one place toward the end.
	 * The average complexity of this method is O(n/2)
	 * 
	 * @param value object to be inserted
	 * @param position index where the object should be inserted
	 * @throws NullPointerException <code>null</code> object will not be inserted into the collection
	 * @throws IndexOutOfBoundsException index must be between 0 and size
	 */
	public void insert(Object value, int position) {
		if (value == null) throw new NullPointerException("The object to be added can not be null!");
		if (position < 0 || position > size) throw new IndexOutOfBoundsException("The index must be between 0 and size (" + (this.size-1) + "), it was: " + position + ".");
		
		reallocateArray();
		
		for (int i = elements.length - 1; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		
		elements[position] = value;
		this.size++;
	}
	
	/**
	 * Searches the collection and returns the index of the first occurrence of the given value
	 * or -1 if the value is not found.
	 * The average complexity of this method is O(n/2)
	 * 
	 * @param value object that will be searched
	 * @return index of the first occurrence of the given object or -1 if the value is not found
	 */
	public int indexOf(Object value) {
		if (value == null) return -1;
		for (int i = 0; i < this.size; i++) {
			if (elements[i].equals(value))
				return i;
		}
		return -1;
	}

	@Override
	public boolean contains(Object value) {
		if (indexOf(value) != -1) return true;

		return false;
	}
	
	/**
	 * Removes element at specified index from collection. 
	 * Element that was previously at location index+1 after this operation is on location index , etc.
	 * 
	 * @param index index at which the element should be removed
	 * @throws IndexOutOfBoundsException index must be between 0 and size-1
	 */
	public void remove(int index) {
		if (index < 0 || index >= size) throw new IndexOutOfBoundsException("The index must be between 0 and size-1 (" + (this.size-1) + "), it was: " + index + ".");
		
		for (int i = index; i < this.size - 1; i++) {
			elements[i] = elements[i+1];
		}
		
		elements[this.size - 1] = null;
		this.size--;
	}
	
	/**
	 * Allocates new array with size equals to the size of this collection, fills it with collection
	 * content and returns the array. This method never returns null.
	 * 
	 * @return array made from the collection
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.size];
		for (int i = 0; i < this.size; i++) {
			array[i] = elements[i];
		}
		return array;
	}
	
	@Override
	public void forEach(Processor processor) {
		for (Object o : elements) {
			if (o != null)
				processor.process(o);
		}
	}
	
	@Override
	public void clear() {
		this.size = 0;
		for (int i = 0; i < elements.length; i++) {
			elements[i] = null;
		}
	}

	@Override
	public boolean remove(Object value) {
		int index = indexOf(value);
		if (index != -1) {
			remove(index);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the backing array is full.
	 * If it is full, this method creates a new array double the size of the current
	 * one and copies all of the values to it.
	 */
	private void reallocateArray() {
		if (size == elements.length) {
			Object[] newElements = new Object[2 * elements.length];
			for (int i = 0; i < elements.length; i++) {
				newElements[i] = elements[i];
			}
			elements = newElements;
		}
	}
}
