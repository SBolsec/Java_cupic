package hr.fer.oprpp1.custom.collections;

/**
 * Linked list-backed collection of objects which extends class <code>Collection</code>
 * Duplicate elements are allowed (each of those element will be held in different
 * list node); storage of null references is not allowed.
 *
 * @author sbolsec
 *
 */
public class LinkedListIndexedCollection extends Collection {

    /**
     * This represents a node in the linked list. It contains pointers to previous and
     * next list node and additional reference for value storage.
     */
    private static class ListNode {
    	/** Value of the node **/
        public Object value;
        /** Reference to previous node in list **/
        public ListNode previous;
        /** Reference to next node in list **/
        public ListNode next;

        /**
         * Constructor which initializes all the variables
         * @param value value of the node
         * @param previous reference to previous node in list
         * @param next reference to next node in list
         */
        public ListNode(Object value, ListNode previous, ListNode next) {
            this.value = value;
            this.previous = previous;
            this.next = next;
        }

        /**
         * Constructor which only sets the value of the node.
         * The references for previous and next node are set to null.
         * @param value
         */
        public ListNode(Object value) {
            this(value, null, null);
        }
    }

    /** Current size of collection (number of elements actually stored; number of nodes in list) **/
    private int size;
    /** Reference to the first node of the linked list **/
    private ListNode first;
    /** Reference to the last node of the linked list **/
    private ListNode last;

    /**
     * Default constructor, creates an empty collection
     */
    public LinkedListIndexedCollection() {
        this.first = this.last = null;
        this.size = 0;
    }

    /**
     * Creates a new collection based on a other collection by copying its elements.
     * @param other collection whose elements will be copied into this newly constructed collection
     * @throws NullPointerException the collection whose elements will be copied can not be <code>null</code>
     */
    public LinkedListIndexedCollection(Collection other) {
        this();
        if (other == null) throw new NullPointerException("The collection whose elements will be copied can not be null!");
        this.addAll(other);
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object into this collection at the end of the collection
     * @param value object to be added into this collection
     * @throws NullPointerException <code>null</code> can not be added to the collection
     */
    @Override
    public void add(Object value) {
        if (value == null) throw new NullPointerException("The object to be added can not be null!");

        this.size++;

        ListNode node = new ListNode(value);
        if (this.first == null) { // First node to be added
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
     * @param index index of the object to be return from this collection.
     * @return object at the given index in this collection
     * @throws IndexOutOfBoundsException valid indexes are 0 to size-1
     */
    public Object get(int index) {
        if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException("Valid indexed are 0 to size-1 (" + (this.size-1) + "), the index was: " + index + ".");

        if (index <= this.size / 2) {
            ListNode node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node.value;
        }

        ListNode node = last;
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
    public void insert(Object value, int position) {
        if (value == null) throw new NullPointerException("Object to be added can not be null!");
        if (position < 0 || position > size) throw new IndexOutOfBoundsException("Position must be between 0 and size (" + this.size + "), it was: " + position + ".");

        ListNode node = new ListNode(value);

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
            ListNode head = first;
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

        ListNode head = last;
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
     * Searches the collection and returns the index of the first occurrence of the given value
     * or -1 if the value is not found.
     * The average time complexity of this method is n.
     * @param value object to be searched for in linked-list
     * @return index of the first occurrence of the given value or -1 if the value is not found
     */
    public int indexOf(Object value) {
        if (value == null) return -1;
    	int index = 0;
        for (ListNode head = first; head != null; head = head.next) {
            if (head.value.equals(value))
                return index;
            index++;
        }
        return -1;
    }

    /**
     * Removes element at specified index from collection. Element that was previously located at
     * index+1 after this operation is on location index.
     * @param index index of the object to be removed
     * @throws IndexOutOfBoundsException valid indexes are from 0 to size-1
     */
    public void remove(int index) {
        if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException("Valid indexed are 0 to size1 (" + (this.size-1) + "), the index was: " + index + ".");

        this.size--;
        if (first == last) { // If there is only one node in the list
            first = last = null;
            return;
        }

        if (index == 0) { // If the first node needs to be deleted
            first = first.next;
            first.previous = null;
            return;
        }
        if (index == this.size) { // If the last node needs to be deleted
            last = last.previous;
            last.next = null;
            return;
        }

        ListNode head = first;
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
        for (ListNode head = first; head != null; head = head.next) {
            array[index++] = head.value;
        }
        return array;
    }

    @Override
    public void forEach(Processor processor) {
        for (ListNode head = first; head != null; head = head.next) {
            processor.process(head.value);
        }
    }

    @Override
    public void clear() {
        this.first = this.last = null;
        this.size = 0;
    }
}
