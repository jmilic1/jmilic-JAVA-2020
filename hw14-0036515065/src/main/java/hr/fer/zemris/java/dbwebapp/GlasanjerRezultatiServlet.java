package hr.fer.zemris.java.dbwebapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.PollEntity;
import hr.fer.zemris.java.model.PollOptionEntity;

/**
 * WebServlet for defining two lists for votingIndex.jsp to use which renders
 * necessary tables to show vote counts. One list contains only the top options
 * with the same number of votes, and the other contains all of the options
 * ordered by the number of their votes.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjerRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<PollOptionEntity> pollOptions = DAOProvider.getDao().getPollOptionEntries();

		List<PollOptionEntity> votes = new ArrayList<PollOptionEntity>();
		long pollId = ((PollEntity) (req.getSession().getAttribute("poll"))).getId();

		for (PollOptionEntity pollOption : pollOptions) {
			if (pollOption.getPollID() == pollId) {
				votes.add(pollOption);
			}
		}

		Collections.sort(votes);
		req.getSession().setAttribute("votes", votes);

		List<PollOptionEntity> bestVotes = new ArrayList<PollOptionEntity>();
		long maxVotes = votes.get(0).getVotesCount();
		for (int i = 0; i < votes.size(); i++) {
			PollOptionEntity current = votes.get(i);
			if (maxVotes > current.getVotesCount()) {
				break;
			}
			bestVotes.add(current);
		}
		req.getSession().setAttribute("bestVotes", bestVotes);

		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
}