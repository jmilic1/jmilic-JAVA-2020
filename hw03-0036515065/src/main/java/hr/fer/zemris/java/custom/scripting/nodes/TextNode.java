package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node which contains some text.
 * 
 * @author Jura Milić
 *
 */
public class TextNode extends Node {
	/**
	 * Text stored in node.
	 */
	private String text;

	/**
	 * Constructs a TextNode with given text
	 * 
	 * @param text given text
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return text;
	}

	public boolean equals(Object node) {
		if (node.getClass() != TextNode.class)
			return false;

		TextNode textNode = (TextNode) node;
		if (this.getText().equals(textNode.getText()))
			return true;
		return false;
	}
}
