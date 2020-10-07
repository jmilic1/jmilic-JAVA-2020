package hr.fer.zemris.java.web.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.crypto.Crypto;
import hr.fer.zemris.java.dao.DAOProvider;
import hr.fer.zemris.java.dao.jpa.JPAEMProvider;
import hr.fer.zemris.java.model.BlogUser;

@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");
		
		BlogUser user = DAOProvider.getDAO().getBlogUserByNick(nick);
		if (user == null) {
			EntityManager em = JPAEMProvider.getEntityManager();
			BlogUser blogAbuser = new BlogUser();
			blogAbuser.setEmail(email);
			blogAbuser.setFirstName(firstName);
			blogAbuser.setLastName(lastName);
			blogAbuser.setNick(nick);
			blogAbuser.setPasswordHash(Crypto.getDigest(password));
			em.persist(blogAbuser);

			req.setAttribute("errorRegister", false);
			req.getRequestDispatcher("/WEB-INF/pages/registerSuccess.jsp").forward(req, resp);
		} else {
			req.setAttribute("errorRegister", true);
			req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
		}	
	}
}