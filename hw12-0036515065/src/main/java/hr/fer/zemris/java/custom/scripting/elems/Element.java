package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element is basic class for types of parsed data. Types of Elements contain:
 * Double, Integer, Function, Operator, String and Variable
 * 
 * @author Jura Milić
 *
 */
public class Element {
	/**
	 * Returns value stored in this Element as String
	 * 
	 * @return String value of Element
	 */
	public String asText() {
		String res = "";
		return res;
	}

	@Override
	public boolean equals(Object element) {
		if (this.getClass() != element.getClass())
			return false;

		Element elem = (Element) element;
		if (this.asText().equals((elem).asText()))
			return true;
		return false;
	}
}
