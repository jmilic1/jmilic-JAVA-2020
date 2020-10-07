package hr.fer.zemris.java.dbwebapp;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.sql.SQLDAO;
import hr.fer.zemris.java.model.PollEntity;

/**
 * WebServlet which sends available Polls to pollIndex.jsp for rendering.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/index.html")
public class RenderPollsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SQLDAO sqlDao = new SQLDAO();
		List<PollEntity> polls = sqlDao.getPollEntries();

		req.getSession().setAttribute("polls", polls);
		req.getRequestDispatcher("/WEB-INF/pages/pollIndex.jsp").forward(req, resp);
	}
}