package hr.fer.zemris.java.dbwebapp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.dao.sql.SQLConnectionProvider;

/**
 * WebServlet which adds one vote to the total number of votes to a single
 * option whose id is given through parameters.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Integer vote = Integer.parseInt(req.getParameter("id").trim());

		Connection connection = SQLConnectionProvider.getConnection();
		try {
			PreparedStatement pst = connection.prepareStatement("SELECT votesCount FROM PollOptions WHERE id=?");
			pst.setLong(1, vote);
			ResultSet rs = pst.executeQuery();
			rs.next();
			long votes = rs.getLong(1);
			pst = connection.prepareStatement("UPDATE PollOptions SET votesCount = ? WHERE id = ?");
			pst.setLong(1, votes + 1);
			pst.setLong(2, vote);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error while updating votes " + e.getMessage());
		}

		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati");
	}
}