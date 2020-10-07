package hr.fer.zemris.java.servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servlets.GlasanjeServlet.Band;

/**
 * WebServlet which adds one vote to the total number of votes to a single band
 * given through parameters. Vote counts are stored in
 * WEB-INF/glasanje-rezultati.txt file.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

		File file = Paths.get(fileName).toFile();
		PrintWriter pw = new PrintWriter(new FileWriter(file, true));

		if (file.length() == 0) {
			createFile(req, file, pw);
		}

		String vote = req.getParameter("id").trim();
		String data = new String(Files.readAllBytes(Paths.get(fileName)));
		String[] lines = data.split("\\r?\\n");

		StringBuilder newFileText = new StringBuilder();
		for (String line : lines) {
			String[] twoNumbers = line.split("\\t");
			if (twoNumbers[0].equals(vote)) {
				newFileText.append(twoNumbers[0] + "\t" + (Integer.parseInt(twoNumbers[1]) + 1));
			} else {
				newFileText.append(twoNumbers[0] + "\t" + twoNumbers[1]);
			}
			newFileText.append("\n");
		}

		pw.write(newFileText.toString());
		pw.flush();
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}

	/**
	 * Helper method for creating a default text file with vote counts set to zero
	 * for each band.
	 * 
	 * @param req  given request
	 * @param file given text file
	 * @param pw   given print writer
	 * @throws IOException if new file could not be created
	 */
	public static void createFile(HttpServletRequest req, File file, PrintWriter pw) throws IOException {
		StringBuilder sb = new StringBuilder();
		file.createNewFile();

		@SuppressWarnings("unchecked")
		List<Band> bands = (List<Band>) req.getSession().getAttribute("bands");

		req.setAttribute("bands", bands);
		for (Band band : bands) {
			sb.append(band.getId());
			sb.append("\t");
			sb.append("0");
			sb.append("\n");
		}

		pw.write(sb.toString());
		pw.flush();
	}
}
