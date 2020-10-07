package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Lexer is a class used in lexing. Lexer reads a given string and assigns a
 * Token to each component. Components are determined by their meaning which
 * will be used later in parsing. Eventually parser is the one who will consume
 * the information gathered in tokens. Outside of TAGS the Lexer interprets
 * everything as STRING. Inside of TAGS, STRINGS start and end with '"', numbers
 * can be negative, functions start with '@', a letter and any number of digits,
 * letters and underscores.
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
			throw new NullPointerException("Given text was NULL.");
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
				throw new LexerException("Lexer tried to read text after finishing the text.");
			}
		}

		if (currentIndex == data.length) {
			if (state == LexerState.TAG)
				throw new LexerException("TAG was not closed before end of input.");
			token = new Token(TokenType.EOF, null);
			return token;
		}

		char c = data[currentIndex];
		if (c == '{') {
			if (state == LexerState.TAG) {
				throw new LexerException("Character '{' should not be found inside of a TAG.");
			}
			currentIndex++;
			char d;
			try {
				d = data[currentIndex];
			} catch (ArrayIndexOutOfBoundsException ex) {
				throw new LexerException("Beginning of TAG was at end of text.");
			}
			if (d == '$') {
				currentIndex++;
				String s = Character.toString(c) + Character.toString(d);
				setState(LexerState.TAG);
				token = new Token(TokenType.SYMBOL, s);
				return token;
			} else {
				throw new LexerException("Character '{' was found without character '$'");
			}
		}

		if (c == '$' && state == LexerState.TAG) {
			currentIndex++;
			char d;
			if (currentIndex >= data.length) {
				throw new LexerException("TAG was not closed before end of file.");
			}
			d = data[currentIndex];
			if (d == '}') {
				String s = Character.toString(c) + Character.toString(d);
				setState(LexerState.BASIC);
				currentIndex++;
				token = new Token(TokenType.SYMBOL, s);
				return token;
			} else
				throw new LexerException("Character '$' was in TAG without character '}'");
		}

		if (state == LexerState.BASIC)
			return getBasicToken();
		try {
			return getTagToken();
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new LexerException("TAG did not end before end of input.");
		}
	}

	/**
	 * @return current Token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param sets Lexer to work in given state.
	 */
	public void setState(LexerState state) {
		this.state = state;
	}

	/**
	 * Generates Token as a String outside of TAGS.
	 * 
	 * @return generated Token
	 */
	private Token getBasicToken() {
		StringBuilder sb = new StringBuilder();
		char c = data[currentIndex];

		while (!checkTag()) {
			if (c == '\\') {
				currentIndex++;
				if (currentIndex >= data.length)
					throw new LexerException("Escaped sign was at the end of input.");

				readBasicEscapedLetter(sb);
				currentIndex++;
				if (currentIndex >= data.length) {
					token = new Token(TokenType.STRING, sb.toString());
					return token;
				}
				c = data[currentIndex];
			} else {
				sb.append(c);
				currentIndex++;
				if (currentIndex >= data.length) {
					token = new Token(TokenType.STRING, sb.toString());
					return token;
				}
				c = data[currentIndex];
			}
		}
		token = new Token(TokenType.STRING, sb.toString());
		return token;
	}

	/**
	 * Reads an escaped letter outside of TAGS.
	 * 
	 * @param sb StringBuilder which is generating the String.
	 */
	private void readBasicEscapedLetter(StringBuilder sb) {
		char c = data[currentIndex];
		if (c != '\\' && c != '{')
			throw new LexerException("Invalid escaped character.");
		sb.append(c);
	}

	/**
	 * Checks if next two characters are the start of TAG "{$".
	 * 
	 * @return true if next Token is the beginning of TAG, false otherwise.
	 */
	private boolean checkTag() {
		if (currentIndex + 1 >= data.length)
			return false;
		char c = data[currentIndex];
		char d = data[currentIndex + 1];
		String s = Character.toString(c) + Character.toString(d);
		if (s.equals("{$"))
			return true;
		else
			return false;
	}

	/**
	 * Generates next TAG Token.
	 * 
	 * @return generated Token
	 */
	private Token getTagToken() {

		char c = data[currentIndex];
		if (c == ' ') {
			currentIndex++;
			return nextToken();
		}

		if (c == '\\')
			throw new LexerException("\\ is not allowed inside of TAG");

		StringBuilder sb = new StringBuilder();

		// reads function name
		if (c == '@') {
			sb.append(c);
			currentIndex++;
			c = data[currentIndex];

			if (!Character.isLetter(c))
				throw new LexerException("Function name did not start with letter.");

			sb.append(c);
			currentIndex++;

			c = data[currentIndex];
			while (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
				sb.append(c);
				currentIndex++;
				c = data[currentIndex];
			}

			token = new Token(TokenType.FUNCTION, sb.toString());
			return token;
		}

		// reads variable name
		if (Character.isLetter(c)) {
			while (Character.isLetter(c) || c == '_') {
				sb.append(c);
				currentIndex++;

				c = data[currentIndex];
			}
			token = new Token(TokenType.WORD, sb.toString());
			return token;
		} else {

			// reads number
			char d = data[currentIndex + 1];
			if (Character.isDigit(c) || (c == '-' && Character.isDigit(d))) {
				return readNumber();

				// reads symbol
			} else {
				if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
					currentIndex++;
					token = new Token(TokenType.OPERATOR, c);
					return token;
				}

				if (c == '"') {
					currentIndex++;
					return getStringTagToken();
				}
				currentIndex++;
				token = new Token(TokenType.SYMBOL, Character.toString(c));
				return token;
			}
		}

	}

	/**
	 * Generates new Number Token.
	 * 
	 * @return generated Token
	 */
	private Token readNumber() {
		char c = data[currentIndex];
		StringBuilder sb = new StringBuilder();

		if (c == '-') {
			if (Character.isDigit(data[currentIndex + 1])) {
				sb.append(c);
				currentIndex++;
				c = data[currentIndex];
				sb.append(c);
				currentIndex++;
				c = data[currentIndex];
			}
		}

		while (Character.isDigit(c)) {
			sb.append(c);
			currentIndex++;
			c = data[currentIndex];
		}

		if (c == '.') {
			if (Character.isDigit(data[currentIndex + 1])) {
				sb.append(c);
				currentIndex++;
				c = data[currentIndex];
				sb.append(c);
				currentIndex++;
				c = data[currentIndex];
			}
			while (Character.isDigit(c)) {
				sb.append(c);
				currentIndex++;
				c = data[currentIndex];
			}
		}
		double number;
		try {
			number = Double.valueOf(sb.toString());
		} catch (NumberFormatException ex) {
			throw new LexerException("Number could not be parsed into double.");
		}
		token = new Token(TokenType.NUMBER, number);
		return token;
	}

	/**
	 * Generates a String Token which is inside TAG.
	 * 
	 * @return generated String Token
	 */
	private Token getStringTagToken() {
		char c = data[currentIndex];
		StringBuilder sb = new StringBuilder("");

		while (c != '"') {
			if (c == '\\') {
				currentIndex++;
				c = data[currentIndex];

				if (c != '\\' && c != '"' && c != 'n' && c != 't' && c != 'r')
					throw new LexerException("Escape sign was not right in String inside of TAG");
				switch (c) {
				case ('n'):
					c = '\n';
					break;
				case ('r'):
					c = '\r';
					break;
				case ('t'):
					c = '\t';
					break;
				}
			}
			sb.append(c);
			currentIndex++;
			if (currentIndex >= data.length)
				throw new LexerException();
			c = data[currentIndex];
		}
		currentIndex++;
		token = new Token(TokenType.STRING, sb.toString());
		return token;
	}
}
