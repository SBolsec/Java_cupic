package hr.fer.oprpp1.custom.collections;

/**
 * This is a generic Collection which defines all the methods of a collection
 * 
 * @author sbolsec
 *
 */
public interface Collection<T> {
	
	/**
	 * Returns true if collection contains no objects and false otherwise.
	 * 
	 * @return true if collection contains no objects and false otherwise
	 */
	default boolean isEmpty() {
		return this.size() == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in this collections.
	 * 
	 * @return number of objects in the collection
	 */
	int size();
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value object to be added into this collection
	 * @throws NullPointerException <code>null</code> can not be added to the collection
	 */
	void add(T value);
	
	/**
	 * Returns true only if the collection contains given value, as determined by equals method.
	 * 
	 * @param value object to be tested
	 * @return true if the object is in this collection, false otherwise
	 */
	boolean contains(Object value);
	/**
	 * Returns true only if the collection contains given value as determined by equals method and 
	 * removes one occurrence of it.
	 * 
	 * @param value object to be removed from the collection
	 * @return returns true if the collection contains the object and removes it, false otherwise
	 */
	boolean remove(Object value);
	
	/**
	 * Allocates new array with size equals to the size of this collection, fills it with collection
	 * content and returns the array. This method never returns null.
	 * 
	 * @return array made from the collection
	 */
	Object[] toArray();
	
	/**
	 * Method calls processor.process(.) for each element of this collection. 
	 * 
	 * @param processor processor which will be used to process all of the items in the collection
	 */
	default void forEach(Processor<? super T> processor) {
		ElementsGetter<T> getter = createElementsGetter();
		while (getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}
	
	/**
	 * Method adds into the current collection all elements from the given collection.
	 * This other collection remains unchanged
	 * 
	 * @param other collection whose items will be added to this collection
	 */
	default void addAll(Collection<? extends T> other) {
		class AddProcessor implements Processor<T> {
			@Override
			public void process(T value) {
				add(value);
			}
		}
		
		other.forEach(new AddProcessor());
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	void clear();
	
	/**
	 * Creates a new ElementsGetter for this collection which allows you to iterate through this 
	 * collection element by element.
	 * 
	 * @return returns object which allows to iterate through collection
	 */
	ElementsGetter<T> createElementsGetter();
	
	/**
	 * Adds the elements from the given collection only if they are acceptable by the given tester.
	 * 
	 * @param col collection to be added
	 * @param tester will test which elements of the collection to add
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		ElementsGetter<? extends T> getter = col.createElementsGetter();
		while (getter.hasNextElement()) {
			T el = getter.getNextElement();
			if (tester.test(el)) {
				add(el);
			}
		}
	}
}
