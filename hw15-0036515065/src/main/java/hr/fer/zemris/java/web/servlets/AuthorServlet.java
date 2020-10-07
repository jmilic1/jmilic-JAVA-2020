package hr.fer.zemris.java.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.BlogEntry;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Servlet which, depending on the request, redirects the user to servlets or
 * pages which process the creation of new blog entries, editing of old ones,
 * listing of blogs made by a user, or show a certain blog entry with its
 * comments.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/author/*")
public class AuthorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String info = req.getPathInfo().substring(1);
		String[] infos = info.split("/");

		if (infos.length == 1) {
			System.out.println("Ja sam na infos.length = 1");
			String nick = infos[0];
			BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);

			List<BlogEntry> entries = user.getBlogs();

			req.setAttribute("entries", entries);
			req.setAttribute("nick", nick);
			req.getRequestDispatcher("/WEB-INF/pages/blogs.jsp").forward(req, resp);
		} else {
			String nick = infos[0];
			String other = infos[1];

			if (other.equals("new")) {
				if (nick.equals(req.getSession().getAttribute("current.user.nick"))) {
					req.getRequestDispatcher("/servleti/newBlog").forward(req, resp);
				} else {
					req.getRequestDispatcher("/WEB-INF/pages/error.jsp");
				}
			} else {
				if (other.equals("edit")) {
					if (nick.equals(req.getSession().getAttribute("current.user.nick"))) {
						req.getRequestDispatcher("/servleti/editBlog").forward(req, resp);
					} else {
						req.getRequestDispatcher("/WEB-INF/pages/error.jsp");
					}
				} else {
					BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(other));
					req.getSession().setAttribute("entry", entry);
					req.setAttribute("comments", entry.getComments());
					req.setAttribute("nick", nick);
					req.getRequestDispatcher("/WEB-INF/pages/comment.jsp").forward(req, resp);
					return;
				}
			}
		}
	}
}