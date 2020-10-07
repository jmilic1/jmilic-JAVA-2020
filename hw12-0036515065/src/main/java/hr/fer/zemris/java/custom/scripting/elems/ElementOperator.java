package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Element that contains a parsed operator. Valid operators are + - * / ^
 * 
 * @author Jura Milić
 *
 */
public class ElementOperator extends Element {
	/**
	 * Parsed operator
	 */
	private String symbol;

	/**
	 * 
	 * @param symbol
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}

	/**
	 * @return operator stored in this Element
	 */
	public String getSymbol() {
		return symbol;
	}
}
