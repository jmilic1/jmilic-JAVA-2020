package hr.fer.zemris.java.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hr.fer.zemris.java.crypto.Crypto;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.model.BlogUser;

/**
 * Servlet which handles logins and listings of blog users on main page.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BlogUser> users = DAOProvider.getDAO().getBlogUsers();
		req.setAttribute("users", users);

		HttpSession session = req.getSession();

		Boolean error = (Boolean) session.getAttribute("errorLogin");
		if (error != null) {
			req.setAttribute("errorLogin", error);
			session.setAttribute("errorLogin", null);
		}

		Object nick = session.getAttribute("nickname");
		if (nick != null) {
			req.setAttribute("nickname", nick);
			session.setAttribute("nickname", null);
		}

		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nick = req.getParameter("nick");
		String pass = req.getParameter("password");
		HttpSession session = req.getSession();

		BlogUser user = null;
		try {
			user = DAOProvider.getDAO().getBlogUserByNick(nick);
		} catch (NoResultException ex) {
			session.setAttribute("errorLogin", true);
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
			return;
		}

		String digest = Crypto.getDigest(pass);
		if (user != null && digest.equals(user.getPasswordHash())) {
			session.setAttribute("errorLogin", null);
			session.setAttribute("current.user.id", user.getId());
			session.setAttribute("current.user.fn", user.getFirstName());
			session.setAttribute("current.user.ln", user.getLastName());
			session.setAttribute("current.user.nick", user.getNick());
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
		} else {
			session.setAttribute("errorLogin", true);
			session.setAttribute("nickname", nick);
			resp.sendRedirect(req.getContextPath() + "/servleti/main");
		}
	}
}