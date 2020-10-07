package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which offers information about commands. Command can take one or
 * no arguments. If no arguments are given, the command will output a list of
 * available commands. If one argument is given, it should be a name of a
 * command and Help Command will output the description of given command.
 * 
 * @author Jura Milić
 *
 */
public class HelpShellCommand implements ShellCommand {
	/**
	 * List of Strings which describe Help Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs Help Command with default description.
	 */
	public HelpShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("help - displays information about available commands");
		commandDescription.add("EXAMPLES");
		commandDescription.add("      help      Lists all available commands");
		commandDescription.add("      help cat  Outputs a description of cat");
	}

	/**
	 * Executes Help Command using given Environment and arguments.
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

		if (args.size() > 1) {
			env.write("Help command must not take more than one argument.");
		} else {
			if (args.size() == 0) {
				env.write("Available commands: ");
				for (Entry<String, ShellCommand> entry : env.commands().entrySet()) {
					env.write(entry.getValue().getCommandName() + " ");
				}
				env.writeln("");
			} else {
				String commandName = args.get(0);

				List<String> desc = env.commands().get(commandName).getCommandDescription();
				if (desc == null) {
					env.writeln("Command does not exist.");
				} else {
					for (String str : desc) {
						env.writeln(str);
					}
				}
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * @return name of Help Command
	 */
	@Override
	public String getCommandName() {
		return "help";
	}

	/**
	 * @return description of Help Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

}
