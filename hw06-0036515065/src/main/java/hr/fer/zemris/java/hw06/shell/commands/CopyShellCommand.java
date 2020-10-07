package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which copies a file's contents. Source must not be a directory.
 * If it is a directory, an appropriate message is outputted.
 * <p>
 * If destination is a directory the copied contents will be written into a new
 * file with the same name as the source file inside the directory. If
 * destination is a file that already exists, the user is asked if they wish to
 * overwrite the file.
 * 
 * @author Jura Milić
 *
 */
public class CopyShellCommand implements ShellCommand {
	/**
	 * List of Strings which describe Copy Command.
	 */
	private List<String> commandDescription;

	/**
	 * Constructs a Copy Command with default description.
	 */
	public CopyShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("copy - copy a file's contents into a new file.");
		commandDescription.add("EXAMPLES");
		commandDescription
				.add("      copy a.txt b.txt        Creates a new file b.txt with a copy of contents from a.txt");
		commandDescription.add(
				"      copy a.txt folderName   Creates a new file a.txt in folderName with a copy of contents from original a.txt");
	}

	/**
	 * Executes Copy Command using given environment and arguments.
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

		if (args.size() != 2) {
			env.writeln("Copy command must take two arguments.");
		} else {

			File source = new File(args.get(0));
			File dest = new File(args.get(1));

			if (source.isDirectory()) {
				env.writeln("Source of copy must not be directory! Insert a file name instead.");
			} else {
				if (dest.exists()) {
					if (dest.isFile()) {
						env.writeln("Destination file already exists. Do you want to overwrite it? (Y/N)");
						env.write(Character.toString(env.getPromptSymbol()));
						String answer = env.readLine().toUpperCase();
						if (answer.equals("N")) {
							env.writeln("File will not be overwritten. Copy command was terminated.");
							return ShellStatus.CONTINUE;
						}
						if (!answer.equals("Y")) {
							env.writeln("Answer was not understandable. Copy command was terminated.");
							return ShellStatus.CONTINUE;
						}

						env.writeln("Overwriting file.");

					} else {
						dest = new File(dest.toString() + "/" + source.getName());
					}
				}
				try {
					dest.createNewFile();
				} catch (IOException e) {
					env.writeln("Unable to create new File");
					return ShellStatus.CONTINUE;
				}

				try (FileInputStream is = new FileInputStream(source);
						FileOutputStream os = new FileOutputStream(dest)) {
					while (is.available() > 0) {
						byte[] buf = new byte[2048];
						buf = is.readNBytes(2048);
						os.write(buf);
					}
				} catch (FileNotFoundException e) {
					env.writeln("Could not find source file");
				} catch (IOException e) {
					env.writeln("Error while reading/writing.");
				}
			}
		}
		return ShellStatus.CONTINUE;

	}

	/**
	 * @return name of Copy Command
	 */
	@Override
	public String getCommandName() {
		return "copy";
	}

	/**
	 * @return description of Copy Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}
}
