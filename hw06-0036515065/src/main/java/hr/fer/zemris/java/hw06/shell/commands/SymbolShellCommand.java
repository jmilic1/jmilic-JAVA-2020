package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which can change the Environment's PROMPT, MORELINES and
 * MULTILINE symbols or output their values. SymbolCommand can take one or two
 * arguments. Only legal first arguments are "PROMPT", "MORELINES" and
 * "MULTILINE". The first argument defines which symbol the command will execute
 * upon.
 * <p>
 * 
 * If there is no second argument, command will output the current value of
 * specified symbol. Legal second arguments are only single characters. If a
 * second argument is given, command will change the specified symbol to the
 * specified character.
 * 
 * @author Jura Milić
 *
 */
public class SymbolShellCommand implements ShellCommand {
	/**
	 * List of Strings which describe Symbol Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs Symbol Command with default description.
	 */
	public SymbolShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("symbol - outputs the current value of a symbol or changes it's value");
		commandDescription.add("EXAMPLE");
		commandDescription.add("     symbol MORELINES      outputs the current value of MORELINES");
		commandDescription.add("     symbol PROMPT #       changes the symbol of PROMPT to '#'");
		commandDescription.add("     symbol MULTILINE      outputs the current value of MULTILINE");
	}

	/**
	 * Executes Symbol Command using given Environment and arguments
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
		if (args.size() < 1 || args.size() > 2) {
			env.writeln("Invalid number of arguments for symbol command.");
		} else {
			String name = args.get(0);

			if (args.size() == 2) {
				String str = args.get(1);

				if (str.length() != 1) {
					env.writeln("Symbol too long!");
					return ShellStatus.CONTINUE;
				}
				char symbol = str.charAt(0);

				changeSymbol(name, symbol, env);
			} else {
				outputSymbol(name, env);
			}
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Changes the symbol of given Environment to given character.
	 * 
	 * @param symbolName name of symbol to be changed
	 * @param newSymbol  new symbol
	 * @param env        given Environment
	 */
	private void changeSymbol(String symbolName, char newSymbol, Environment env) {
		char oldSymbol = '0';

		switch (symbolName) {
		case ("PROMPT"):
			oldSymbol = env.getPromptSymbol();
			env.setPromptSymbol(newSymbol);
			break;
		case ("MULTILINE"):
			oldSymbol = env.getMultilineSymbol();
			env.setMultilineSymbol(newSymbol);
			break;
		case ("MORELINES"):
			oldSymbol = env.getMorelinesSymbol();
			env.setMorelinesSymbol(newSymbol);
			break;
		default:
			env.writeln("Invalid symbol name");
			return;
		}
		env.writeln("Symbol for " + symbolName + "changed from '" + oldSymbol + "' to '" + newSymbol + "'");
	}

	/**
	 * Outputs the current symbol of given Environment
	 * 
	 * @param symbolName name of symbol
	 * @param env        given Environment
	 */
	private void outputSymbol(String symbolName, Environment env) {
		char symbol = '0';
		switch (symbolName) {
		case ("PROMPT"):
			symbol = env.getPromptSymbol();
			break;
		case ("MULTILINE"):
			symbol = env.getMultilineSymbol();
			break;
		case ("MORELINES"):
			symbol = env.getMorelinesSymbol();
			break;
		default:
			env.writeln("Invalid symbol name");
			return;
		}
		env.writeln("Symbol for " + symbolName + " is '" + symbol + "'");
	}

	/**
	 * @return name of Symbol Command
	 */
	@Override
	public String getCommandName() {
		return "symbol";
	}

	/**
	 * @return description of Symbol Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}

}
