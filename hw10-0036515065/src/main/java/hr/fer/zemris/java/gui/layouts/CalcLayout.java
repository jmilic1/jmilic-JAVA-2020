package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A custom LayoutManager implementation used for laying out a calculator gui.
 * The maximum number of rows and columns of the calculator are fixed and the
 * LayoutManager will throw an exception if an illegal row or column is set.
 * 
 * @author Jura Milić
 *
 */
public class CalcLayout implements LayoutManager2 {
	/**
	 * Maximum number of columns
	 */
	private static final int NUMBER_OF_COLUMNS = 7;
	/**
	 * Maximum number of rows
	 */
	private static final int NUMBER_OF_ROWS = 5;
	/**
	 * Width of the component located at (1,1)
	 */
	private static final int FIRST_WIDTH = 5;

	/**
	 * How many pixels should be between each component
	 */
	private int space;

	/**
	 * A map used for storing Components and their respective position in the
	 * layout.
	 */
	private Map<Component, RCPosition> components;

	/**
	 * Constructs CalcLayout with space set to zero.
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * Constructs CalcLayout with given space between components
	 * 
	 * @param space given space
	 */
	public CalcLayout(int space) {
		this.space = space;
		components = new HashMap<Component, RCPosition>();
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.remove(comp);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calcDimension(parent.getInsets(), "preferred");
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calcDimension(parent.getInsets(), "minimum");
	}

	@Override
	public void layoutContainer(Container parent) {
		int height = parent.getHeight();
		int width = parent.getWidth();

		Insets insets = parent.getInsets();

		width += -insets.left - insets.right - space * (NUMBER_OF_COLUMNS - 1);
		height += -insets.top - insets.bottom - space * (NUMBER_OF_ROWS - 1);

		int compWidth = width / NUMBER_OF_COLUMNS;
		int compHeight = height / NUMBER_OF_ROWS;

		for (Entry<Component, RCPosition> entry : components.entrySet()) {
			Component comp = entry.getKey();
			RCPosition pos = entry.getValue();
			int row = pos.getRow() - 1;
			int column = pos.getColumn() - 1;
			int x = insets.left;
			int y = insets.top;
			int tempWidth = compWidth;
			int tempHeight = compHeight;

			if (row == 0 && column == 0) {
				tempWidth = compWidth * FIRST_WIDTH + space * (FIRST_WIDTH - 1);
			} else {
				x += (space + compWidth) * column;
				y += (space + compHeight) * row;
			}
			comp.setBounds(x, y, tempWidth, tempHeight);
		}
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition pos = getConstraint(constraints);
		components.put(comp, pos);
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calcDimension(target.getInsets(), "maximum");
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	/**
	 * Calculates the specified dimension of this layout.
	 * 
	 * @param insets        given insets
	 * @param typeDimension specified Dimension (allowed values are "maximum",
	 *                      "preferred" and "minimum")
	 * @return calculated Dimension
	 */
	private Dimension calcDimension(Insets insets, String typeDimension) {
		int width = 0;
		int height = 0;

		for (Entry<Component, RCPosition> entry : components.entrySet()) {
			Component comp = entry.getKey();
			Dimension dim = null;

			switch (typeDimension) {
			case ("maximum"):
				dim = comp.getMaximumSize();
				break;
			case ("minimum"):
				dim = comp.getMinimumSize();
				break;
			case ("preferred"):
				dim = comp.getPreferredSize();
			}

			RCPosition pos = entry.getValue();

			if (pos.getColumn() == 1 && pos.getRow() == 1) {
				int tempWidth = (int) dim.getWidth() - space * (FIRST_WIDTH - 1);
				tempWidth = (int) Math.round(tempWidth / FIRST_WIDTH);
				dim.setSize(tempWidth, dim.getHeight());
			}

			width = Math.max(dim.width, width);
			height = Math.max(dim.height, height);
		}

		width = width * NUMBER_OF_COLUMNS + space * (NUMBER_OF_COLUMNS - 1);
		height = height * NUMBER_OF_ROWS + space * (NUMBER_OF_ROWS - 1);

		width += insets.left + insets.right;
		height += insets.bottom + insets.top;

		return new Dimension(width, height);
	}

	/**
	 * Returns an instance of RCPosition from given constraint. Constraint must be
	 * either an instance of RCPosition already or a String. (1,2) (1,3) (1,4) (1,5)
	 * and values with row values outside of range[1,5] or column values outside of
	 * range [1,7] are not allowed. There cannot be multiple positions with same
	 * values in layout.
	 * 
	 * @param constraint given constraint
	 * @return generated RCPosition
	 * @throws NullPointerException     if given constraint was null
	 * @throws CalcLayoutException      if RCPosition already exists or if its
	 *                                  values are invalid
	 * @throws IllegalArgumentException if given constraint was not String nor
	 *                                  RCPosition
	 */
	private RCPosition getConstraint(Object constraint) {
		if (constraint == null) {
			throw new NullPointerException("Constraint cannot be null!");
		}

		if (constraint instanceof String) {
			constraint = RCPosition.parse((String) constraint);
		}

		RCPosition pos = null;

		if (constraint instanceof RCPosition) {
			pos = (RCPosition) constraint;
			int row = pos.getRow();
			int column = pos.getColumn();

			if (row < 1 || row > 5 || column < 1 || column > 7) {
				throw new CalcLayoutException("Illegal RCPosition arguments!");
			}

			if (row == 1 && column > 1 && column < 6) {
				throw new CalcLayoutException("Column position cannot be [2, 5] if row is 1!");
			}

			if (components.containsValue(pos)) {
				throw new CalcLayoutException("RCPosition already exists!");
			}
		} else {
			throw new IllegalArgumentException("Illegal constraint!");
		}

		return pos;
	}
}
