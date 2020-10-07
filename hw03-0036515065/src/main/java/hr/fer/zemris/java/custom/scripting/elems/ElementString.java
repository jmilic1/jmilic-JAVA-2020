package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that contains a parsed String.
 * 
 * @author Jura Milić
 *
 */
public class ElementString extends Element {
	/**
	 * Parsed string.
	 */
	private String value;

	/**
	 * Constructs an ElementString with given String
	 * 
	 * @param value given String
	 */
	public ElementString(String value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return value;
	}

	/**
	 * @return value of this Element
	 */
	public String getValue() {
		return value;
	}
}
