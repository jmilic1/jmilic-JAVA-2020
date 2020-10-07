package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class used for executing a parsed Smart Script. The parsed data for the
 * script to be executed is stored as a document node which holds all the other
 * text, for loop and echo nodes of that same parsed script.
 * 
 * @author Jura Milić
 *
 */
public class SmartScriptEngine {
	/**
	 * Document node to be executed.
	 */
	private DocumentNode documentNode;
	/**
	 * Request Context through which the commands of the Smart Script will
	 * communicate with a user.
	 */
	private RequestContext requestContext;
	/**
	 * Multistack which stores the last value of a parameter.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	/**
	 * String Builder which holds all of the data the Smart Script needs to send to
	 * user. The data is then passed as a String to context all at once.
	 */
	private StringBuilder sb = new StringBuilder();
	/**
	 * Node visitor.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			sb.append(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));

			String endExpression = node.getEndExpression().asText();
			while (multistack.peek(variable).numCompare(endExpression) <= 0) {
				for (int i = 0; i < node.numberOfChildren(); i++) {
					Node currentNode = node.getChild(i);
					currentNode.accept(visitor);
				}

				ValueWrapper arg1 = multistack.pop(variable);
				arg1.add(node.getStepExpression().asText());
				multistack.push(variable, arg1);
			}
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<Object>();

			for (Element elem : node.getElements()) {
				if (elem instanceof ElementConstantDouble) {
					stack.push(elem.asText());
				} else {
					if (elem instanceof ElementConstantInteger) {
						stack.push(elem.asText());
					} else {
						if (elem instanceof ElementString) {
							stack.push(elem.asText());
						} else {
							if (elem instanceof ElementVariable) {
								stack.push(multistack.peek(elem.asText()).getValue());
							} else {
								if (elem instanceof ElementOperator) {
									Object arg2 = stack.pop();
									ValueWrapper arg1 = new ValueWrapper(stack.pop());

									ElementOperator operator = (ElementOperator) elem;
									String op = operator.getSymbol();

									switch (op) {
									case ("+"):
										arg1.add(arg2);
										break;
									case ("-"):
										arg1.subtract(arg2);
										break;
									case ("*"):
										arg1.multiply(arg2);
										break;
									case ("/"):
										arg1.divide(arg2);
										break;
									}

									stack.push(arg1.getValue());
								} else {
									ElementFunction function = (ElementFunction) elem;
									String func = function.asText();

									switch (func) {
									case ("sin"):
										Double res = Double.valueOf(stack.pop().toString());
										res = Math.sin(res * Math.PI / 180);
										stack.push(res);
										break;
									case ("decfmt"):
										Object f = stack.pop();
										Object x = stack.pop();

										if (!(x instanceof Integer) && !(x instanceof Double)
												&& !(x instanceof String)) {
											throw new RuntimeException(
													"Number that was supposed to be formated was not Integer, Double nor String!");
										}

										String[] formats = f.toString().split("\\.");
										if (formats.length != 2) {
											throw new RuntimeException("Could not parse format for @decfmt function!");
										}

										try {
											DecimalFormat df = new DecimalFormat(f.toString());
											stack.push(df.format(Double.valueOf(x.toString())));
										} catch (IllegalArgumentException ex) {
											throw new IllegalArgumentException(
													"Smart script engine could not perform @decfmt function because given format is invalid!");
										}
										break;
									case ("dup"):
										Object dup = stack.peek();
										stack.push(dup);
										break;
									case ("swap"):
										Object a = stack.pop();
										Object b = stack.pop();
										stack.push(a);
										stack.push(b);
										break;
									case ("setMimeType"):
										requestContext.setMimeType(stack.pop().toString());
										break;
									case ("paramGet"):
										Object dv = stack.pop();
										Object name = null;
										if (!stack.isEmpty()) {
											name = stack.pop();
											Object value = requestContext.getParameter(name.toString());
											stack.push(value == null ? dv : value);
										} else {
											stack.push(dv);
										}
										break;
									case ("pparamGet"):
										Object dvP = stack.pop();
										Object nameP = stack.pop();
										Object valueP = requestContext.getPersistentParameter(nameP.toString());
										stack.push(valueP == null ? dvP : valueP);
										break;
									case ("pparamSet"):
										Object namePP = stack.pop();
										Object valuePP = stack.pop();
										requestContext.setPersistentParameter(namePP.toString(), valuePP.toString());
										break;
									case ("pparamDel"):
										Object nameDel = stack.peek();
										requestContext.removePersistentParameter(nameDel.toString());
										break;
									case ("tparamGet"):
										Object dvT = stack.pop();
										Object nameT = null;
										if (!stack.isEmpty()) {
											nameT = stack.pop();
											Object valueT = requestContext.getTemporaryParameter(nameT.toString());
											stack.push(valueT == null ? dvT : valueT);
										} else {
											stack.push(dvT);
										}
										break;
									case ("tparamSet"):
										Object nameTP = stack.pop();
										Object valueTP = stack.pop();
										requestContext.setTemporaryParameter(nameTP.toString(), valueTP.toString());
										break;
									case ("tparamDel"):
										Object tnameDel = stack.peek();
										requestContext.removePersistentParameter(tnameDel.toString());
										break;
									}
								}
							}
						}
					}
				}
			}

			List<Object> list = new ArrayList<Object>();
			while (!stack.isEmpty()) {
				list.add(stack.pop());
			}
			for (int i = list.size() - 1; i >= 0; i--) {
				sb.append(list.get(i));
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0; i < node.numberOfChildren(); i++) {
				Node currentNode = node.getChild(i);

				currentNode.accept(this);
			}
		}

	};

	/**
	 * Constructs a SmartScriptEngine with document node and context set
	 * appropriately.
	 * 
	 * @param documentNode   given document node
	 * @param requestContext given context
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Executes the Smart Script of document Node.
	 */
	public void execute() {
		documentNode.accept(visitor);
		try {
			if (!sb.toString().endsWith("\r\n")) {
				sb.append("\r\n");
			}
			requestContext.write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException("Unable to write to context!");
		}
	}

}
