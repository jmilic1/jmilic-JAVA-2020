package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Class used for lexing a query input. Tokenises each component of input
 * String. Tokens will be laster used by parser to parse the input String.
 * 
 * @author Jura Milić
 *
 */
public class QueryLexer {
	/**
	 * Input that should be lexed
	 */
	private char[] data;
	/**
	 * Index of next character to be read.
	 */
	private int currentIndex;
	/**
	 * Current token
	 */
	private Token token;

	/**
	 * Constructs a QueryLexer with given input String
	 * 
	 * @param query given String
	 * @throws NullPointerException if given input was null
	 */
	public QueryLexer(String query) {
		if (query == null) {
			throw new NullPointerException("Given query was NULL.");
		}

		data = query.toCharArray();
		currentIndex = 0;
	}

	/**
	 * Reads next component and assigns a token to it
	 * 
	 * @return assigned token
	 */
	public Token nextToken() {
		if (currentIndex >= data.length)
			return new Token(TokenType.EOF, null);
		if (data[currentIndex] == ' ' || data[currentIndex] == '\t') {
			currentIndex++;
			return nextToken();
		}
		char c = data[currentIndex];

		switch (c) {
		case ('"'):
			currentIndex++;
			return readLiteral();
		case ('='):
			currentIndex++;
			return new Token(TokenType.OPERATOR, "=");
		case ('!'):
			if (!(data[currentIndex + 1] == '=')) {
				throw new IllegalArgumentException();
			}
			currentIndex++;
			currentIndex++;
			return new Token(TokenType.OPERATOR, "!=");
		}

		if (c == '<' || c == '>') {
			String str = "";
			if (data[currentIndex + 1] == '=') {
				str = c + "=";
				currentIndex++;
				currentIndex++;
			} else {
				str = Character.toString(c);
				currentIndex++;
			}
			return new Token(TokenType.OPERATOR, str);
		}

		return readWord();

	}

	/**
	 * @return current Token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Read a String literal
	 * 
	 * @return tokenised literal
	 */
	private Token readLiteral() {
		StringBuilder sb = new StringBuilder();

		while (data[currentIndex] != '"') {
			sb.append(data[currentIndex]);
			currentIndex++;
		}
		currentIndex++;
		return new Token(TokenType.LITERAL, sb.toString());
	}

	/**
	 * Tokenise a word. Every word will be tokenised as a FIELD except LIKE operator
	 * and "and".
	 * 
	 * @return Tokenised word
	 */
	private Token readWord() {
		char c = data[currentIndex];
		StringBuilder sb = new StringBuilder();
		while (Character.isLetter(c)) {
			sb.append(c);
			currentIndex++;
			c = data[currentIndex];
		}
		if (sb.toString().toLowerCase().equals("and"))
			return new Token(TokenType.AND, "and");
		if (sb.toString().equals("LIKE"))
			return new Token(TokenType.OPERATOR, "LIKE");
		return new Token(TokenType.FIELD, sb.toString());
	}
}
