package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which makes a new directory or multiple directories if they do
 * not already exist. If a path to a folder is given and certain parent folders
 * do not exist, they will be created.
 * 
 * @author Jura Milić
 *
 */
public class MkdirShellCommand implements ShellCommand {
	/**
	 * List of Strings which describe Mkdir Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs a Mkdir Command with default description
	 */
	public MkdirShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("mkdir - create the directory or directories if they do not already exist.");
		commandDescription.add("EXAMPLE");
		commandDescription.add("     mkdir a        Creates folder a");
		commandDescription.add("     mkdir a/b/c    Creates folder a, then folder b, and finally folder c");
	}

	/**
	 * Executes Mkdir Command using given Environment and arguments.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ShellParser parser = null;
		try {
			parser = new ShellParser(arguments);
		} catch (Exception ex) {
			env.write("Error while reading input: ");
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		List<String> args = parser.getArgs();

		if (args.size() != 1) {
			env.write("Invalid number of arguments.");
		} else {
			File file = new File(args.get(0));
			file.mkdirs();
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * @return name of Mkdir Command
	 */
	@Override
	public String getCommandName() {
		return "mkdir";
	}

	/**
	 * @return description of Mkdir command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

}
