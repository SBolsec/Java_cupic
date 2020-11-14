package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This class allows you to iterate over a collection element by element
 * @author sbolsec
 *
 */
public interface ElementsGetter {
	
	/**
	 * Checks whether the collection has another item.
	 * @return true if collection has another item or false otherwise
	 * @throws ConcurrentModificationException if the collection was changed
	 */
	boolean hasNextElement();
	
	/**
	 * Returns the next element in the collection.
	 * @return returns the next element in the collection
	 * @throws NoSuchElementException if there isn't more elements in the collection
	 * @throws ConcurrentModificationException if the collection was changed
	 */
	Object getNextElement();
	
	/**
	 * Calls the given processor on all remaining elements of the collection.
	 * @param p processor which will be applied to all remaining elements
	 * @throws ConcurrentModificationException if the collection was changed
	 */
	default void processRemaining(Processor p) {
		while (hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
