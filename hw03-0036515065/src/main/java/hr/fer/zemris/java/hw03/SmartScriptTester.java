package hr.fer.zemris.java.hw03;

import hr.fer.zemris.java.custom.scripting.parser.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;

import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Class used to demonstrate SmartScriptParser. Path to file to be parsed should
 * be given as an argument.
 * 
 * @author Jura Milić
 *
 */
public class SmartScriptTester {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Invalid number of arguments.");
			System.exit(-1);
		}
		String docBody = "";
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.out.println("Document could not be opened.");
			System.exit(-1);
		}

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		System.out.println(originalDocumentBody); // should write something like original // content of docBody

		SmartScriptParser parser2 = null;
		try {
			parser2 = new SmartScriptParser(originalDocumentBody);
		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		if (originalDocumentBody.equals(parser2.getDocumentNode().toString())) {
			System.out.println("Correct!");
		} else
			System.out.println("INCorrect!");
	}
}
