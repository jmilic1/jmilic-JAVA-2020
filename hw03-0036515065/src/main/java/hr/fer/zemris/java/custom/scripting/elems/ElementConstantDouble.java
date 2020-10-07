package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that contains a parsed double.
 * 
 * @author Jura Milić
 *
 */
public class ElementConstantDouble extends Element {
	/**
	 * Parsed double
	 */
	private double value;

	/**
	 * Constructs an ElementConstantDouble with given value
	 * 
	 * @param value given value
	 */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	@Override
	public String asText() {
		return String.valueOf(value);
	}

	/**
	 * @return value of this Element
	 */
	public double getValue() {
		return value;
	}
}
