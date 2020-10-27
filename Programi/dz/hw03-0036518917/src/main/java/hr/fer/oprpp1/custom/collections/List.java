package hr.fer.oprpp1.custom.collections;

/**
 * This interface models a list. A list is a collection which has indexed positions.
 * 
 * @author sbolsec
 *
 */
public interface List<T> extends Collection<T> {
	
	/**
	 * Returns the object that is stored in the list at position <code>index</code>.
	 * 
	 * @param index position from which the object will be returned
	 * @return the object that is stored at position <code>index</code>
	 * @throws IndexOutOfBoundsException index must be between 0 and size-1
	 */
	T get(int index);
	
	/**
	 * Inserts the object at the specified position. It does not overwrite the given value at the given position, 
	 * it shifts the elements at greater positions one place toward the end.
	 * 
	 * @param value object to be inserted
	 * @param position index where the object should be inserted
	 * @throws NullPointerException <code>null</code> object will not be inserted into the list
	 * @throws IndexOutOfBoundsException index must be between 0 and size
	 */
	void insert(T value, int position);
	
	/**
	 * Searches the collection and returns the index of the first occurrence of the given value
	 * or -1 if the value is not found.
	 * 
	 * @param value object that will be searched
	 * @return index of the first occurrence of the given object or -1 if the value is not found
	 */
	int indexOf(Object value);
	
	/**
	 * Removes element at specified index from collection. 
	 * Element that was previously at location index+1 after this operation is on location index , etc.
	 * 
	 * @param index index at which the element should be removed
	 * @throws IndexOutOfBoundsException index must be between 0 and size-1
	 */
	void remove(int index);
}
