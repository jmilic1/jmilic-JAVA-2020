package hr.fer.zemris.java.hw06.shell;

import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.*;

/**
 * Program which offers simple Shell functionality. Program uses an Environment
 * implementation which reads user input from console and writes messages to
 * user through console. Shell offers multiple commands: exit, help, cat,
 * charsets, copy, hexdump, ls, mkdir, symbol and tree. Command descriptions can
 * be accessed by entering "help CommandName" into console. Program terminates
 * upon inputting exit command.
 * 
 * @author Jura Milić
 *
 */
public class MyShell {
	/**
	 * Current version of MyShell
	 */
	private static final double SHELL_VERSION = 1.0;

	/**
	 * Simple Implementation of Environment which reads input from console and
	 * outputs messages to user into console
	 * 
	 * @author Jura Milić
	 *
	 */
	static class ShellEnvironment implements Environment {

		/**
		 * A map that stores commands.
		 */
		private SortedMap<String, ShellCommand> commands;
		/**
		 * Current value of PROMPTSYMBOL
		 */
		Character promptSymbol;
		/**
		 * Current value of MORELINESSYMBOL
		 */
		Character moreLinesSymbol;
		/**
		 * Current value of MULTILINESYMBOL
		 */
		Character multiLineSymbol;
		/**
		 * Scanner used for reading input.
		 */
		Scanner sc;

		public ShellEnvironment(SortedMap<String, ShellCommand> commands) {
			this.commands = commands;
			promptSymbol = '>';
			moreLinesSymbol = '\\';
			multiLineSymbol = '|';
			sc = new Scanner(System.in);
		}

		@Override
		public String readLine() throws ShellIOException {
			try {
				return sc.nextLine();
			} catch (Exception ex) {
				throw new ShellIOException();
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			System.out.print(text);
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			System.out.println(text);
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Character getMultilineSymbol() {
			return multiLineSymbol;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multiLineSymbol = symbol;
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			promptSymbol = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return moreLinesSymbol;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			moreLinesSymbol = symbol;
		}
	}

	/**
	 * Generates and initializes a new ShellEnvironment with default commands.
	 * 
	 * @return generated Environment
	 */
	private static ShellEnvironment initEnvironment() {
		SortedMap<String, ShellCommand> commands = new TreeMap<>();

		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("symbol", new SymbolShellCommand());

		return new ShellEnvironment(commands);
	}

	/**
	 * The method where program for MyShell starts. Program will stay in a constant
	 * loop of reading and executing commands until user calls "exit" command.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		ShellEnvironment env = initEnvironment();
		System.out.println("Welcome to MyShell v " + SHELL_VERSION);

		ShellStatus status = ShellStatus.CONTINUE;
		while (status == ShellStatus.CONTINUE) {
			env.write(Character.toString(env.getPromptSymbol()) + " ");

			String strInput = env.readLine();

			while (strInput.endsWith(Character.toString(env.getMorelinesSymbol()))) {
				env.write(Character.toString(env.getMultilineSymbol()) + " ");
				strInput = strInput.substring(0, strInput.length() - 1);
				strInput += env.readLine();
			}

			int index = strInput.indexOf(" ");
			ShellCommand command = null;

			if (index == -1) {
				command = env.commands.get(strInput);
			} else {
				command = env.commands.get(strInput.substring(0, index));
			}
			if (command == null) {
				env.writeln("Invalid command.");
			} else {
				status = command.executeCommand(env, strInput);
			}
		}
	}
}
