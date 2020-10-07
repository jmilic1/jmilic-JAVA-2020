package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which outputs contents of a file to the user. If a directory
 * is given, Command will not output it's contents but instead will output an
 * appropriate message. Cat can also take a charset name which it will use to
 * output the file. If no charset is given, a default charset is used.
 * 
 * @author Jura Milić
 *
 */
public class CatShellCommand implements ShellCommand {
	/**
	 * Default Charset used for interpeting a file's contents.
	 */
	private static final Charset DEFAULT_CHARSET = java.nio.charset.Charset.defaultCharset();
	/**
	 * List of Strings which describe Cat Command.
	 */
	private List<String> commandDescription;

	/**
	 * Constructs a Cat Command with default description.
	 */
	public CatShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("cat - concatenate a file to output using given Charset.");
		commandDescription.add("If no CHARSET is given, use default Charset: " + DEFAULT_CHARSET);
		commandDescription.add("EXAMPLES");
		commandDescription.add("      cat a.txt x-windows-950");
		commandDescription.add("                         Outputs content from a.txt using charset x-windows-950.");
		commandDescription.add("      cat a.txt          Outputs content from a.txt using default Charset.");
	}

	/**
	 * Executes Cat Command using given environment and given arguments
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ShellParser parser = null;
		try {
			parser = new ShellParser(arguments);
		} catch (Exception ex) {
			env.write("Error reading input: ");
			env.writeln(ex.getMessage());
			return ShellStatus.CONTINUE;
		}
		List<String> args = parser.getArgs();

		if (args.size() < 1 || args.size() > 2) {
			env.writeln("Invalid number of arguments");
		} else {
			File file = new File(args.get(0));
			if (file.isDirectory()) {
				env.write("Cat command cannot read directories. Insert a file name instead.");
			} else {
				Charset cs = null;
				if (args.size() == 2) {
					try {
						cs = Charset.forName(args.get(1));
					} catch (UnsupportedCharsetException ex) {
						env.writeln("Invalid charset name.");
						return ShellStatus.CONTINUE;
					}
				} else {
					cs = DEFAULT_CHARSET;
				}

				try (BufferedReader reader = Files.newBufferedReader(file.toPath(), cs)) {
					while (reader.ready()) {
						env.writeln(reader.readLine());
					}
				} catch (IOException e) {
					env.writeln("Error reading file.");
				}
			}
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * @return name of Cat Command.
	 */
	@Override
	public String getCommandName() {
		return "cat";
	}

	/**
	 * @return description of Cat Command.
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}
}
