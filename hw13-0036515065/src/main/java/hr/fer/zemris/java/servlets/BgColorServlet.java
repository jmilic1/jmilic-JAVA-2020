package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebServlet which is used for setting the the background color session
 * attribute given through parameters.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/setcolor")
public class BgColorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String color = req.getParameter("color");

		req.getSession().setAttribute("pickedBgCol", color);
		req.getRequestDispatcher("colors.jsp").forward(req, resp);
	}
}
