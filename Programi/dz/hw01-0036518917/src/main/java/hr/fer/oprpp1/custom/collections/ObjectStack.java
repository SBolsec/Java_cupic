package hr.fer.oprpp1.custom.collections;

/**
 * This is a <code>Adaptor</code> class, it adapts the class <code>ArrayIndexedCollection</code> so that
 * it can be used as a stack.
 *
 * @author sbolsec
 *
 */
public class ObjectStack {

    private ArrayIndexedCollection stack;

    public ObjectStack() {
        this.stack = new ArrayIndexedCollection();
    }

    /**
     * Returns true if this stack contains no objects and false otherwise.
     *
     * @return true if this stack contains no objects and false otherwise
     */
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    /**
     * Returns the number of currently stored objects in this stack.
     *
     * @return number of objects in this stack
     */
    public int size() {
        return this.stack.size();
    }

    /**
     * Pushes given value on the stack. <code>null</code> is not allowed.
     *
     * @param value object to be pushed to stack
     */
    public void push(Object value) {
        this.stack.add(value);
    }

    /**
     * Removes last value pushed on stack from stack and returns it.
     *
     * @return last value pushed on stack
     * @throws EmptyStackException the stack was empty
     */
    public Object pop() {
        if (this.stack.size() == 0) throw new EmptyStackException("The stack was empty!");

        Object result = this.stack.get(this.stack.size()-1);
        this.stack.remove(this.stack.size()-1);
        return result;
    }

    /**
     * Returns last element placed on stack but does not delete it from stack.
     *
     * @return last element placed on stack
     * @throws EmptyStackException the stack was empty
     */
    public Object peek() {
        if (this.stack.size() == 0) throw new EmptyStackException("The stack was empty!");

        return this.stack.get(this.stack.size()-1);
    }

    /**
     * Removes all elements from this stack.
     */
    public void clear() {
        this.stack.clear();
    }
}
