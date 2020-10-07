package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Node which is defined by its variable, start, end and step expressions. This
 * Node can be a parent. Expressions cannot be defined as functions.
 * 
 * @author Jura Milić
 *
 */
public class ForLoopNode extends Node {
	/**
	 * Parsed variable.
	 */
	private ElementVariable variable;
	/**
	 * Parsed start expression.
	 */
	private Element startExpression;
	/**
	 * Parsed end expression.
	 */
	private Element endExpression;
	/**
	 * Parsed step expression. Does not have to be defined.
	 */
	private Element stepExpression;

	/**
	 * Constructs ForLoopNode with given variable, start, end and step expressions.
	 * Variable and expressions are given as Tokens.
	 * 
	 * @param var   given variable
	 * @param start given start expression
	 * @param end   given end expression
	 * @param step  given step expression
	 */
	public ForLoopNode(Token var, Token start, Token end, Token step) {
		variable = new ElementVariable(var.getValue().toString());

		startExpression = setExpression(start);
		endExpression = setExpression(end);

		if (step == null)
			stepExpression = null;
		else
			stepExpression = setExpression(step);
	}

	/**
	 * Reads given token to determine what type of Element the expression should be
	 * and assigns the expression's value.
	 * 
	 * @param input given token
	 * @return read Element
	 */
	private Element setExpression(Token input) {
		if (input.getType() == TokenType.NUMBER) {
			try {
				double value = Double.parseDouble(input.getValue().toString());
				int val = (int) value;
				if (value - val == 0)
					return new ElementConstantInteger(val);
				return new ElementConstantDouble(value);
			} catch (NumberFormatException ex) {
				throw new SmartScriptParserException();
			}
		}
		if (input.getType() == TokenType.STRING) {
			return new ElementString(input.getValue().toString());
		}
		if (input.getType() == TokenType.WORD) {
			return new ElementVariable(input.getValue().toString());
		}
		throw new SmartScriptParserException();
	}

	/**
	 * @return variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * @return start expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * @return end expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * @return step expression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	public boolean equals(Object node) {
		if (node.getClass() != ForLoopNode.class)
			return false;

		ForLoopNode forNode = (ForLoopNode) node;
		if (!forNode.getVariable().equals(variable))
			return false;
		if (!forNode.getStartExpression().equals(startExpression))
			return false;
		if (!forNode.getEndExpression().equals(endExpression))
			return false;
		if (stepExpression == null && forNode.getStepExpression() != null)
			return false;
		if (stepExpression != null && forNode.getStepExpression() == null)
			return false;
		if (stepExpression != null && forNode.getStepExpression() != null) {
			if (!forNode.getStepExpression().equals(stepExpression))
				return false;
		}
		if (this.numberOfChildren() != forNode.numberOfChildren())
			return false;

		for (int i = 0; i < this.numberOfChildren(); i++) {
			if (!this.getChild(i).equals(forNode.getChild(i)))
				return false;
		}
		return true;
	}
}
