package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellParser;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * ShellCommand which outputs a file's contents along with hexadecimal
 * representations of the content. Command takes one argument which must be a
 * file.
 * 
 * @author Jura Milić
 *
 */
public class HexdumpShellCommand implements ShellCommand {
	/**
	 * List of Strings which contain description of Hexdump Command
	 */
	private List<String> commandDescription;

	/**
	 * Constructs a hexdump command with a default description.
	 */
	public HexdumpShellCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("hexdump - produces a hex-output of file");
		commandDescription.add("EXAMPLES");
		commandDescription
				.add("      hexdump a.txt     Outputs a hexadecimal representation of a given file's contents");
	}

	/**
	 * Executes Hexdump Command using given Environment and arguments
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
			env.write("Invalid number of arguments.");
		} else {
			try (FileInputStream is = new FileInputStream(new File(args.get(0)))) {
				Integer line = 0;

				while (is.available() > 0) {
					StringBuilder sb = new StringBuilder();

					sb.append(generateNumber(line));

					byte[] buf = new byte[Math.min(16, is.available())];
					is.read(buf);

					sb.append(byteToHex(buf));

					sb.append(" | ");

					for (byte b : buf) {
						if (b < 32 || b > 127) {
							sb.append(".");
						} else {
							sb.append((char) b);
						}
					}
					env.writeln(sb.toString());
					line++;
				}
			} catch (FileNotFoundException e) {
				env.writeln("Could not find file.");
			} catch (IOException e) {
				env.writeln("Could not read file.");
			}
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Generates a number that should be outputted at the beginning of given line.
	 * 
	 * @param line level of given line
	 * @return generated number
	 */
	private String generateNumber(Integer line) {
		StringBuilder sb = new StringBuilder();
		String number = line.toString();
		int zeros = 7 - number.length();

		for (int i = 0; i < zeros; i++) {
			sb.append("0");
		}
		sb.append(number + "0");
		sb.append(": ");
		return sb.toString();
	}

	/**
	 * Converts a given array of bytes into it's stringed hexadecimal representation
	 * 
	 * @param buf given byte array
	 * @return String representation
	 */
	private String byteToHex(byte[] buf) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		while (i < 8) {
			if (i >= buf.length) {
				break;
			}
			byte currentByte = buf[i];

			sb.append(generateString((byte) (currentByte >> 4)));
			sb.append(generateString((byte) (currentByte & 0x0F)));

			if (i != 7) {
				sb.append(" ");
			}
			i++;
		}
		while (i < 8) {
			if (i != 7) {
				sb.append("    ");
			}
			i++;
		}
		sb.append("|");
		while (i < 16) {
			if (i >= buf.length) {
				break;
			}
			byte currentByte = buf[i];

			sb.append(generateString((byte) (currentByte >> 4)));
			sb.append(generateString((byte) (currentByte & 0x0F)));
			sb.append(" ");
			i++;
		}
		while (i < 16) {
			sb.append("   ");
			i++;
		}
		return sb.toString();
	}

	/**
	 * Generates a hexadecimal String based on given digit
	 * 
	 * @param digit given digit
	 * @return generated String
	 */
	private static String generateString(byte digit) {
		if (digit >= 0 && digit < 10)
			return String.valueOf(digit);

		switch (digit) {
		case (-8):
			return "8";
		case (-7):
			return "9";
		case (-6):
		case (10):
			return "a";
		case (-5):
		case (11):
			return "b";
		case (-4):
		case (12):
			return "c";
		case (-3):
		case (13):
			return "d";
		case (-2):
		case (14):
			return "e";
		case (-1):
		case (15):
			return "f";
		}
		throw new IllegalArgumentException();
	}

	/**
	 * @return name of Hexdump Command
	 */
	@Override
	public String getCommandName() {
		return "hexdump";
	}

	/**
	 * @return description of Hexdump Command
	 */
	@Override
	public List<String> getCommandDescription() {
		return commandDescription;
	}
}
