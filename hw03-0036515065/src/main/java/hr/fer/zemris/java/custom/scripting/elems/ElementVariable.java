package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that contains a parsed variable name. Valid variable names start with
 * a letter and contain any number of letters, digits and underscores.
 * 
 * @author Jura Milić
 *
 */
public class ElementVariable extends Element {
	/**
	 * Parsed variable.
	 */
	String name;

	/**
	 * Constructs an ElementVariable with given variable name
	 * 
	 * @param name given variable name
	 */
	public ElementVariable(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}

	/**
	 * @return variable name of this Element
	 */
	public String getName() {
		return name;
	}
}
