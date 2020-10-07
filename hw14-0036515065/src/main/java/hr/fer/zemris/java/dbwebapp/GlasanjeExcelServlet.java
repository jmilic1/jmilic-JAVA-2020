package hr.fer.zemris.java.dbwebapp;

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

import hr.fer.zemris.java.model.PollOptionEntity;

/**
 * WebServlet which creates an excel document with data of most popular options.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/servleti/glasanje-xls")
public class GlasanjeExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HSSFWorkbook hwb = new HSSFWorkbook();
		@SuppressWarnings("unchecked")
		List<PollOptionEntity> votes = (List<PollOptionEntity>) req.getSession().getAttribute("votes");

		HSSFSheet sheet = hwb.createSheet("Voting Results");
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Poll Option");
		rowhead.createCell(1).setCellValue("Vote Results");

		int i = 1;
		for (PollOptionEntity entity : votes) {
			HSSFRow rownum = sheet.createRow(i++);
			rownum.createCell(0).setCellValue(entity.getTitle());
			rownum.createCell(1).setCellValue(entity.getVotesCount());
		}

		resp.setHeader("Content-Disposition", "attachment; filename=results.xls");

		ServletOutputStream fos = resp.getOutputStream();
		hwb.write(fos);
		hwb.close();
	}
}