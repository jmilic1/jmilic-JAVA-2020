package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Class which stores TurtleStates in an ObjectStack. Current state of turtle is
 * the TurtleState at the top of ObjectStack.
 * 
 * @author Jura Milić
 *
 */
public class Context {
	/**
	 * Stack which stores TurtleStates
	 */
	private ObjectStack<TurtleState> stack;

	/**
	 * Constructs Context with an empty instance of ObjectStack.
	 */
	public Context() {
		stack = new ObjectStack<>();
	}

	/**
	 * Gets the TurtleState at the top of stack without erasing it from stack.
	 * 
	 * @return last added TurtleState
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}

	/**
	 * Pushes the given TurtleState to stack.
	 * 
	 * @param state given TurtleState
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}

	/**
	 * Pops the last added TurtleState from stack.
	 */
	public void popState() {
		stack.pop();
	}
}
