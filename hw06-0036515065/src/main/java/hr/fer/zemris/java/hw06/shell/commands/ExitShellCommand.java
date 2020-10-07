package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which terminates the program. Command takes no arguments but
 * command will act no differently if arguments are given.
 * 
 * @author Jura Milić
 *
 */
public class ExitShellCommand implements ShellCommand {
	/**
	 * List of String which describe Exit Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs an Exit Command with default description.
	 */
	public ExitShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("exit - Terminates shell and exits program.");
		commandDescription.add("EXAMPLES");
		commandDescription.add("      exit    Terminates Shell");
	}

	/**
	 * Executes Shell Command using given environment.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
	}

	/**
	 * @return name of Exit Command
	 */
	@Override
	public String getCommandName() {
		return "exit";
	}

	/**
	 * @return description of Exit Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}
}
