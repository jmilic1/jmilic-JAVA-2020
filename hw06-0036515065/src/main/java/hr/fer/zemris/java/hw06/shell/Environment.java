package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Interface which offers communication between Shell and user. Implementations
 * of Environment store Multiline, Prompt and Morelines symbols for which
 * getters and setters need to be defined.
 * 
 * @author Jura Milić
 *
 */
public interface Environment {
	/**
	 * Reads one line from user input.
	 * 
	 * @return Read input
	 * @throws ShellIOException if an error occurs during reading
	 */
	String readLine() throws ShellIOException;

	/**
	 * Sends a given String to user.
	 * 
	 * @param text given String
	 * @throws ShellIOException if an error occurs during sending
	 */
	void write(String text) throws ShellIOException;

	/**
	 * Sends a given String to user and sends a new line character.
	 * 
	 * @param text given String
	 * @throws ShellIOException if an error occurs during sending
	 */
	void writeln(String text) throws ShellIOException;

	/**
	 * @return Commands offered by environment
	 */
	SortedMap<String, ShellCommand> commands();

	/**
	 * @return Multiline symbol
	 */
	Character getMultilineSymbol();

	/**
	 * @param symbol Multiline symbol
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * @return Prompt symbol
	 */
	Character getPromptSymbol();

	/**
	 * @param symbol Prompt symbol
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * @return Morelines symbol
	 */
	Character getMorelinesSymbol();

	/**
	 * @param symbol Morelines symbol
	 */
	void setMorelinesSymbol(Character symbol);
}
