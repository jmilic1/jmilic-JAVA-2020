package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * Primitive parser used for parsing a given String input into parameters of a
 * BarChart.
 * 
 * @author Jura Milić
 *
 */
public class DataParser {
	/**
	 * Data to be parsed.
	 */
	char[] data;
	/**
	 * Index of next character to be read.
	 */
	int currentIndex;
	/**
	 * Parsed chart
	 */
	BarChart chart;

	/**
	 * Constructs a DataParser with given String and immediately starts to parse it.
	 * 
	 * @param input given String
	 */
	public DataParser(String input) {
		data = input.toCharArray();
		currentIndex = 0;
		parse();
	}

	/**
	 * Parses the input
	 */
	private void parse() {
		char c = data[currentIndex];
		StringBuilder sb = new StringBuilder();
		while (c != '\n') {
			sb.append(c);
			c = data[++currentIndex];
		}
		String xDesc = sb.toString();
		sb = new StringBuilder();
		c = data[++currentIndex];

		while (c != '\n') {
			sb.append(c);
			c = data[++currentIndex];
		}
		String yDesc = sb.toString();
		sb = new StringBuilder();
		c = data[++currentIndex];
		List<XYValue> values = new ArrayList<XYValue>();
		while (c != '\n') {
			while (c != ',') {
				sb.append(c);
				c = data[++currentIndex];
			}
			int x = Integer.valueOf(sb.toString());
			sb = new StringBuilder();
			c = data[++currentIndex];
			while (c != ' ' && c != '\n') {
				sb.append(c);
				c = data[++currentIndex];
			}
			int y = Integer.valueOf(sb.toString());
			values.add(new XYValue(x, y));
			sb = new StringBuilder();
			if (c != '\n') {
				c = data[++currentIndex];
			}
		}

		c = data[++currentIndex];
		while (c != '\n') {
			sb.append(c);
			c = data[++currentIndex];
		}
		int minY = Integer.valueOf(sb.toString());
		sb = new StringBuilder();

		c = data[++currentIndex];
		while (c != '\n') {
			sb.append(c);
			c = data[++currentIndex];
		}
		int maxY = Integer.valueOf(sb.toString());
		sb = new StringBuilder();
		c = data[++currentIndex];

		while (c != '\n' && currentIndex < data.length) {
			sb.append(c);
			currentIndex++;
			if (currentIndex != data.length) {
				c = data[currentIndex];
			}
		}
		int space = Integer.valueOf(sb.toString());

		chart = new BarChart(values, xDesc, yDesc, minY, maxY, space);
	}

	/**
	 * @return parsed chart
	 */
	public BarChart getChart() {
		return chart;
	}
}
