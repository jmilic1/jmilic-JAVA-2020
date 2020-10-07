package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that contains a parsed integer.
 * 
 * @author Jura Milić
 *
 */
public class ElementConstantInteger extends Element {
	/**
	 * parsed integer
	 */
	private int value;

	/**
	 * Constructs an ElementContantInteger with given value
	 * 
	 * @param value given value
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return String.valueOf(value);
	}

	/**
	 * @return value of this Element
	 */
	public int getValue() {
		return value;
	}
}
