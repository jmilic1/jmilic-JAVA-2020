package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * A basic class for single unit of a data structure constructed by Parser.
 * 
 * @author Jura Milić
 *
 */
public abstract class Node {
	/**
	 * Collection for storing child nodes if node is allowed to have them. Children
	 * are indexed in order by which they were added to parent.
	 */
	private ArrayIndexedCollection coll;

	/**
	 * Constructs Node with no children.
	 */
	public Node() {
		coll = null;
	}

	/**
	 * Adds a child to this node.
	 * 
	 * @param child given child
	 */
	public void addChildNode(Node child) {
		if (coll == null) {
			coll = new ArrayIndexedCollection();
		}
		coll.add(child);
	}

	/**
	 * @return number of direct children
	 */
	public int numberOfChildren() {
		if (coll == null)
			return 0;
		return coll.size();
	}

	/**
	 * Finds and returns the child of this node at given index.
	 * 
	 * @param index given index
	 * @return found child
	 * @throws SmartScriptParserException if there are no children or index is too
	 *                                    large
	 */
	public Node getChild(int index) {
		if (coll == null)
			throw new SmartScriptParserException("Child was searched for when Node has no children");
		if (index >= coll.size())
			throw new SmartScriptParserException("Given index is too large");
		return (Node) coll.get(index);
	}

	/**
	 * Calls the given visitor to visit this node.
	 * 
	 * @param visitor given visitor
	 */
	public void accept(INodeVisitor visitor) {
	}
}
