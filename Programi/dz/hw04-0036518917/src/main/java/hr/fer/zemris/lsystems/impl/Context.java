package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.custom.collections.EmptyStackException;
import hr.fer.zemris.custom.collections.ObjectStack;

/**
 * This class allows the procedure of fractal representation to be performed
 * @author sbolsec
 *
 */
public class Context {

	/** Stack which holds all the turtle states **/
	private ObjectStack<TurtleState> stack = new ObjectStack<>();
	
	/**
	 * Returns the current turtle state on top of the stack without removing
	 * it from the top of the stack.
	 * @return current turtle state on top of the stack
	 * @throws EmptyStackException the stack was empty
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}
	
	/**
	 * Adds the turtle state to the top of the stack.
	 * @param state turtle state to be added to the stack
	 * @throws NullPointerException the object to be added can not be null
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}
	
	/**
	 * Removes a turtle state from the top of the stack.
	 * @throws EmptyStackException the stack was empty
	 */
	public void popState() {
		stack.pop();
	}
}
