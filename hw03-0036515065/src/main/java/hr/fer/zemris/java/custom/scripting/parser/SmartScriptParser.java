package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.collections.*;

/**
 * Parser which takes Tokens from a Lexer and constructs an interpreted data
 * structure from those Tokens. The data structure is made up of Nodes and the
 * root Node is always a DocumentNode.
 * 
 * @author Jura Milić
 *
 */
public class SmartScriptParser {
	/**
	 * Lexer which will tokenize an input.
	 */
	private Lexer lexer;
	/**
	 * A collection of extracted tokens.
	 */
	private ArrayIndexedCollection tokens;
	/**
	 * The root Node.
	 */
	private DocumentNode rootNode;

	/**
	 * Constructs a SmartScriptParser and parses the given String.
	 * 
	 * @param document given String.
	 * @throws SmartScriptParserException if an error occurs during parsing.
	 */
	public SmartScriptParser(String document) {
		rootNode = null;
		lexer = new Lexer(document);
		tokens = new ArrayIndexedCollection();
		Token token;

		try {
			token = lexer.nextToken();
		} catch (LexerException ex) {
			throw new SmartScriptParserException();
		}
		tokens.add(token);

		while (token.getType() != TokenType.EOF) {
			try {
				token = lexer.nextToken();
			} catch (LexerException ex) {
				throw new SmartScriptParserException("An error ocurred while lexing.");
			}
			tokens.add(token);
		}

		rootNode = new DocumentNode();
		try {
			parse();
		} catch (Exception e) {
			throw new SmartScriptParserException();
		}
	}

	/**
	 * The actual method which will parse the input.
	 */
	public void parse() {
		ElementsGetter tokenGetter = tokens.createElementsGetter();
		Token currentToken = getNextToken(tokenGetter);
		ObjectStack stack = new ObjectStack();

		stack.push(rootNode);

		while (currentToken.getType() != TokenType.EOF) {
			if (currentToken.getType() == TokenType.STRING) {
				Node node = (Node) stack.pop();
				try {
					node.addChildNode(new TextNode((String) currentToken.getValue()));
					stack.push(node);
				} catch (ClassCastException ex) {
					throw new SmartScriptParserException();
				}

			} else {
				if (currentToken.getType() != TokenType.SYMBOL)
					throw new SmartScriptParserException();

				if (currentToken.getValue().equals("{$")) {
					currentToken = getNextToken(tokenGetter);

					if (currentToken.getValue().equals("=")) {
						Node node = (Node) stack.pop();
						parseEchoTag(tokenGetter, node);
						stack.push(node);

					} else {
						if (currentToken.getType() != TokenType.WORD)
							throw new SmartScriptParserException();
						if (currentToken.getValue().toString().toUpperCase().equals("FOR")) {
							Node node = (Node) stack.pop();
							parseForTag(tokenGetter, node);
							ForLoopNode forNode = (ForLoopNode) node.getChild(node.numberOfChildren() - 1);
							stack.push(node);
							stack.push(forNode);

						} else {
							if (!currentToken.getValue().toString().toUpperCase().contentEquals("END"))
								throw new SmartScriptParserException();
							stack.pop();
							if (stack.isEmpty())
								throw new SmartScriptParserException();
						}
					}
				}
			}
			currentToken = getNextToken(tokenGetter);
		}

		if (!stack.pop().equals(rootNode))
			throw new SmartScriptParserException();
	}

	/**
	 * Takes the next Token from Collection.
	 * 
	 * @param getter given ElementsGetter which takes the Tokens from Collection
	 * @return Found Token.
	 */
	private Token getNextToken(ElementsGetter getter) {
		try {
			return (Token) getter.getNextElement();
		} catch (Exception ex) {
			throw new SmartScriptParserException("Parser requested a Token but there are no more Tokens.");
		}
	}

	/**
	 * Parses next Tokens into an EchoNode. The constructed EchoNode will be added
	 * as a child to the given parent node.
	 * 
	 * @param getter ElementsGetter which will extract Tokens from Collection
	 * @param node   given parent node
	 */
	private void parseEchoTag(ElementsGetter getter, Node node) {
		Token currentToken = getNextToken(getter);
		ArrayIndexedCollection coll = new ArrayIndexedCollection();

		try {
			while (!currentToken.getValue().equals("$}")) {
				coll.add(currentToken);
				currentToken = getNextToken(getter);
			}
		} catch (NullPointerException ex) {
			throw new SmartScriptParserException();
		}

		Element[] array = new Element[coll.size()];
		for (int i = 0; i < coll.size(); i++) {
			currentToken = (Token) coll.get(i);

			if (currentToken.getType() == TokenType.WORD) {
				String value = currentToken.getValue().toString();
				if (!varIsValid(value))
					throw new SmartScriptParserException();
				array[i] = new ElementVariable(value);
			}

			if (currentToken.getType() == TokenType.FUNCTION) {
				String value = currentToken.getValue().toString();
				if (!funcIsValid(value))
					throw new SmartScriptParserException();
				array[i] = new ElementFunction(value.substring(1));
			}

			if (currentToken.getType() == TokenType.NUMBER) {
				try {
					double value = Double.parseDouble(currentToken.getValue().toString());
					array[i] = new ElementConstantDouble(value);
				} catch (NumberFormatException ex) {
					try {
						int value = Integer.parseInt(currentToken.getValue().toString());
						array[i] = new ElementConstantInteger(value);
					} catch (NumberFormatException exc) {
						throw new SmartScriptParserException();
					}
				}
			}

			if (currentToken.getType() == TokenType.OPERATOR) {
				String value = currentToken.getValue().toString();
				if (!operatorIsValid(value))
					throw new SmartScriptParserException();
				array[i] = new ElementOperator(value);
			}

			if (currentToken.getType() == TokenType.STRING) {
				array[i] = new ElementString(currentToken.getValue().toString());
			}
		}

		EchoNode echoNode = new EchoNode(array);
		node.addChildNode(echoNode);
	}

	/**
	 * Parses the next Tokens into a ForLoopNode. The constructed ForLoopNode will
	 * be added as a child to the given parent node.
	 * 
	 * @param getter ElementsGetter which will extract Tokens from Collection
	 * @param node   given parent node
	 */
	private void parseForTag(ElementsGetter getter, Node node) {
		Token currentToken = getNextToken(getter);

		if (currentToken.getType() != TokenType.WORD)
			throw new SmartScriptParserException();

		Token var = currentToken;
		if (!varIsValid(var.getValue().toString())) {
			throw new SmartScriptParserException();
		}

		currentToken = getNextToken(getter);
		if (currentToken.getValue().equals("$}"))
			throw new SmartScriptParserException();

		Token startExpression = currentToken;

		currentToken = getNextToken(getter);
		if (currentToken.getValue().equals("$}"))
			throw new SmartScriptParserException();

		Token endExpression = currentToken;
		Token stepExpression = null;

		currentToken = getNextToken(getter);
		if (!currentToken.getValue().equals("$}")) {
			stepExpression = currentToken;
			currentToken = getNextToken(getter);
			if (!currentToken.getValue().equals("$}"))
				throw new SmartScriptParserException();
		}

		ForLoopNode forNode = new ForLoopNode(var, startExpression, endExpression, stepExpression);
		node.addChildNode(forNode);
	}

	/**
	 * Checks if given String is a valid variable name.
	 * 
	 * @param value given String
	 * @return true if name is valid, false otherwise.
	 */
	private boolean varIsValid(String value) {
		if (value.length() == 0)
			throw new SmartScriptParserException();
		int index = 0;
		char c = value.charAt(index);
		if (!Character.isLetter(c))
			throw new SmartScriptParserException();
		while (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
			index++;
			if (index == value.length())
				return true;
			c = value.charAt(index);
		}
		return false;
	}

	/**
	 * Checks if given String is contains a valid function name.
	 * 
	 * @param value given String
	 * @return true if name is valid, false otherwise
	 */
	private boolean funcIsValid(String value) {
		if (value.length() == 0)
			throw new SmartScriptParserException();
		int index = 0;

		char c = value.charAt(index);
		if (c != '@')
			throw new SmartScriptParserException();

		index++;
		c = value.charAt(index);

		if (!Character.isLetter(c))
			throw new SmartScriptParserException();
		while (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
			index++;
			if (index == value.length())
				return true;
			c = value.charAt(index);
		}
		return false;
	}

	/**
	 * Checks if given String is a valid operator.
	 * 
	 * @param value given String
	 * @return true if operator is valid, false otherwise
	 */
	private boolean operatorIsValid(String value) {
		if (value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/") || value.equals("^"))
			return true;
		return false;
	}

	/**
	 * @return the root DocumentNode.
	 */
	public DocumentNode getDocumentNode() {
		return rootNode;
	}
}
