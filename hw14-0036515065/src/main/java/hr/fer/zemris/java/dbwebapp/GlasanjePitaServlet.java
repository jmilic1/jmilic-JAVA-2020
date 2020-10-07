package hr.fer.zemris.java.dbwebapp;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.model.PollOptionEntity;

/**
 * WebServlet which sends an image of a PieChart representing the vote counts
 * for each option.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/glasanje-grafika")
public class GlasanjePitaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		List<PollOptionEntity> votes = (List<PollOptionEntity>) req.getSession().getAttribute("votes");
		DefaultPieDataset dataset = new DefaultPieDataset();

		for (PollOptionEntity vote : votes) {
			dataset.setValue(vote.getTitle(), vote.getVotesCount());
		}

		JFreeChart chart = ChartFactory.createPieChart("Rezultati", dataset, true, true, false);

		resp.setContentType("image/png");
		ChartUtils.writeChartAsPNG(resp.getOutputStream(), chart, 400, 400);
	}
}
