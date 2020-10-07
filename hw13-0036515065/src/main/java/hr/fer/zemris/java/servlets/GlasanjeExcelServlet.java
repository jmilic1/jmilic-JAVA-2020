package hr.fer.zemris.java.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.servlets.GlasanjerRezultatiServlet.BandVote;

/**
 * WebServlet which creates an excel document with data of most popular bands.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/glasanje-xls")
public class GlasanjeExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();
		@SuppressWarnings("unchecked")
		List<BandVote> bandVotes = (List<BandVote>) req.getSession().getAttribute("bandVotes");

		HSSFSheet sheet = hwb.createSheet("Voting Results");
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Band name");
		rowhead.createCell(1).setCellValue("Vote Results");

		int i = 1;
		for (BandVote bandVote : bandVotes) {
			HSSFRow rownum = sheet.createRow(i++);
			rownum.createCell(0).setCellValue(bandVote.getBandName());
			rownum.createCell(1).setCellValue(bandVote.getVoteCount());
		}

		resp.setHeader("Content-Disposition", "attachment; filename=results.xls");

		ServletOutputStream fos = resp.getOutputStream();
		hwb.write(fos);
		hwb.close();
	}
}
