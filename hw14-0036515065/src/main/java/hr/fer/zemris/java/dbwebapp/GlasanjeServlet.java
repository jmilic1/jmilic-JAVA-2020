package hr.fer.zemris.java.dbwebapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollOptionEntity;

/**
 * WebServlet which reads option information and puts int in a list for
 * glasanjeIndex.jsp to use for a voting list. Option information is extracted
 * from database depending on the given pollId which is sent through parameters.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		DAO dao = DAOProvider.getDao();
		List<PollOptionEntity> pollOptions = dao.getPollOptionEntries();
		List<PollOptionEntity> actualOptions = new ArrayList<PollOptionEntity>();

		Integer pollId = Integer.parseInt(req.getParameter("pollID"));

		for (PollOptionEntity pollOption : pollOptions) {
			if (pollOption.getPollID() == pollId) {
				System.out.println(pollOption.getTitle());
				actualOptions.add(pollOption);
			}
		}

		req.getSession().setAttribute("pollActualOptions", actualOptions);
		req.getSession().setAttribute("poll", dao.getPollEntry(pollId));
		req.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(req, resp);
	}
}
