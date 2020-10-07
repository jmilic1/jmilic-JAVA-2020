package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Program that demonstrates the functionality of a visitor that visits nodes.
 * 
 * @author Jura Milić
 *
 */
public class TreeWriter {
	private static class WriterVisitor implements INodeVisitor {

		private int depth = 0;

		@Override
		public void visitTextNode(TextNode node) {
			writeTabs();
			System.out.println("Text of TextNode: " + node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			writeTabs();
			System.out.println("For Loop Node: START=" + node.getStartExpression().asText() + ", END="
					+ node.getEndExpression().asText() + ", STEP=" + node.getStepExpression().asText());
			depth++;
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
			depth--;
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			writeTabs();
			System.out.println("Echo node!");
			Element[] elements = node.getElements();
			for (Element elem:elements) {
				writeTabs();
				System.out.println(elem.asText());
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			writeTabs();
			System.out.println("Document node!");
			depth++;
			for (int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
			depth--;
		}

		private void writeTabs() {
			for (int i = 0; i < depth; i++) {
				System.out.print(" ");
			}
		}

	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("I expected exactly one argument!");
			System.exit(-1);
		}

		String docBody = "";
		try {
			docBody = Files.readString(Paths.get(args[0]));
		} catch (IOException e) {
			throw new RuntimeException("Could not read from given path!");
		}
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

}
