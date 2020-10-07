package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.*;

/**
 * Root Node of Parsed document. All nodes of a document stem from an instance
 * of this class. This Node is allowed to have children.
 * 
 * @author Jura Milić
 *
 */
public class DocumentNode extends Node {

	@Override
	public boolean equals(Object node) {
		if (node.getClass() != DocumentNode.class)
			return false;

		DocumentNode docNode = (DocumentNode) node;
		if (this.numberOfChildren() != docNode.numberOfChildren())
			return false;

		for (int i = 0; i < this.numberOfChildren(); i++) {
			if (!this.getChild(i).equals(docNode.getChild(i)))
				return false;
		}
		return true;
	}

	/**
	 * Returns a String which aims to be as close as possible to the original
	 * document. Certain parts of original document will not be in this String for
	 * example spaces.
	 */
	@Override
	public String toString() {
		return readNode(this);
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}

	/**
	 * Reads given element and returns it as String
	 * 
	 * @param elem given Element
	 * @return generated String
	 */
	private String readElement(Element elem) {
		if (elem.getClass() == ElementString.class)
			return readString(elem.asText());

		String string = "";
		if (elem.getClass() == ElementFunction.class) {
			string = "@" + elem.asText();
		} else {
			if (elem.getClass() == ElementString.class) {
				string = "\"" + elem.asText() + "\"";
			} else {
				string = elem.asText();
			}
		}
		return string;
	}

	/**
	 * Reads information about given parent node's children and returns that
	 * information as a String. This method is used to try to recreate the original
	 * document body.
	 * 
	 * @param parent given parent node
	 * @return String interpretation of children
	 */
	private String readNode(Node parent) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parent.numberOfChildren(); i++) {
			Node currentNode = parent.getChild(i);

			if (currentNode.getClass() == ForLoopNode.class) {
				ForLoopNode node = (ForLoopNode) currentNode;
				sb.append("{$ ");
				sb.append("FOR ");

				sb.append(node.getVariable().asText());
				sb.append(" ");
				sb.append(readElement(node.getStartExpression()));
				sb.append(" ");
				sb.append(readElement(node.getEndExpression()));
				if (node.getStepExpression() != null) {
					sb.append(" ");
					sb.append(readElement(node.getStepExpression()));
				}
				sb.append(" $}");

				sb.append(readNode(node));

				sb.append("{$END$}");
			} else {
				if (currentNode.getClass() == TextNode.class) {
					TextNode node = (TextNode) currentNode;
					sb.append(readText(node.getText()));
				} else {
					EchoNode node = (EchoNode) currentNode;
					Element[] array = node.getElements();

					sb.append("{$=");
					for (int j = 0; j < array.length; j++) {
						sb.append(" ");
						sb.append(readElement(array[j]));
					}
					sb.append(" ");
					sb.append("$}");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Reads given text and adds necessary escape signs to ensure the text can be
	 * parsed again and the result will be the same. Characters that will have an
	 * escape sign before them are '\' and '{'
	 * 
	 * @param text given text
	 * @return String of text with added escape signs.
	 */
	private String readText(String text) {
		int n = text.length();
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0; i < n; i++) {
			c = text.charAt(i);
			if (c == '\\' || c == '{')
				sb.append('\\');
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Reads given string and adds necessary escape signs to ensure the String can
	 * be parsed again and the result will be the same. Characters that will have an
	 * escape sign before them are '\' and '"'.
	 * 
	 * @param string given string
	 * @return string with added escape signs
	 */
	private String readString(String string) {
		int n = string.length();
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		char c;
		for (int i = 0; i < n; i++) {
			c = string.charAt(i);
			if (c == '\\' || c == '"') 
				sb.append('\\');
			sb.append(c);
		}
		sb.append("\"");
		return sb.toString();
	}
}
