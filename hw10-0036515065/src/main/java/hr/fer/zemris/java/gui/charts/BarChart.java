package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Class which models a single bar chart.
 * 
 * @author Jura Milić
 *
 */
public class BarChart {
	/**
	 * XYValues of the chart.
	 */
	private List<XYValue> values;
	/**
	 * Description of x-axis
	 */
	private String xDesc;
	/**
	 * Description of y-axis
	 */
	private String yDesc;
	/**
	 * Smallest Y-value
	 */
	private int minY;
	/**
	 * Maximum Y-value.
	 */
	private int maxY;
	/**
	 * Y-value difference between two rows of chart.
	 */
	private int space;

	/**
	 * Constructs a BarChart with variables set appropriately.
	 * 
	 * @param values given XYValues
	 * @param xDesc  given description of x-axis
	 * @param yDesc  given description of y-axis
	 * @param minY   given minimum Y-value
	 * @param maxY   given maximum Y-value
	 * @param space  given space
	 */
	public BarChart(List<XYValue> values, String xDesc, String yDesc, int minY, int maxY, int space) {
		this.values = values;
		this.xDesc = xDesc;
		this.yDesc = yDesc;
		if (minY < 0) {
			throw new IllegalArgumentException("Minimum Y value cannot be negative!");
		}
		this.minY = minY;
		if (maxY <= minY) {
			throw new IllegalArgumentException("Maximum Y value must be greater than the minimym Y value!");
		}
		this.maxY = maxY;
		this.space = space;
		if ((maxY - minY) % space != 0) {
			minY += space - (maxY - minY) % space;
		}

		for (XYValue value : values) {
			if (value.getY() < minY) {
				throw new IllegalArgumentException("Y value cannot be less than the minimum Y value!");
			}
		}
	}

	/**
	 * @return xDescription
	 */
	public String getXDesc() {
		return xDesc;
	}

	/**
	 * @return yDescription
	 */
	public String getYDesc() {
		return yDesc;
	}

	/**
	 * @return minimum Y-value
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * @return maximum Y-value
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * @return space
	 */
	public int getSpace() {
		return space;
	}

	/**
	 * @return XY values
	 */
	public List<XYValue> getValues() {
		return values;
	}
}
