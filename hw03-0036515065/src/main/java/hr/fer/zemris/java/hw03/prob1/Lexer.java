package hr.fer.zemris.java.hw03.prob1;

/**
 * Lexer is a class used in lexing. Lexer reads a given string and assigns a
 * Token to each component. Components are usually determined by their meaning
 * which will be used later in parsing.
 * 
 * Lexer can switch states between BASIC and EXTENDED. In BASIC state Lexer
 * reads the given input and separates numbers, words and symbols. Words consist
 * of letters, numbers consist of digits. If a digit is escaped it will be
 * treated as a letter instead. In EXTENDED state ALL digits are considered
 * letters and escaping does nothing to the interpretation of next character.
 * 
 * @author Jura Milić
 *
 */
public class Lexer {
	/**
	 * Stores the given input.
	 */
	private char[] data;
	/**
	 * Current token
	 */
	private Token token;
	/**
	 * Current index of input
	 */
	private int currentIndex;
	/**
	 * Current state of Lexer
	 */
	private LexerState state;

	/**
	 * Constructs a Lexer with given text as input.
	 * 
	 * @param text given text
	 */
	public Lexer(String text) {
		if (text == null) {
			throw new NullPointerException();
		}

		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
	}

	/**
	 * Generates next Token
	 * 
	 * @return next Token
	 * @throws LexerException if an error occurs
	 */
	public Token nextToken() {
		if (token != null) {
			if (token.getType() == TokenType.EOF) {
				throw new LexerException("Tried to read token after input was already read");
			}
		}

		if (currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return token;
		}

		char c = data[currentIndex];
		if (c == ' ' || c == '\r' || c == '\n' || c == '\t') {
			currentIndex++;
			return nextToken();
		}

		if (c == '#') {
			currentIndex++;
			token = new Token(TokenType.SYMBOL, c);
			return token;
		}

		StringBuilder sb = new StringBuilder();

		if (Character.isLetter(c) || c == '\\' || state == LexerState.EXTENDED) {
			while (Character.isLetter(c) || c == '\\' || (state == LexerState.EXTENDED && Character.isDigit(c))) {
				if (c == '\\' && state == LexerState.BASIC) {
					currentIndex++;
					if (currentIndex >= data.length) {
						throw new LexerException("Escape sign was last character.");
					}
					c = data[currentIndex];
					readLetterEscaped(c, sb);
					currentIndex++;
					if (currentIndex >= data.length) {
						token = new Token(TokenType.WORD, sb.toString());
						return token;
					}
					c = data[currentIndex];
				} else {
					sb.append(c);
					currentIndex++;
					if (currentIndex >= data.length) {
						token = new Token(TokenType.WORD, sb.toString());
						return token;
					}
				}
			}
			token = new Token(TokenType.WORD, sb.toString());
			return token;
		} else {
			if (Character.isDigit(c)) {
				while (Character.isDigit(c)) {
					sb.append(c);
					currentIndex++;
					if (currentIndex >= data.length) {
						token = new Token(TokenType.NUMBER, Long.valueOf(sb.toString()));
						return token;
					}
					c = data[currentIndex];
				}
				long number;
				try {
					number = Long.valueOf(sb.toString());
				} catch (NumberFormatException ex) {
					throw new LexerException();
				}
				token = new Token(TokenType.NUMBER, number);
				return token;
			} else {
				currentIndex++;
				token = new Token(TokenType.SYMBOL, c);
				return token;
			}
		}
	}

	/**
	 * @return last generated Token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Reads an escaped letter
	 * 
	 * @param c  given letter
	 * @param sb StringBuilder which is building the output String.
	 */
	private void readLetterEscaped(char c, StringBuilder sb) {
		if (c != '\\') {
			try {
				Integer.parseInt(String.valueOf(c));
			} catch (NumberFormatException ex) {
				throw new LexerException();
			}
		}
		sb.append(c);
	}

	/**
	 * Sets Lexer to work in given state
	 * 
	 * @param state given state
	 */
	public void setState(LexerState state) {
		if (state == null)
			throw new NullPointerException();
		this.state = state;
	}
}
