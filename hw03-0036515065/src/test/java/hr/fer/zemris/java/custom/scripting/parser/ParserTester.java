package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.parser.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class ParserTester {

	@Test
	void testEmpty() {
		SmartScriptParser parser = new SmartScriptParser("");

		DocumentNode rootNode = parser.getDocumentNode();
		assertThrows(SmartScriptParserException.class, () -> rootNode.getChild(0));
		assertEquals(0, rootNode.numberOfChildren());
	}

	@Test
	void testString() {
		SmartScriptParser parser = new SmartScriptParser("This is sample text.");

		DocumentNode rootNode = parser.getDocumentNode();
		assertEquals(1, rootNode.numberOfChildren());

		TextNode textNode = new TextNode("This is sample text.");
		assertEquals(textNode, rootNode.getChild(0));

		DocumentNode documentNode = new DocumentNode();
		documentNode.addChildNode(textNode);

		assertEquals(documentNode, rootNode);
	}

	@Test
	void testFor() {
		SmartScriptParser parser = new SmartScriptParser("{$ FOR i 1 10 1 $} \r\n This is {$END$}");

		DocumentNode rootNode = parser.getDocumentNode();
		assertEquals(1, rootNode.numberOfChildren());

		TextNode textNode = new TextNode(" \r\n This is ");
		ForLoopNode forNode = new ForLoopNode(new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, 1.0),
				new Token(TokenType.NUMBER, 10.0), new Token(TokenType.NUMBER, 1.0));
		forNode.addChildNode(textNode);

		assertEquals(forNode, rootNode.getChild(0));

		DocumentNode documentNode = new DocumentNode();
		documentNode.addChildNode(forNode);

		assertEquals(documentNode, rootNode);
	}

	@Test
	void testEcho() {
		SmartScriptParser parser = new SmartScriptParser("{$= i i * @sin  \"0.000\" @decfmt $}");

		DocumentNode rootNode = parser.getDocumentNode();
		assertEquals(1, rootNode.numberOfChildren());

		Element[] array = new Element[] { new ElementVariable("i"), new ElementVariable("i"), new ElementOperator("*"),
				new ElementFunction("sin"), new ElementString("0.000"), new ElementFunction("decfmt") };
		EchoNode echoNode = new EchoNode(array);

		assertEquals(echoNode, rootNode.getChild(0));

		DocumentNode documentNode = new DocumentNode();
		documentNode.addChildNode(echoNode);
		assertEquals(documentNode, rootNode);
	}

	@Test
	void realDeal() {
		SmartScriptParser parser = new SmartScriptParser(
				"This is sample text.{$ FOR i 1 10 1 $} This is {$= i $}-th time this message is generated.{$END$}{$FOR i 0 10 2 $} sin({$=i$}^2) = {$= i i * @sin  \"0.000\" @decfmt $}{$END$}");

		DocumentNode rootNode = parser.getDocumentNode();

		DocumentNode docNode = new DocumentNode();
		TextNode firstTextNode = new TextNode("This is sample text.");
		ForLoopNode firstForNode = new ForLoopNode(new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, "1.0"),
				new Token(TokenType.NUMBER, 10.0), new Token(TokenType.NUMBER, 1.0));
		TextNode secondTextNode = new TextNode(" This is ");
		Element[] elements = new Element[] { new ElementVariable("i") };
		EchoNode firstEcho = new EchoNode(elements);
		TextNode thirdTextNode = new TextNode("-th time this message is generated.");

		ForLoopNode secondForNode = new ForLoopNode(new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, "0.0"),
				new Token(TokenType.NUMBER, 10.0), new Token(TokenType.NUMBER, 2.0));
		TextNode fourthTextNode = new TextNode(" sin(");
		EchoNode secondEcho = new EchoNode(elements);
		TextNode fifthTextNode = new TextNode("^2) = ");
		Element[] elements2 = new Element[] { new ElementVariable("i"), new ElementVariable("i"),
				new ElementOperator("*"), new ElementFunction("sin"), new ElementString("0.000"),
				new ElementFunction("decfmt") };
		EchoNode thirdEcho = new EchoNode(elements2);

		firstForNode.addChildNode(secondTextNode);
		firstForNode.addChildNode(firstEcho);
		firstForNode.addChildNode(thirdTextNode);

		secondForNode.addChildNode(fourthTextNode);
		secondForNode.addChildNode(secondEcho);
		secondForNode.addChildNode(fifthTextNode);
		secondForNode.addChildNode(thirdEcho);

		docNode.addChildNode(firstTextNode);
		docNode.addChildNode(firstForNode);
		docNode.addChildNode(secondForNode);

		assertEquals(firstTextNode, rootNode.getChild(0));
		assertEquals(firstForNode, rootNode.getChild(1));
		assertEquals(secondForNode, rootNode.getChild(2));
		assertEquals(docNode, rootNode);
	}

	@Test
	public void testExample1() {
		String document = loader("extra/primjer1.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals("Ovo je \nsve jedan text node\n", string);

		TextNode expectedTextNode = new TextNode(string);
		assertEquals(expectedTextNode, node.getChild(0));

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(expectedTextNode);

		assertEquals(expectedDocNode, node);

	}

	@Test
	public void testExample2() {
		String document = loader("extra/primjer2.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals("Ovo je \nsve jedan \\{$ text node\n", string);

		TextNode expectedTextNode = new TextNode("Ovo je \nsve jedan {$ text node\n");
		assertEquals(expectedTextNode, node.getChild(0));

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(expectedTextNode);

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testExample3() {
		String document = loader("extra/primjer3.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals("Ovo je \nsve jedan \\\\\\{$text node\n", string);

		TextNode expectedTextNode = new TextNode("Ovo je \nsve jedan \\{$text node\n");
		assertEquals(expectedTextNode, node.getChild(0));

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(expectedTextNode);

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testExample4() {
		String document = loader("extra/primjer4.txt");
		try {
			SmartScriptParser parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		}
	}

	@Test
	public void testExample5() {
		String document = loader("extra/primjer5.txt");
		try {
			SmartScriptParser parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException.");
		}
	}

	@Test
	public void testExample6() {
		String document = loader("extra/primjer6.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals("Ovo je OK {$= \"String ide\nu više redaka\nčak tri\" $}\n", string);

		TextNode expectedTextNode = new TextNode("Ovo je OK ");
		assertEquals(expectedTextNode, node.getChild(0));
		Element[] array = new Element[] { new ElementString("String ide\nu više redaka\nčak tri") };
		EchoNode echoNode = new EchoNode(array);
		TextNode lastText = new TextNode("\n");

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(expectedTextNode);
		expectedDocNode.addChildNode(echoNode);
		expectedDocNode.addChildNode(lastText);

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testExample7() {
		String document = loader("extra/primjer7.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals(
				"Ovo je isto OK {$= \"String ide\n" + "u \\\"više\\\" \nredaka\n" + "ovdje a stvarno četiri\" $}\n",
				string);

		TextNode expectedTextNode = new TextNode("Ovo je isto OK ");
		assertEquals(expectedTextNode, node.getChild(0));

		Element[] array = new Element[] {
				new ElementString("String ide\nu \"više\" \nredaka\novdje a stvarno četiri") };
		EchoNode echoNode = new EchoNode(array);
		TextNode lastText = new TextNode("\n");

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(expectedTextNode);
		expectedDocNode.addChildNode(echoNode);
		expectedDocNode.addChildNode(lastText);

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testExample8() {
		String document = loader("extra/primjer8.txt");

		try {
			SmartScriptParser parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}
	}

	@Test
	public void testExample9() {
		String document = loader("extra/primjer9.txt");

		try {
			SmartScriptParser parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}
	}

	@Test
	public void testDoc01() {
		String document = loader("document01.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals(
				"Ovo je sample text\nTurizam {$ FOR var 1 1 1 $}\n{$ FOR var 2 2 2 $} Bas mi se gricka\nnesto\n{$END$}\n{$END$}",
				string);

		TextNode firstTextNode = new TextNode("Ovo je sample text\nTurizam ");
		assertEquals(firstTextNode, node.getChild(0));

		ForLoopNode firstForNode = new ForLoopNode(new Token(TokenType.WORD, "var"), new Token(TokenType.NUMBER, 1.0),
				new Token(TokenType.NUMBER, 1.0), new Token(TokenType.NUMBER, 1.0));
		ForLoopNode secondForNode = new ForLoopNode(new Token(TokenType.WORD, "var"), new Token(TokenType.NUMBER, 2.0),
				new Token(TokenType.NUMBER, 2.0), new Token(TokenType.NUMBER, 2.0));

		TextNode secondTextNode = new TextNode("\n");
		TextNode thirdTextNode = new TextNode(" Bas mi se gricka\nnesto\n");
		TextNode fourthTextNode = new TextNode("\n");

		firstForNode.addChildNode(secondTextNode);
		firstForNode.addChildNode(secondForNode);
		firstForNode.addChildNode(fourthTextNode);

		secondForNode.addChildNode(thirdTextNode);

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(firstTextNode);
		expectedDocNode.addChildNode(firstForNode);

		assertEquals(secondTextNode, node.getChild(1).getChild(0));
		assertEquals(thirdTextNode, node.getChild(1).getChild(1).getChild(0));
		assertEquals(secondForNode, node.getChild(1).getChild(1));
		assertEquals(thirdTextNode, node.getChild(1).getChild(1).getChild(0));
		assertEquals(firstForNode, node.getChild(1));

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testDoc02() {
		String document = loader("document02.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();
		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals(
				"Ovo je sample text\n{$ FOR OvoJeDuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuugackoImeVarijable 1 1 1 $}\nEcho zauzima puno mjesta ali ima samo dva elementa {$= i \"ana voli milovana\" $}{$END$}",
				string);

		TextNode firstTextNode = new TextNode("Ovo je sample text\n");
		assertEquals(firstTextNode, node.getChild(0));

		ForLoopNode forNode = new ForLoopNode(
				new Token(TokenType.WORD, "OvoJeDuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuugackoImeVarijable"),
				new Token(TokenType.NUMBER, 1.0), new Token(TokenType.NUMBER, 1.0), new Token(TokenType.NUMBER, 1.0));

		TextNode secondTextNode = new TextNode("\nEcho zauzima puno mjesta ali ima samo dva elementa ");
		Element[] array = new Element[] { new ElementVariable("i"), new ElementString("ana voli milovana") };
		EchoNode echoNode = new EchoNode(array);
		forNode.addChildNode(secondTextNode);
		forNode.addChildNode(echoNode);

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(firstTextNode);
		expectedDocNode.addChildNode(forNode);

		assertEquals(secondTextNode, node.getChild(1).getChild(0));
		assertEquals(firstTextNode, node.getChild(0));
		assertEquals(forNode, node.getChild(1));
		assertEquals(echoNode, node.getChild(1).getChild(1));

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testDoc03() {
		String document = loader("document03.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();

		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals("Ovo je sample text\n{$ FOR thisShouldNotThrow 1 1 $}{$END$}", string);

		TextNode textNode = new TextNode("Ovo je sample text\n");
		assertEquals(textNode, node.getChild(0));

		ForLoopNode forNode = new ForLoopNode(new Token(TokenType.WORD, "thisShouldNotThrow"),
				new Token(TokenType.NUMBER, 1.0), new Token(TokenType.NUMBER, 1.0), null);
		;

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(textNode);
		expectedDocNode.addChildNode(forNode);

		assertEquals(forNode, node.getChild(1));

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testDoc04() {
		String document = loader("document04.txt");
		SmartScriptParser parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();

		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());

		String string = node.toString();
		assertEquals("{$ FOR i 1 1 $}{$ FOR i 2 2 $}{$ FOR i 3 3 $}{$END$}{$END$}{$END$}", string);

		ForLoopNode firstForNode = new ForLoopNode(new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, 1.0),
				new Token(TokenType.NUMBER, 1.0), null);
		;

		ForLoopNode secondForNode = new ForLoopNode(new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, 2.0),
				new Token(TokenType.NUMBER, 2.0), null);
		;

		ForLoopNode thirdForNode = new ForLoopNode(new Token(TokenType.WORD, "i"), new Token(TokenType.NUMBER, 3.0),
				new Token(TokenType.NUMBER, 3.0), null);
		;

		secondForNode.addChildNode(thirdForNode);
		firstForNode.addChildNode(secondForNode);

		DocumentNode expectedDocNode = new DocumentNode();
		expectedDocNode.addChildNode(firstForNode);

		assertEquals(firstForNode, node.getChild(0));

		assertEquals(expectedDocNode, node);
	}

	@Test
	public void testForFew() {
		String document = loader("exceptionTests/forFewArguments1.txt");

		SmartScriptParser parser;
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forFewArguments2.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forFewArguments3.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forFewArguments4.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forFewArguments5.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}
	}

	@Test
	public void testForInvalidVar() {
		String document = loader("exceptionTests/forInvalidVarName1.txt");

		SmartScriptParser parser;
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forInvalidVarName2.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forInvalidVarName3.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forInvalidVarName4.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forFewArguments5.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}
	}

	@Test
	public void testForEnds() {
		String document = loader("exceptionTests/forNotEnded1.txt");

		SmartScriptParser parser;
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/forNotEnded2.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/tooManyEnds.txt");
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}
	}
	
	@Test
	public void testBackslash() {
		String document = loader("exceptionTests/SingleEscape.txt");

		SmartScriptParser parser;
		try {
			parser = new SmartScriptParser(document);
			fail("Should throw");
		} catch (SmartScriptParserException ex) {
		} catch (Exception ex) {
			fail("Should throw SmartScriptParserException");
		}

		document = loader("exceptionTests/DoubleEscape.txt");
		parser = new SmartScriptParser(document);
		DocumentNode node = parser.getDocumentNode();

		SmartScriptParser parser2 = new SmartScriptParser(node.toString());
		assertEquals(node.toString(), parser2.getDocumentNode().toString());
		
		assertEquals("\\\\", node.toString());
	}

	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while (true) {
				int read = is.read(buffer);
				if (read < 1)
					break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			return null;
		}
	}

}
