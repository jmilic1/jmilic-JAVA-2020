package hr.fer.zemris.java.gui.layouts;

/**
 * Class used as a constraint in a guy layout. RCPosition indicates the row and
 * column position inside the matrix-like layout.
 * 
 * @author Jura Milić
 *
 */
public class RCPosition {
	/**
	 * Index of row position
	 */
	private int row;
	/**
	 * Index of column position
	 */
	private int column;

	/**
	 * Parses the given String into an instance of RCPosition. The text needs to be
	 * given as "number,number".
	 * 
	 * @param text
	 * @return
	 */
	public static RCPosition parse(String text) {
		if (text.length() != 3) {
			throw new IllegalArgumentException("Input needs to be given as \"number,number\"!");
		}

		int currentIndex = 0;
		int row = 0;
		int column = 0;
		char c = text.charAt(currentIndex++);

		StringBuilder sb = new StringBuilder();
		while (c != ',') {
			sb.append(c);
			c = text.charAt(currentIndex++);
		}

		try {
			row = Integer.valueOf(sb.toString());
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("First parameter was not a number!");
		}
		c = text.charAt(currentIndex++);

		if (c != ',') {
			throw new IllegalArgumentException("Character ',' must be between row and column numbers!");
		}

		c = text.charAt(currentIndex++);
		sb = new StringBuilder();
		while (currentIndex != text.length() - 2) {
			sb.append(c);
			c = text.charAt(currentIndex++);
		}
		sb.append(c);
		try {
			column = Integer.valueOf(sb.toString());
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Last parameter was not a number!");
		}

		return new RCPosition(row, column);
	}

	/**
	 * Constructs a RCPosition with row and column indexes set appropriately to
	 * given indexes.
	 * 
	 * @param row    given row index
	 * @param column given column index
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * @return row index
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return column index
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCPosition other = (RCPosition) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
