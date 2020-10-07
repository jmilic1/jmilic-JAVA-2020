package hr.fer.zemris.java.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebServlet for defining two lists for glasanjeIndex.jsp to use which renders
 * necessary tables to show vote counts. One list contains only the top bands
 * with the same number of votes, and the other contains all of the bands
 * ordered by the number of their votes.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/glasanje-rezultati")
public class GlasanjerRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		File file = new File(fileName);

		if (file.length() == 0) {
			GlasanjeGlasajServlet.createFile(req, file, new PrintWriter(file));
		}

		String data = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] lines = data.split("\\r?\\n");
		Map<Integer, Integer> voteResults = new HashMap<Integer, Integer>();
		for (String line : lines) {
			String[] twoNumbers = line.split("\\t");
			voteResults.put(Integer.parseInt(twoNumbers[0]), Integer.parseInt(twoNumbers[twoNumbers.length - 1]));
		}

		fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		file = new File(fileName);
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		lines = data.split("\\r?\\n");

		List<BandVote> bandVotes = new ArrayList<BandVote>();
		for (String line : lines) {
			String[] params = line.split("\\t");
			int votes = voteResults.get(Integer.parseInt(params[0]));
			bandVotes.add(new BandVote(params[1], votes, params[2]));
		}

		Collections.sort(bandVotes);
		req.getSession().setAttribute("bandVotes", bandVotes);

		List<BandVote> bestBands = new ArrayList<BandVote>();
		int votes = bandVotes.get(0).voteCount;
		for (int i = 0; i < bandVotes.size(); i++) {
			BandVote current = bandVotes.get(i);
			if (votes > current.getVoteCount()) {
				break;
			}

			bestBands.add(current);
		}
		req.getSession().setAttribute("bestBands", bestBands);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

	/**
	 * Class which models a band with their vote count.
	 * 
	 * @author Jura Milić
	 *
	 */
	public static class BandVote implements Comparable<BandVote> {
		/**
		 * Name of band.
		 */
		private String bandName;
		/**
		 * Number of votes for band.
		 */
		private Integer voteCount;
		/**
		 * Sample song of band.
		 */
		private String songUrl;

		/**
		 * Constructs a BandVote with parameters set appropriately.
		 * 
		 * @param bandName  given name
		 * @param voteCount given vote count
		 * @param songUrl   given song
		 */
		private BandVote(String bandName, Integer voteCount, String songUrl) {
			this.bandName = bandName;
			this.voteCount = voteCount;
			this.songUrl = songUrl;
		}

		/**
		 * Gets the name of the band
		 * 
		 * @return bandName
		 */
		public String getBandName() {
			return bandName;
		}

		/**
		 * Gets the number of votes
		 * 
		 * @return voteCount
		 */
		public Integer getVoteCount() {
			return voteCount;
		}

		/**
		 * Gets the URL of the song
		 * 
		 * @return songUrl
		 */
		public String getSongUrl() {
			return songUrl;
		}

		@Override
		public int compareTo(BandVote o) {
			return o.voteCount.compareTo(voteCount);
		}

	}
}