package hr.fer.oprpp1.custom.collections;

/**
 * This is a <code>Adaptor</code> class, it adapts the class <code>ArrayIndexedCollection</code> so that
 * it can be used as a stack.
 * @author sbolsec
 *
 */
public class ObjectStack<T> {

	/** Internal array collection which stores the objects **/
    private ArrayIndexedCollection<T> stack;

    /**
     * Default constructor which initializes the stack
     */
    public ObjectStack() {
        this.stack = new ArrayIndexedCollection<T>();
    }

    /**
     * Returns true if this stack contains no objects and false otherwise.
     * @return true if this stack contains no objects and false otherwise
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Returns the number of currently stored objects in this stack.
     * @return number of objects in this stack
     */
    public int size() {
        return stack.size();
    }

    /**
     * Pushes given value on the stack. <code>null</code> is not allowed.
     * @param value object to be pushed to stack
     */
    public void push(T value) {
        stack.add(value);
    }

    /**
     * Removes last value pushed on stack from stack and returns it.
     * @return last value pushed on stack
     * @throws EmptyStackException the stack was empty
     */
    public T pop() {
        if (stack.size() == 0) throw new EmptyStackException("The stack was empty!");

        T result = stack.get(size() - 1);
        stack.remove(size() - 1);
        return result;
    }

    /**
     * Returns last element placed on stack but does not delete it from stack.
     * @return last element placed on stack
     * @throws EmptyStackException the stack was empty
     */
    public T peek() {
        if (stack.size() == 0) throw new EmptyStackException("The stack was empty!");

        return stack.get(size() - 1);
    }

    /**
     * Removes all elements from this stack.
     */
    public void clear() {
        stack.clear();
    }
}
