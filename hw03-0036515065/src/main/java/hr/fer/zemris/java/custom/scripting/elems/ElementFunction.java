package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that contains a parsed function name. Valid function names start with
 * a letter and can contain any number of letters, digits or underscores.
 * 
 * @author Jura Milić
 *
 */
public class ElementFunction extends Element {
	/**
	 * Name of parsed function.
	 */
	private String name;

	/**
	 * Constructs a new ElementFunction with given function name
	 * 
	 * @param name given function name
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	@Override
	public String asText() {
		return name;
	}

	/**
	 * @return name of function in this Element
	 */
	public String getName() {
		return name;
	}
}
