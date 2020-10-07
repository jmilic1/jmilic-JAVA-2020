package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which can output a tree starting at given folder. Command must
 * take a single argument which needs to be a directory.
 * 
 * @author Jura Milić
 *
 */
public class TreeShellCommand implements ShellCommand {
	/**
	 * List of Strings which describe Tree Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs a Tree Command with default description
	 */
	public TreeShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("tree - outputs all directories and files as a tree starting at given root");
		commandDescription.add("EXAMPLES");
		commandDescription.add("      tree a     Outputs a tree starting at folder a");
	}

	/**
	 * Executes Tree Command using given Environment and arguments
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
			throw new IllegalArgumentException("Invalid number of arguments");
		}

		Path path = Paths.get(args.get(0));

		if (path.toFile().isFile()) {
			env.writeln("Cannot output a tree of a file. Use directory instead.");
			return ShellStatus.CONTINUE;
		}

		/**
		 * FileVisitor implementation used for outputting a tree of a root folder
		 */
		FileVisitor<Path> visitor = new SimpleFileVisitor<>() {

			/**
			 * Current level in tree
			 */
			int level = 0;

			/**
			 * Upon visiting a file visitor outputs name of file and the appropriate number
			 * of spaces according to its level in tree.
			 * 
			 * @param file  current file
			 * @param attrs not used
			 * @return continue fileVisitor
			 * @throws IOException
			 */
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < level; i++) {
					sb.append("  ");
				}

				sb.append(file.getFileName());
				env.writeln(sb.toString());
				return FileVisitResult.CONTINUE;
			}

			/**
			 * Upon visiting a directory FileVisitor outputs name of directory and a certain
			 * number of spaces according to its level in tree
			 * 
			 * @param dir   current directory
			 * @param attrs not used
			 * @return continue fileVisitor
			 * @throws IOException
			 */
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				level++;
				StringBuilder sb = new StringBuilder();

				for (int i = 1; i < level; i++) {
					sb.append("  ");
				}
				sb.append(dir.getFileName());
				env.writeln(sb.toString());

				return FileVisitResult.CONTINUE;
			}

			/**
			 * Adjusts number of current level
			 * 
			 * @param dir current directory
			 * @param exc not used
			 * @return continue FileVisitor
			 * @throws IOException
			 */
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				level--;

				return FileVisitResult.CONTINUE;
			}

		};

		try {
			Files.walkFileTree(path, visitor);
		} catch (IOException e) {
			env.writeln("Error while walking file tree");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * @return name of Tree Command
	 */
	@Override
	public String getCommandName() {
		return "tree";
	}

	/**
	 * @return description of Tree Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

}
