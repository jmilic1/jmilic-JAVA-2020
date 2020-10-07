package hr.fer.zemris.java.web.servlets;

import java.io.IOException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.model.BlogEntry;

/**
 * Servlet which can process blogEntry editing. Any changes are saved in
 * database.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/editBlog")
public class EditBlogServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogEntry entry = (BlogEntry) req.getSession().getAttribute("entry");
		req.setAttribute("title", entry.getTitle());
		req.setAttribute("text", entry.getText());
		req.getRequestDispatcher("/WEB-INF/pages/editBlog.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = (String) req.getParameter("text");
		String title = (String) req.getParameter("title");
		
		BlogEntry entry = (BlogEntry) req.getSession().getAttribute("entry");
		Date date = new Date();

		entry.setLastModifiedAt(date);
		entry.setText(text);
		entry.setTitle(title);

		EntityManager em = JPAEMProvider.getEntityManager();

		BlogEntry blogEntry = em.find(BlogEntry.class, entry.getId());
		blogEntry.setLastModifiedAt(date);
		blogEntry.setText(text);
		blogEntry.setTitle(title);

		req.setAttribute("entry", blogEntry);
		req.getRequestDispatcher("/WEB-INF/pages/comment.jsp").forward(req, resp);
		return;
	}

}
