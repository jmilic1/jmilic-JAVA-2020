package hr.fer.zemris.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * WebServlet which creates an excel document with information about powers of
 * integers given through parameters.
 * 
 * @author Jura Milić
 *
 */
@WebServlet("/powers")
public class PowersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String param = req.getParameter("a");

		Integer a = 0;
		try {
			a = Integer.parseInt(param);
		} catch (NumberFormatException ex) {
			req.getRequestDispatcher("errorPowers.jsp").forward(req, resp);
		}

		Integer b = 0;
		param = req.getParameter("b");
		try {
			b = Integer.parseInt(param);
		} catch (NumberFormatException ex) {
			req.getRequestDispatcher("errorPowers.jsp").forward(req, resp);
		}

		Integer n = 1;
		param = req.getParameter("n");
		try {
			n = Integer.parseInt(param);
		} catch (NumberFormatException ex) {
			req.getRequestDispatcher("errorPowers.jsp").forward(req, resp);
		}

		if (a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5) {
			req.getRequestDispatcher("errorPowers.jsp").forward(req, resp);
		}
		
		if (a > b) {
			Integer temp = b;
			b = a;
			a = temp;
		}

		HSSFWorkbook hwb = new HSSFWorkbook();

		for (int i = 1; i <= n; i++) {
			HSSFSheet sheet = hwb.createSheet(i + "-powers");
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Number");
			rowhead.createCell(1).setCellValue(i + "-power");

			for (int j = a; j <= b; j++) {
				HSSFRow rownum = sheet.createRow(j - a + 1);
				rownum.createCell(0).setCellValue(j);
				rownum.createCell(1).setCellValue(Math.pow(j, i));
			}
		}

		resp.setHeader("Content-Disposition", "attachment; filename=powers.xls");

		ServletOutputStream fos = resp.getOutputStream();
		hwb.write(fos);
		hwb.close();
	}
}
