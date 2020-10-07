package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * WebServlet which creates a PieChart with made up data.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/dynamicChart")
public class DynamicChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Linux", 29);
		dataset.setValue("Windows", 10);
		dataset.setValue("Mac OS", 20);
		dataset.setValue("Other", 5);

		JFreeChart chart = ChartFactory.createPieChart("OS usage", dataset, true, true, false);

		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, 400, 400);
	}
}