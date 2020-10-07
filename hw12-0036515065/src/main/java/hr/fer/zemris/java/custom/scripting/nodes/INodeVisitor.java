package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Simple visitor interface whose implementations go through nodes and do an
 * operation upon them.
 * 
 * @author Jura Milić
 *
 */
public interface INodeVisitor {
	/**
	 * Visits a given text node.
	 * 
	 * @param node given text node
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Vists a given for loop node.
	 * 
	 * @param node given for loop node
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Visits a given echo node.
	 * 
	 * @param node given echo node
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Visits a given document node.
	 * 
	 * @param node given document node
	 */
	public void visitDocumentNode(DocumentNode node);
}