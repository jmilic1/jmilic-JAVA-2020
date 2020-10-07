package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

/**
 * Class which represents a gui component containing a drawn bar chart.
 * 
 * @author Jura Milić
 *
 */
public class BarChartComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	/**
	 * Space between the west side of the screen and the name of y-axis on screen
	 */
	private static final int YDESC_X = 25;
	/**
	 * The y-pixel of the end of y-axis.
	 */
	private static final int END_YAXIS = 20;
	/**
	 * The BarChart to be drawn.
	 */
	private BarChart chart;
	/**
	 * Color used in drawing helper lines.
	 */
	Color helperLineColor = new Color(1, 0, 0, 0.25f);
	/**
	 * Map used for storing values of line levels and their respective y-coordinates
	 */
	Map<Integer, Integer> lineLevels = new HashMap<Integer, Integer>();

	/**
	 * Constructs a BarChartComponent with given chart
	 * 
	 * @param chart given chart
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}

	@Override
	public void paintComponent(Graphics g) {

		int xDescX = (int) ((getWidth() - g.getFontMetrics().getStringBounds(chart.getXDesc(), g).getWidth()) / 2);
		int xDescY = (int) (getHeight() - g.getFontMetrics().getStringBounds(chart.getXDesc(), g).getHeight());

		g.drawString(chart.getXDesc(), xDescX, xDescY);

		Graphics2D g2 = (Graphics2D) g;
		drawYDesc(g2);

		String longestYString = Integer.toString(chart.getMaxY());
		String yValString = Integer.toString(chart.getMinY());
		if (longestYString.length() < yValString.length()) {
			longestYString = yValString;
		}

		int startYVal = YDESC_X * 2;
		int len = (int) g.getFontMetrics().getStringBounds(longestYString, g).getWidth();
		int endYVal = startYVal + len;
		XYValue origin = new XYValue(endYVal + 20, xDescY - 40);
		int yValY = (int) (origin.getY() + g.getFontMetrics().getStringBounds(longestYString, g).getHeight() / 2);
		int endXAxis = getWidth() - 30;

		drawXArrow(g2, origin, yValY, endXAxis);
		drawYArrow(g2, origin);

		yValString = Integer.toString(chart.getMinY());
		int pxLength = (int) g.getFontMetrics().getStringBounds(yValString, g).getWidth();
		g2.drawString(yValString, endYVal - pxLength, yValY);
		lineLevels.put(chart.getMinY(), yValY - 5);

		int rows = (chart.getMaxY() - chart.getMinY()) / chart.getSpace() + 1;
		int shift = origin.getY() / rows;
		yValY -= shift;

		drawYValueStrings(g2, endYVal, origin.getX(), yValY, shift);

		drawHorizontalHelperLines(g2, origin, yValY, shift, endXAxis);

		List<Integer> uniqueXValues = new ArrayList<Integer>();
		for (XYValue val : chart.getValues()) {
			int value = val.getX();
			if (!uniqueXValues.contains(value)) {
				uniqueXValues.add(value);
			}
		}
		Collections.sort(uniqueXValues);

		int xAxisSegment = (endXAxis - origin.getX()) / uniqueXValues.size();

		drawXValueStrings(g2, origin, endXAxis, uniqueXValues, xAxisSegment);

		drawVerticalHelperLines(g2, origin, endXAxis, xAxisSegment);

		drawRectangles(g2, origin, uniqueXValues, xAxisSegment);
	}

	/**
	 * Draws an arrow on top of y-axis based on given origin.
	 * 
	 * @param g2     given context
	 * @param origin given origin
	 */
	private void drawYArrow(Graphics2D g2, XYValue origin) {
		g2.setStroke(new BasicStroke((float) 2));
		g2.setPaint(Color.DARK_GRAY);
		g2.drawLine(origin.getX(), origin.getY() + 15, origin.getX(), END_YAXIS);

		g2.drawLine(origin.getX() - 5, END_YAXIS, origin.getX() + 5, END_YAXIS);
		g2.drawLine(origin.getX() - 5, END_YAXIS, origin.getX(), END_YAXIS - 5);
		g2.drawLine(origin.getX() + 5, END_YAXIS, origin.getX(), END_YAXIS - 5);
	}

	/**
	 * Draws an arrow at the end of x-axis based on given origin, given y-pixel and
	 * given end of x-axis.
	 * 
	 * @param g2       given context
	 * @param origin   given origin
	 * @param y        given y-pixel level
	 * @param endXAxis given x-pixel which is the end of x-axis
	 */
	private void drawXArrow(Graphics2D g2, XYValue origin, int y, int endXAxis) {
		g2.setStroke(new BasicStroke((float) 2));
		g2.setPaint(Color.DARK_GRAY);
		g2.drawLine(origin.getX() - 10, y - 5, endXAxis, y - 5);

		int endOfxArrow = endXAxis + 5;
		g2.drawLine(endXAxis, y - 10, endXAxis, y);
		g2.drawLine(endXAxis, y, endOfxArrow, y - 5);
		g2.drawLine(endXAxis, y - 10, endOfxArrow, y - 5);
	}

	/**
	 * Draws the rectangle bars of the chart.
	 * 
	 * @param g2      given context
	 * @param origin  given origin
	 * @param xValues given list of x-values
	 * @param width   given width of a single bar
	 */
	private void drawRectangles(Graphics2D g2, XYValue origin, List<Integer> xValues, int width) {
		for (XYValue val : chart.getValues()) {
			int xSegment = xValues.indexOf(val.getX());
			int x = origin.getX() + xSegment * width;
			int y = lineLevels.get(val.getY());
			int height = origin.getY() - y;

			g2.setPaint(Color.black);
			g2.drawRect(x, y, width, height);
			g2.setPaint(Color.CYAN);
			g2.fillRect(x, y, width, height);
		}
	}

	/**
	 * Draws horizontal helper lines.
	 * 
	 * @param g2       context
	 * @param origin   origin
	 * @param yValY    y-level of the first helper line
	 * @param shift    the amount by which the level should be shifted
	 * @param endXAxis x-pixel of the end of x-axis
	 */
	private void drawHorizontalHelperLines(Graphics2D g2, XYValue origin, int yValY, int shift, int endXAxis) {
		g2.setPaint(helperLineColor);
		for (int i = chart.getMinY() + chart.getSpace(); i <= chart.getMaxY(); i += chart.getSpace()) {
			g2.setPaint(helperLineColor);
			g2.drawLine(origin.getX() - 10, yValY - 5, endXAxis, yValY - 5);
			lineLevels.put(i, yValY - 5);
			yValY -= shift;
		}
	}

	/**
	 * Draws the Strings of Y-values. All of the Strings are shifted to the right by
	 * a certain amount as dictated by the value which has the longest String.
	 * 
	 * @param g2      context
	 * @param endYVal x-pixel of the end of the longest Y-value
	 * @param originX x-value of the origin
	 * @param yValY   y-level of the first String
	 * @param shift   the amount by which the level should be shifted
	 */
	private void drawYValueStrings(Graphics2D g2, int endYVal, int originX, int yValY, int shift) {
		g2.setPaint(Color.black);
		for (int i = chart.getMinY() + chart.getSpace(); i <= chart.getMaxY(); i += chart.getSpace()) {
			g2.setPaint(Color.BLACK);
			String currNum = Integer.toString(i);
			int num = (int) g2.getFontMetrics().getStringBounds(currNum, g2).getWidth();
			g2.drawString(currNum, endYVal - num, yValY);
			yValY -= shift;
		}
	}

	/**
	 * Draws vertical helper lines
	 * 
	 * @param g2           context
	 * @param origin       origin
	 * @param endXAxis     x-pixel of the end of x-axis
	 * @param xAxisSegment the length of a single x-value segment
	 */
	private void drawVerticalHelperLines(Graphics2D g2, XYValue origin, int endXAxis, int xAxisSegment) {
		g2.setPaint(helperLineColor);
		for (int currSeg = xAxisSegment + origin.getX(); currSeg < endXAxis; currSeg += xAxisSegment) {
			g2.drawLine(currSeg, origin.getY() + 10, currSeg, END_YAXIS);
		}
	}

	/**
	 * Draws the Strings of X-values
	 * 
	 * @param g2           context
	 * @param origin       origin
	 * @param endXAxis     x-pixel of the end of x-axis
	 * @param xValues      list of X-values
	 * @param xAxisSegment the length of a single x-value segment
	 */
	private void drawXValueStrings(Graphics2D g2, XYValue origin, int endXAxis, List<Integer> xValues,
			int xAxisSegment) {
		g2.setPaint(Color.black);
		int i = 0;
		for (int currSeg = xAxisSegment + origin.getX(); currSeg < endXAxis; currSeg += xAxisSegment) {
			String xValueString = Integer.toString(xValues.get(i));

			int strWidth = (int) g2.getFontMetrics().getStringBounds(xValueString, g2).getWidth();
			int strHeight = (int) g2.getFontMetrics().getStringBounds(xValueString, g2).getHeight();

			g2.setPaint(Color.black);
			g2.drawString(xValueString, currSeg - xAxisSegment / 2 - strWidth, origin.getY() + strHeight + 5);

			i++;
		}
	}

	/**
	 * Draws the name of Y-axis on given context
	 * 
	 * @param g2 given context
	 */
	private void drawYDesc(Graphics2D g2) {
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2.setTransform(at);

		int yDescLen = (int) (g2.getFontMetrics().getStringBounds(chart.getYDesc(), g2).getWidth());
		int yDescX = (getHeight() + yDescLen) / 2;
		int yDescY = YDESC_X;
		g2.drawString(chart.getYDesc(), -yDescX, yDescY);

		at.rotate(Math.PI / 2);
		g2.setTransform(at);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 650);
	}
}
