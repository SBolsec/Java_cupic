package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Linked list-backed list of objects which implements interface <code>List</code>
 * Duplicate elements are allowed (each of those element will be held in different
 * list node); storage of null references is not allowed.
 *
 * @author sbolsec
 *
 */
public class LinkedListIndexedCollection<T> implements List<T> {

    /**
     * This represents a node in the linked list. It contains pointers to previous and
     * next list node and additional reference for value storage.
     */
    private static class ListNode<T> {
    	/** Value stored in the node **/
        public T value;
        /** Reference to previous node **/
        public ListNode<T> previous;
        /** Reference to next node **/
        public ListNode<T> next;

        /** ListNode constructor **/
        public ListNode(T value, ListNode<T> previous, ListNode<T> next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }

        /** Constructor which sets the value and sets the references for next and previous node to null **/
        public ListNode(T value) {
            this(value, null, null);
        }
    }

    /** Current size of list (number of elements actually stored; number of nodes in list) **/
    private int size;
    /**  Keeps track of every structural modification to the list. **/
	private long modificationCount;
    /** Reference to the first node of the linked list **/
    private ListNode<T> first;
    /** Reference to the last node of the linked list **/
    private ListNode<T> last;

    /**
     * Default constructor, creates an empty list
     */
    public LinkedListIndexedCollection() {
        this.first = this.last = null;
        this.size = 0;
        this.modificationCount = 0;
    }

    /**
     * Creates a new list based on a other collection by copying its elements.
     * @param other collection whose elements will be copied into this newly constructed collection
     * @throws NullPointerException the collection whose elements will be copied can not be <code>null</code>
     */
    public LinkedListIndexedCollection(Collection<? extends T> other) {
        this();
        if (other == null) throw new NullPointerException("Collection whose elements will be copied can not be null!");
        this.addAll(other);
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object into this list at the end of the list
     * @param value object to be added into this list
     * @throws NullPointerException <code>null</code> can not be added to the list
     */
    @Override
    public void add(T value) {
        if (value == null) throw new NullPointerException("The object to be added can not be null!");

        this.size++;
        this.modificationCount++;

        ListNode<T> node = new ListNode<>(value);
        if (this.first == null) {
            this.first = this.last = node;
            return;
        }
        last.next = node;
        node.previous = last;
        last = node;
    }

    /**
     * Returns the object that is stored in linked list at position index.
     * The time complexity of this method is never greater than n/2 + 1
     * @param index index of the object to be return from this list.
     * @return object at the given index in this list
     * @throws IndexOutOfBoundsException valid indexes are 0 to size-1
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException("Valid indexed are 0 to size-1 (" + (this.size-1) + "), the index was: " + index + ".");

        if (index <= this.size / 2) {
            ListNode<T> node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node.value;
        }

        ListNode<T> node = last;
        for (int i = 0; i < this.size - index - 1; i++) {
            node = node.previous;
        }
        return node.value;
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in linked-list.
     * Elements starting from this position are shifted one position.
     * The average time complexity of this method is n/2 + 1
     * @param value object to be inserted at the given position
     * @param position position at which the object will be inserted
     * @throws IndexOutOfBoundsException valid positions are from 0 to size
     * @throws NullPointerException <code>null</code> can not be inserted in linked-list
     */
    @Override
    public void insert(T value, int position) {
        if (value == null) throw new NullPointerException("Object to be added can not be null!");
        if (position < 0 || position > size) throw new IndexOutOfBoundsException("Position must be between 0 and size (" + this.size + "), it was: " + position + ".");

        this.modificationCount++;
        ListNode<T> node = new ListNode<>(value);

        if (position == 0) { // Insert at the start
            node.next = first;
            first.previous = node;
            first = node;
            this.size++;
            return;
        }

        if (position == this.size) { // Insert at the end
            node.previous = last;
            last.next = node;
            last = node;
            this.size++;
            return;
        }

        if (position <= this.size / 2) {
            ListNode<T> head = first;
            for (int i = 0; i < position - 1; i++) {
                head = head.next;
            }
            node.previous = head;
            node.next = head.next;
            node.next.previous = node;
            node.previous.next = node;
            this.size++;
            return;
        }

        ListNode<T> head = last;
        for (int i = 0, n = this.size - position - 1; i < n; i++) {
            head = head.previous;
        }
        node.previous = head.previous;
        node.next = head;
        node.next.previous = node;
        node.previous.next = node;
        this.size++;
        return;
    }

    /**
     * Searches the list and returns the index of the first occurrence of the given value
     * or -1 if the value is not found.
     * The average time complexity of this method is n.
     * @param value object to be searched for in linked-list
     * @return index of the first occurrence of the given value or -1 if the value is not found
     */
    @Override
    public int indexOf(Object value) {
    	if (value == null) return -1;
        int index = 0;
        for (ListNode<T> head = first; head != null; head = head.next) {
            if (head.value.equals(value))
                return index;
            index++;
        }
        return -1;
    }

    /**
     * Removes element at specified index from list. Element that was previously located at
     * index+1 after this operation is on location index.
     * @param index index of the object to be removed
     * @throws IndexOutOfBoundsException valid indexes are from 0 to size-1
     */
    @Override
    public void remove(int index) {
        if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException("Valid indexed are 0 to size1 (" + (this.size-1) + "), the index was: " + index + ".");

        this.size--;
        this.modificationCount++;
        
        if (first == last) {
            first = last = null;
            return;
        }

        if (index == 0) {
            first = first.next;
            first.previous = null;
            return;
        }
        if (index == this.size) {
            last = last.previous;
            last.next = null;
            return;
        }

        ListNode<T> head = first;
        for (int i = 0; i < index; i++) {
            head = head.next;
        }

        head.previous.next = head.next;
        head.next.previous = head.previous;
        head = null;
    }

    @Override
    public boolean contains(Object value) {
        if (indexOf(value) != -1) return true;

        return false;
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

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        int index = 0;
        for (ListNode<T> head = first; head != null; head = head.next) {
            array[index++] = head.value;
        }
        return array;
    }

    @Override
    public void clear() {
        this.first = this.last = null;
        this.size = 0;
        this.modificationCount++;
    }

	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new ListElementsGetter<T>(this);
	}
	
	/**
	 * Implementation of ElementsGetter for LinkedListIndexedCollection.
	 * @author sbolsec
	 *
	 */
	private static class ListElementsGetter<T> implements ElementsGetter<T> {
		/** Current node **/
		private ListNode<T> current;
		/** Number of modifications when this ElementsGetter was created **/
		private long savedModificationCount;
		/** Reference to the list **/
		private LinkedListIndexedCollection<T> collection;

		/**
		 * Initializes the elements getter and saves the count of current modifications.
		 * @param collection collection for which this elements getter is created 
		 */
		public ListElementsGetter(LinkedListIndexedCollection<T> collection) {
			current = collection.first;
			this.savedModificationCount = collection.modificationCount;
			this.collection = collection;
		}
		
		@Override
		public boolean hasNextElement() {
			if (this.savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException("The collection was changed!");
			return current != null;
		}

		@Override
		public T getNextElement() {
			if (!hasNextElement())
				throw new NoSuchElementException("There are no more elements in this collection!");
			T res = current.value;
			current = current.next;
			return res;
		}
	}
}
