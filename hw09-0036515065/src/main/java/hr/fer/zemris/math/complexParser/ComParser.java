package hr.fer.zemris.math.complexParser;

import hr.fer.zemris.math.Complex;

/**
 * Simple Parser that reads a single Complex Number. Parser first tokenizes the
 * input and then generates the real and imaginary values of the read Complex
 * Number.
 * 
 * @author Jura Milić
 *
 */
public class ComParser {
	/**
	 * Lexer used for generating tokens.
	 */
	ComLexer lexer;
	/**
	 * Real value of number.
	 */
	double real;
	/**
	 * Imaginary value of number.
	 */
	double imaginary;

	/**
	 * Constructs a ComParser and reads the given input. Real and imaginary values
	 * are set according to input.
	 * 
	 * @param text given input
	 */
	public ComParser(String text) {
		lexer = new ComLexer(text);
		real = 0;
		imaginary = 0;
		parse();
	}

	/**
	 * Parses the input given through constructor.
	 */
	private void parse() {
		double factor = 1;
		Token token = lexer.nextToken();

		if (token.getType() == TokenType.EOF) {
			System.out.println("Cannot read empty string!");
		}

		while (token.getType() != TokenType.EOF) {
			switch (token.getType()) {
			case OPERATOR:
				if (token.getValue().toString().equals("-")) {
					factor = -1;
				}
				break;
			case NUMBER:
				real = factor * (double) token.getValue();
				factor = 1;
				break;
			case i:
				token = lexer.nextToken();
				if (token.getType() == TokenType.OPERATOR) {
					if (token.getValue().toString().equals("-")) {
						factor = -1;
					}
					token = lexer.nextToken();
				}
				double val = 1;
				if (token.getType() == TokenType.NUMBER) {
					val = (double) token.getValue();
				}
				imaginary = val * factor;
			default:
				break;
			}
			token = lexer.nextToken();
		}
	}

	/**
	 * Generates the Complex Number and returns it.
	 * 
	 * @return the parsed Complex Number
	 */
	public Complex getComplex() {
		return new Complex(real, imaginary);
	}

}
