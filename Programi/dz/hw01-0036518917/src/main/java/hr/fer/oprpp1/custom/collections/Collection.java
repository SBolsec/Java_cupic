package hr.fer.oprpp1.custom.collections;

/**
 * This is a generic Collection which defines all the methods of a collection
 * 
 * @author sbolsec
 *
 */
public class Collection {

	/**
	 * Creates a new insance of the Collection class
	 */
	protected Collection() {}
	
	/**
	 * Returns true if collection contains no objects and false otherwise.
	 * 
	 * @return true if collection contains no objects and false otherwise
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in this collections.
	 * 
	 * @return number of objects in the collection
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value object to be added into this collection
	 */
	public void add(Object value) {
		// Does nothing
	}
	
	/**
	 * Returns true only if the collection contains given value, as determined by equals method.
	 * 
	 * @param value object to be tested
	 * @return true if the object is in this collection, false otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Returns true only if the collection contains given value as determined by equals method and 
	 * removes one occurrence of it (in this class it is not specified which one).
	 * 
	 * @param value object to be removed from the collection
	 * @return returns true if the collection contains the object and removes it, false otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array with size equals to the size of this collection, fills it with collection
	 * content and returns the array. This method never returns null.
	 * 
	 * @return array made from the collection
	 * @throws UnsupportedOperationException if there is no implementation of this method
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method calls processor.process(.) for each element of this collection. 
	 * The order in which elements will be sent is undefined in this class.
	 * 
	 * @param processor processor which will be used to process all of the items in the collection
	 */
	public void forEach(Processor processor) {
		// Does nothing
	}
	
	/**
	 * Method adds into the current collection all elements from the given collection.
	 * This other collection remains unchanged
	 * 
	 * @param other collection whose items will be added to this collection
	 */
	public void addAll(Collection other) {
		class AddProcessor extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new AddProcessor());
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	public void clear() {
		// Does nothing
	}
}
