package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which outputs information about all files and directories inside
 * a certain directory. Command outputs file's/folder's permissions, their size,
 * their time of creation and their name. Given argument must be a directory.
 * 
 * @author Jura Milić
 *
 */
public class LsShellCommand implements ShellCommand {
	/**
	 * List of Strings that describe the Ls Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs an Ls Command with default description.
	 */
	public LsShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("ls - lists information about the files/directories in a directory");
		commandDescription.add("EXAMPLE");
		commandDescription.add("     ls folderName    outputs information of all objects inside folderName");
	}

	/**
	 * Executes Ls Command using given Environment and arguments.
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
			env.writeln("ls command must take only one argument.");
		} else {
			Path root = Paths.get(args.get(0));
			
			if (root.toFile().isFile()) {
				env.writeln("Cannot use Ls with a file. Use directory instead");
			} else {
				if (root.toFile().exists()) {
					StringBuilder sb = new StringBuilder();

					if (Files.isDirectory(root)) {
						File dir = root.toFile();
						File[] files = dir.listFiles();

						for (File file : files) {
							Path path = file.toPath();
							if (file.isDirectory()) {
								sb.append("d");
							} else {
								sb.append("-");
							}
							sb.append(getPermissions(file.toPath()) + getSize(file.toPath()) + " "
									+ getTime(file.toPath()) + " " + path.getFileName() + "\n");
						}
					} else {
						sb.append(getPermissions(root) + getSize(root) + getTime(root) + root.getFileName());
					}
					env.write(sb.toString());
				} else {
					env.writeln("Unable to find file/directory.");
				}
			}
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Generates a String with information of a given file's permissions.
	 * 
	 * @param file given file
	 * @return generated String
	 */
	private String getPermissions(Path file) {
		StringBuilder sb = new StringBuilder();
		if (Files.isReadable(file)) {
			sb.append("r");
		} else {
			sb.append("-");
		}
		if (Files.isWritable(file)) {
			sb.append("w");
		} else {
			sb.append("-");
		}
		if (Files.isExecutable(file)) {
			sb.append("x");
		} else {
			sb.append("-");
		}
		return sb.toString();
	}

	/**
	 * Generates a String with information of a given file's creation time
	 * 
	 * @param file given file
	 * @return generated String
	 */
	private String getTime(Path file) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(file, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);

		BasicFileAttributes attributes = null;
		try {
			attributes = faView.readAttributes();
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to read file time.");
		}

		FileTime fileTime = attributes.creationTime();
		return sdf.format(new Date(fileTime.toMillis()));
	}

	/**
	 * Generates a String with information of given file's size. The String is
	 * shifted to the right in a text box that is 10 spaces long.
	 * 
	 * @param file given file
	 * @return generated String
	 */
	private String getSize(Path file) {
		String str = "";
		try {
			str = Long.toString(Files.size(file));
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to read file size.");
		}
		int len = str.length();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10 - len; i++) {
			sb.append(" ");
		}
		sb.append(str);
		return sb.toString();
	}

	/**
	 * @return name of ls Command
	 */
	@Override
	public String getCommandName() {
		return "ls";
	}

	/**
	 * @return description of ls Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

}
