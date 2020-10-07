package hr.fer.zemris.java.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet handles log out request and allows user to register or log in with a
 * different account.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().invalidate();

		System.out.println("Session invalidated!");

		resp.sendRedirect(req.getContextPath() + "/servleti/main");
	}
}