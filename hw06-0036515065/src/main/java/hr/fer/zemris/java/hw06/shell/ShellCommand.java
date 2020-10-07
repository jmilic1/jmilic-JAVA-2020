package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Simple interface which defines basic methods for a Command that can be used
 * in Shell.
 * 
 * @author Jura Milić
 *
 */
public interface ShellCommand {
	/**
	 * Executes the Command based on implementation. If Command communicates if user
	 * it will do so through given Environment. If Command takes arguments it will
	 * take them from given String.
	 * 
	 * @param env       given environment
	 * @param arguments given arguments
	 * @return Status of Shell after execution
	 */
	ShellStatus executeCommand(Environment env, String arguments);

	/**
	 * @return Name of Command
	 */
	String getCommandName();

	/**
	 * @return Description of Command.
	 */
	List<String> getCommandDescription();
}
