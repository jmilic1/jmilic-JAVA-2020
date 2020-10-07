package hr.fer.zemris.math.complexParser;

/**
 * Simple Lexer for tokenizing a Complex Number.
 * 
 * @author Jura Milić
 *
 */
public class ComLexer {
	/**
	 * Data that needs to be lexed.
	 */
	private char[] data;
	/**
	 * Index of next character that needs to be read.
	 */
	private int currentIndex;

	/**
	 * Constructs a ComLexer that will lex the given input.
	 * 
	 * @param text given input
	 */
	public ComLexer(String text) {
		data = text.toCharArray();
		currentIndex = 0;
	}

	/**
	 * Reads and returns the next Token.
	 * 
	 * @return next Token
	 */
	public Token nextToken() {
		if (currentIndex == data.length) {
			return new Token(TokenType.EOF, null);
		}

		char c = data[currentIndex];
		if (c == ' ') {
			currentIndex++;
			return nextToken();
		}

		if (c == 'i') {
			currentIndex++;
			return new Token(TokenType.i, c);
		}

		if (c == '+' || c == '-') {
			currentIndex++;
			return new Token(TokenType.OPERATOR, c);
		}

		if (Character.isDigit(c)) {
			StringBuilder sb = new StringBuilder();
			while (Character.isDigit(c)) {
				sb.append(c);
				currentIndex++;
				if (currentIndex == data.length) {
					return new Token(TokenType.NUMBER, Double.parseDouble(sb.toString()));
				}
				c = data[currentIndex];
			}

			if (c == '.') {
				sb.append(c);
				c = data[currentIndex];
				currentIndex++;
				while (Character.isDigit(c)) {
					sb.append(c);
					if (currentIndex == data.length) {
						return new Token(TokenType.NUMBER, Double.parseDouble(sb.toString()));
					}
					c = data[currentIndex];
					currentIndex++;
				}
			}

			return new Token(TokenType.NUMBER, Double.parseDouble(sb.toString()));
		}

		throw new IllegalArgumentException("Could not parse input.");
	}
}
