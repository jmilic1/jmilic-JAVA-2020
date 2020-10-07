package hr.fer.zemris.java.web.servlets;

import java.io.IOException;
import java.util.Date;

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

/**
 * Servlet which handles creation of new comments on a certain blog entry.
 * Comments are added to database.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/newComment")
public class NewComment extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/newComment.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = req.getParameter("message");

		EntityManager em = JPAEMProvider.getEntityManager();
		BlogComment comment = new BlogComment();
		comment.setBlogEntry((BlogEntry) req.getSession().getAttribute("entry"));
		comment.setMessage(text);
		comment.setPostedOn(new Date());
		Long userID = (Long) req.getSession().getAttribute("current.user.id");
		comment.setUsersEMail(DAOProvider.getDAO().getBlogUserById(userID).getEmail());
		em.persist(comment);

		resp.sendRedirect("../servleti/authors");
	}
}
