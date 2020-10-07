package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Node containing multiple parsed Elements. This Node cannot be a parent.
 * 
 * @author Jura Milić
 *
 */
public class EchoNode extends Node {
	/**
	 * Parsed Elements which belong to this EchoNode.
	 */
	private Element[] elements;

	/**
	 * Constructs an EchoNode with given Elements.
	 * 
	 * @param elements given Elements
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * @return Elements of this EchoNode.
	 */
	public Element[] getElements() {
		return elements;
	}

	@Override
	public boolean equals(Object node) {
		if (node.getClass() != EchoNode.class)
			return false;
		Element[] firstArray = elements;
		Element[] secondArray = ((EchoNode) node).getElements();

		if (firstArray.length != secondArray.length)
			return false;
		for (int i = 0; i < firstArray.length; i++) {
			if (!firstArray[i].equals(secondArray[i]))
				return false;
		}
		return true;
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
