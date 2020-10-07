package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used for parsing a user input. The input contains command name but the
 * Parser ignores it and reads following arguments.
 * <p>
 * 
 * As the Shell is mostly used to work with files and directories (such as
 * /home/Desktop/file.txt) escaping is only used within quotation marks. Within
 * quotation marks, input "\\" results in "\" and input "\"" results in """. \"
 * is part of file path and not the end of quotations. All other combinations of
 * \ and any other character results in two characters: \ and the aforementioned
 * character.
 * 
 * @author Jura Milić
 *
 */
public class ShellParser {
	/**
	 * List of parsed arguments
	 */
	private List<String> arguments;
	/**
	 * Array of characters from parsable input.
	 */
	private char[] data;
	/**
	 * Index of next character to be read.
	 */
	private int currentIndex;

	/**
	 * Constructs a ShellParser which immediately starts reading the given input
	 * 
	 * @param input given input
	 * @throws NullPointerException if given input is NULL
	 */
	public ShellParser(String input) {
		if (input == null) {
			throw new NullPointerException("Given text was NULL.");
		}

		data = input.toCharArray();
		int index = input.indexOf(' ');
		arguments = new ArrayList<>();

		if (index != -1) {
			currentIndex = index;
			parse();
		}
	}

	/**
	 * Method which reads whole input and generates arguments which Shell Commands
	 * can work with.
	 */
	private void parse() {
		while (currentIndex != data.length) {
			if (currentIndex == data.length) {
				return;
			}

			char c = data[currentIndex];
			if (c == ' ') {
				currentIndex++;
			} else {
				if (c == '"') {
					currentIndex++;
					arguments.add(readString());
				} else {
					arguments.add(readWord());
				}
			}
		}
	}

	/**
	 * @return parsed arguments
	 */
	public List<String> getArgs() {
		return arguments;
	}

	/**
	 * Reads an argument surrounded by quotation marks from char array.
	 * 
	 * @return Parsed quoted argument
	 * @throws IllegalArgumentException if quote was not closed before end of input
	 *                                  or if space does not follow the end of
	 *                                  quotations
	 */
	private String readString() {
		StringBuilder sb = new StringBuilder();

		while (data[currentIndex] != '"') {
			char c = data[currentIndex];

			if (c == '\\') {
				if (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '"') {
					c = data[++currentIndex];
				}
			}
			sb.append(c);
			currentIndex++;
			if (currentIndex >= data.length) {
				throw new IllegalArgumentException("Quotations were not closed before end of data input");
			}
		}

		if (currentIndex + 1 < data.length) {
			if (data[currentIndex + 1] != ' ') {
				throw new IllegalArgumentException("End of quotations was not followed by empty space.");
			}
		}
		currentIndex++;
		return sb.toString();
	}

	/**
	 * Reads an argument from char array.
	 * 
	 * @return parsed argument
	 */
	private String readWord() {
		StringBuilder sb = new StringBuilder();

		char c = data[currentIndex];

		while (c != ' ' && currentIndex != data.length - 1) {
			sb.append(c);
			c = data[++currentIndex];
		}

		if (c != ' ') {
			sb.append(c);
			currentIndex++;
		}
		return sb.toString();
	}
}
