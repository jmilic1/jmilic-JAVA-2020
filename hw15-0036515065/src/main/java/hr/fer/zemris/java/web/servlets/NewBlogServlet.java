package hr.fer.zemris.java.web.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.model.BlogComment;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Servlet which handles creation of new BlogEntries. New entries are added to
 * database.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/newBlog")
public class NewBlogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/newBlog.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = (String) req.getParameter("text");
		String title = (String) req.getParameter("title");

		List<BlogComment> comments = new ArrayList<BlogComment>();
		Date date = new Date();
		BlogUser user = DAOProvider.getDAO()
				.getBlogUserByNick((String) req.getSession().getAttribute("current.user.nick"));

		EntityManager em = JPAEMProvider.getEntityManager();
		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setComments(comments);
		blogEntry.setCreatedAt(date);
		blogEntry.setLastModifiedAt(date);
		blogEntry.setCreator(user);
		blogEntry.setText(text);
		blogEntry.setTitle(title);
		em.persist(blogEntry);

		req.setAttribute("entry", blogEntry);
		req.getSession().setAttribute("entry", blogEntry);
		req.getRequestDispatcher("/WEB-INF/pages/comment.jsp").forward(req, resp);
	}
}