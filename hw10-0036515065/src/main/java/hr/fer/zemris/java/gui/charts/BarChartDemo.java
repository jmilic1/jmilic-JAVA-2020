package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Program demo for drawing a bar chart from data stored in a file whose file
 * path is given through arguments. The data file needs to contain at least 5
 * lines and contents of these 5 lines need to be as such:</br>
 * 
 * 1. Name of x-axis </br>
 * 2. Name of y-axis </br>
 * 3. XY values of points given as "x1,y1 x2,y2..." </br>
 * 4. Minimum Y value </br>
 * 5. Maximum Y value </br>
 * 6. Space between 2 rows in chart
 * 
 * @author Jura Milić
 *
 */
public class BarChartDemo extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a BarChartDemo with given chart and path of data input.
	 * 
	 * @param chart given chart
	 * @param input path of input
	 */
	public BarChartDemo(BarChart chart, String input) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI(chart, input);
		pack();
	}

	/**
	 * Initializes the gui.
	 * 
	 * @param chart given chart
	 * @param input given path of input
	 */
	private void initGUI(BarChart chart, String input) {
		setVisible(true);
		setLayout(new BorderLayout());
		getContentPane().add(new BarChartComponent(chart), BorderLayout.CENTER);
		getContentPane().add(new JLabel(input, SwingConstants.CENTER), BorderLayout.NORTH);

	}

	/**
	 * Start of program. Only one parameter is allowed
	 * 
	 * @param args path of data input
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments!");
			System.exit(-1);
		}
		String path = args[0];
		String input = "";
		try {
			input = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.out.println("File could not be opened!");
			System.exit(-1);
		}

		DataParser parser = null;
		try {
			parser = new DataParser(input);
		} catch (Exception e) {
			System.out.println("Unable to parse data!");
			System.exit(-1);
		}

		BarChart chart = parser.getChart();

		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(chart, path).setVisible(true);
		});
	}
}
