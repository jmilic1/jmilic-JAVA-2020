package hr.fer.zemris.java.hw06.shell;

/**
 * Enumeration used by Shell to read what state it should be in after executing
 * a command.
 * 
 * @author Jura Milić
 *
 */
public enum ShellStatus {
	/**
	 * If Shell is in this state, it will continue to take Commands from user.
	 */
	CONTINUE,
	/**
	 * If Shell is in this state, it will terminate.
	 */
	TERMINATE
}
