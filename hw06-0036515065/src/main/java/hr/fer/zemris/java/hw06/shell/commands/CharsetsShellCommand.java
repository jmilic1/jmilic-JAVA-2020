package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A ShellCommand which outputs names of all charsets supported by this Java
 * platform. One charset is outputted per line. These charsets can all be used
 * for ShellCommands which take charsets as arguments (e.g. Cat). Charsets
 * Command takes no arguments but it will not work any differently when
 * arguments are given.
 * 
 * @author Jura Milić
 *
 */
public class CharsetsShellCommand implements ShellCommand {
	/**
	 * List of Strings which describe Charsets Command.
	 */
	private List<String> commandDescription;

	/**
	 * Constructs Charsets Command with default description.
	 */
	public CharsetsShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("charsets - lists all charsets that can be used.");
		commandDescription.add("EXAMPLES");
		commandDescription.add("      charsets    Outputs content from a.txt using default Charset.\");");
	}

	/**
	 * Executes Charsets using given Environment.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		SortedMap<String, Charset> chars = Charset.availableCharsets();
		for (SortedMap.Entry<String, Charset> entry : chars.entrySet()) {
			env.writeln(entry.getValue().toString());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * @return name of Charsets Command
	 */
	@Override
	public String getCommandName() {
		return "charsets";
	}

	/**
	 * @return description of Charsets Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}
}
