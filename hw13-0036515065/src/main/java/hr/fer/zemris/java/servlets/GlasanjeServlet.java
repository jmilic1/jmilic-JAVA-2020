package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebServlet which reads band information and puts them in a list for
 * glasanjeIndex.jsp to use for a voting list. Band information is extracted
 * from WEB-INF/glasanje-definicija.txt file.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");

		String data = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] lines = data.split("\\r?\\n");

		List<Band> bands = new ArrayList<Band>();
		for (String line : lines) {
			String[] params = line.split("\\t");

			bands.add(new Band(Integer.parseInt(params[0]), params[1], params[2]));
		}

		Collections.sort(bands);
		req.getSession().setAttribute("bands", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}

	/**
	 * A class which models basic band information.
	 * 
	 * @author Jura Milić
	 *
	 */
	public static class Band implements Comparable<Band> {
		/**
		 * Id of band.
		 */
		private Integer id;
		/**
		 * Name of band.
		 */
		private String bandName;
		/**
		 * Sample song of band.
		 */
		private String songUrl;

		/**
		 * Constructs a Band with parameters set appropriately.
		 * 
		 * @param id       given id
		 * @param bandName given name
		 * @param songUrl  given song
		 */
		private Band(Integer id, String bandName, String songUrl) {
			this.id = id;
			this.bandName = bandName;
			this.songUrl = songUrl;
		}

		/**
		 * Gets the id of band
		 * 
		 * @return id
		 */
		public Integer getId() {
			return id;
		}

		/**
		 * Gets the band's name
		 * 
		 * @return bandName
		 */
		public String getBandName() {
			return bandName;
		}

		/**
		 * Gets the song of the band
		 * 
		 * @return songUrl
		 */
		public String getSongUrl() {
			return songUrl;
		}

		@Override
		public int compareTo(Band o) {
			return this.id.compareTo(o.id);
		}
	}
}
