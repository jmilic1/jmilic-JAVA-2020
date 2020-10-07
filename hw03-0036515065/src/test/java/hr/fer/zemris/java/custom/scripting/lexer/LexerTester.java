package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LexerTester {
	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");

		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testNullInput() {
		// must throw!
		assertThrows(NullPointerException.class, () -> new Lexer(null));
	}

	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");

		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return
		// each time what nextToken returned...
		Lexer lexer = new Lexer("");

		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	@Test
	public void testRadAfterEOF() {
		Lexer lexer = new Lexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	public void testFirstLine() {
		Lexer lexer = new Lexer("This is sample text.\r\n{$ FOR i 1 10 1 $}");

		Token correctData[] = { new Token(TokenType.STRING, "This is sample text.\r\n"),
				new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"), new Token(TokenType.WORD, "i"),
				new Token(TokenType.NUMBER, 1.0), new Token(TokenType.NUMBER, 10.0), new Token(TokenType.NUMBER, 1.0),
				new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testFirstLineThree() {
		Lexer lexer = new Lexer("{$ FOR i 1 10 1 $}\r\n This is {$= i $}-th time this message is generated.\r\n");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, 1.0), new Token(TokenType.NUMBER, 10.0),
				new Token(TokenType.NUMBER, 1.0), new Token(TokenType.SYMBOL, "$}"),
				new Token(TokenType.STRING, "\r\n This is "), new Token(TokenType.SYMBOL, "{$"),
				new Token(TokenType.SYMBOL, "="), new Token(TokenType.WORD, "i"), new Token(TokenType.SYMBOL, "$}"),
				new Token(TokenType.STRING, "-th time this message is generated.\r\n"),
				new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testFirstLineSix() {
		Lexer lexer = new Lexer("\r\nsin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}");

		Token correctData[] = { new Token(TokenType.STRING, "\r\nsin("), new Token(TokenType.SYMBOL, "{$"),
				new Token(TokenType.SYMBOL, "="), new Token(TokenType.WORD, "i"), new Token(TokenType.SYMBOL, "$}"),
				new Token(TokenType.STRING, "^2) = "), new Token(TokenType.SYMBOL, "{$"),
				new Token(TokenType.SYMBOL, "="), new Token(TokenType.WORD, "i"), new Token(TokenType.WORD, "i"),
				new Token(TokenType.OPERATOR, '*'), new Token(TokenType.FUNCTION, "@sin"),
				new Token(TokenType.STRING, "0.000"), new Token(TokenType.FUNCTION, "@decfmt"),
				new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testForOne() {
		Lexer lexer = new Lexer("{$ FOR i -1 10 1 $}");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, -1.0), new Token(TokenType.NUMBER, 10.0),
				new Token(TokenType.NUMBER, 1.0), new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testForTwo() {
		Lexer lexer = new Lexer("{$    FOR    sco_re       \"-1\"10 \"1\" $}");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "sco_re"), new Token(TokenType.STRING, "-1"),
				new Token(TokenType.NUMBER, 10.0), new Token(TokenType.STRING, "1"), new Token(TokenType.SYMBOL, "$}"),
				new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testForThree() {
		Lexer lexer = new Lexer("{$ FOR year 1 last_year $}");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "year"), new Token(TokenType.NUMBER, 1.0),
				new Token(TokenType.WORD, "last_year"), new Token(TokenType.SYMBOL, "$}"),
				new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testForFour() {
		Lexer lexer = new Lexer("{$ FOR i-1.35bbb\"1\" $}");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, -1.35), new Token(TokenType.WORD, "bbb"),
				new Token(TokenType.STRING, "1"), new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testForExpression() {
		Lexer lexer = new Lexer("{$ FOR i-1.35+2bbb\"1\" $}");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, -1.35), new Token(TokenType.OPERATOR, '+'),
				new Token(TokenType.NUMBER, 2.0), new Token(TokenType.WORD, "bbb"), new Token(TokenType.STRING, "1"),
				new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testForExpressionSpace() {
		Lexer lexer = new Lexer("{$ FOR i-1.35 + 2bbb\"1\" $}");

		Token correctData[] = { new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.WORD, "FOR"),
				new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, -1.35), new Token(TokenType.OPERATOR, '+'),
				new Token(TokenType.NUMBER, 2.0), new Token(TokenType.WORD, "bbb"), new Token(TokenType.STRING, "1"),
				new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testDoc() {
		Lexer lexer = new Lexer("A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}");

		Token correctData[] = { new Token(TokenType.STRING, "A tag follows "), new Token(TokenType.SYMBOL, "{$"),
				new Token(TokenType.SYMBOL, "="), new Token(TokenType.STRING, "Joe \"Long\" Smith"),
				new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testDocTwo() {
		Lexer lexer = new Lexer("Example { bla } blu \\{$=1$}. Nothing interesting {=here}");

		Token correctData[] = { new Token(TokenType.STRING, "Example { bla } blu {$=1$}. Nothing interesting {=here}"),
				new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testDocThree() {
		Lexer lexer = new Lexer("Example \\{$=1$}. Now actually write one {$=1$}");

		Token correctData[] = { new Token(TokenType.STRING, "Example {$=1$}. Now actually write one "),
				new Token(TokenType.SYMBOL, "{$"), new Token(TokenType.SYMBOL, "="), new Token(TokenType.NUMBER, 1.0),
				new Token(TokenType.SYMBOL, "$}"), new Token(TokenType.EOF, null) };

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testTagThrow() {

		try {
			Lexer lexer = new Lexer("{$=");
			lexer.nextToken();
			lexer.nextToken();
			fail("Should throw LexerException.");
		} catch (LexerException e) {

		}
	}

	private void checkTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for (Token expected : correctData) {
			Token actual = lexer.nextToken();
			String msg = "Checking token " + counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}

	private String readExample(int n) {
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer" + n + ".txt")) {
			if (is == null)
				throw new RuntimeException("Datoteka extra/primjer" + n + ".txt je nedostupna.");
			byte[] data = this.getClass().getClassLoader().getResourceAsStream("extra/primjer1.txt").readAllBytes();
			String text = new String(data, StandardCharsets.UTF_8);
			return text;
		} catch (IOException ex) {
			throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		}
	}
}
