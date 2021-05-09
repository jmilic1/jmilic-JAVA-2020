package hr.fer.zemris.server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.model.Line;
import hr.fer.zemris.model.Model;
import hr.fer.zemris.model.Oval;

@WebServlet("/drawServlet")
public class ParseServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = (String) req.getParameter("text");
		
		try {
			System.out.println("Ovdje");
			List<Model> models = parse(text);
			
			System.out.println("Nakon parsa	");
			
			drawGraphics(models, resp);
		} catch(Exception ex) {
			req.setAttribute("text", text);

			req.getRequestDispatcher("error.jsp").forward(req, resp);
		}
		
		
	}
	
	
	
	
	private List<Model> parse(String text) {
		String lines[] = text.split("\\r?\\n");
		List<Model> models = new ArrayList<Model>();
		
		System.out.println("U parsu");

		int dimX = -1;
		int dimY = -1;
		Color bg = null;
		
		for (String line : lines) {
			line = line.trim();
			Color stroke = null;
			int startX = -1;
			int startY = -1;
			int endX = -1;
			int endY = -1;
			int centerX = -1;
			int centerY = -1;
			int rx = -1;
			int ry = -1;
			Color fill = null;
			Model model = null;

			if (line.startsWith("#") || line.length() == 0) {
				continue;
			}

			line = line.trim();

			int firstSpace = line.indexOf(" ");
			String command = line.substring(0, firstSpace);
			line = line.substring(firstSpace);
			line = line.trim();
			switch (command) {
			case ("SIZE"):
				String[] attributesValues = line.split(" ");

				for (String attributeValue : attributesValues) {
					if (attributeValue.length() == 0) {
						continue;
					}
					String[] pair = attributeValue.split("=");
					switch (pair[0]) {
					case ("dim"):
						String[] dimensions = pair[1].split(",");
						dimX = Integer.parseInt(dimensions[0]);
						dimY = Integer.parseInt(dimensions[1]);
						break;
					case ("background:rgb"):
						String[] rgb = pair[1].split(",");
						int r = Integer.parseInt(rgb[0]);
						int g = Integer.parseInt(rgb[1]);
						int b = Integer.parseInt(rgb[2]);
						bg = new Color(r, g, b);
						break;
					default:
						throw new RuntimeException();
					}
				}
				break;
			case ("LINE"):
				String[] attributesValuesLine = line.split(" ");

				for (String attributeValue : attributesValuesLine) {
					if (attributeValue.length() == 0) {
						continue;
					}

					String[] pair = attributeValue.split("=");
					switch (pair[0]) {
					case ("start"):
						String[] coords = pair[1].split(",");
						startX = Integer.parseInt(coords[0]);
						startY = Integer.parseInt(coords[1]);
						break;

					case ("end"):
						String[] coordsEnd = pair[1].split(",");
						endX = Integer.parseInt(coordsEnd[0]);
						endY = Integer.parseInt(coordsEnd[1]);
						break;

					case ("stroke:rgb"):
						String[] rgb = pair[1].split(",");
						int r = Integer.parseInt(rgb[0]);
						int g = Integer.parseInt(rgb[1]);
						int b = Integer.parseInt(rgb[2]);
						stroke = new Color(r, g, b);
						break;
					default:
						throw new RuntimeException();
					}
				}
				model = new Line(startX, startY, endX, endY, stroke, dimX, dimY, bg);
				models.add(model);
				break;
			case ("OVAL"):
				String[] attributesValuesOval = line.split(" ");

				for (String attributeValue : attributesValuesOval) {
					if (attributeValue.length() == 0) {
						continue;
					}

					String[] pair = attributeValue.split("=");
					switch (pair[0]) {
					case ("center"):
						String[] coords = pair[1].split(",");
						centerX = Integer.parseInt(coords[0]);
						centerY = Integer.parseInt(coords[1]);
						break;

					case ("rx"):
						rx = Integer.parseInt(pair[1]);
						break;

					case ("ry"):
						ry = Integer.parseInt(pair[1]);
						break;

					case ("stroke:rgb"):
						String[] rgb = pair[1].split(",");
						int r = Integer.parseInt(rgb[0]);
						int g = Integer.parseInt(rgb[1]);
						int b = Integer.parseInt(rgb[2]);
						stroke = new Color(r, g, b);
						break;
					case ("fill:rgb"):
						String[] rgbFill = pair[1].split(",");
						int rFill = Integer.parseInt(rgbFill[0]);
						int gFill = Integer.parseInt(rgbFill[1]);
						int bFill = Integer.parseInt(rgbFill[2]);
						fill = new Color(rFill, gFill, bFill);
						break;
					default:
						throw new RuntimeException();
					}
					model = new Oval(centerX, centerY, rx, ry, stroke, fill, dimX, dimY, bg);
					models.add(model);
				}
				break;
			default:
				throw new RuntimeException();

			}
			
		}
		return models;
	}

	private void drawGraphics(List<Model> models ,HttpServletResponse resp) {

		Graphics2D g2 = null;
		BufferedImage graphicsContext = null;
		
		Model start = models.get(0);
		if (start instanceof Line) {
			Line line = (Line) start;
			graphicsContext = new BufferedImage(line.getDimX(), line.getDimY(),
					BufferedImage.TYPE_INT_RGB);
			g2 = graphicsContext.createGraphics();
			g2.setBackground(line.getBg());
		} else {
			Oval oval = (Oval) start;
			graphicsContext = new BufferedImage(oval.getDimX(), oval.getDimY(),
					BufferedImage.TYPE_INT_RGB);
			g2 = graphicsContext.createGraphics();
			g2.setBackground(oval.getBg());
		}

		for (Model model : models) {
			if (model instanceof Line) {
				Line line = (Line) model;
				g2.setColor(line.getStroke());
				g2.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
			} else {
				Oval oval = (Oval) model;
				g2.setColor(oval.getStroke());
				g2.drawOval(oval.getCenterX(), oval.getCenterY(), oval.getRadiusX(), oval.getRadiusY());
				g2.setColor(oval.getFill());
				
				g2.fillOval(oval.getCenterX(), oval.getCenterY(), oval.getRadiusX(), oval.getRadiusY());
			}
		}

		resp.setContentType("image/png");
		try {
			ImageIO.write(graphicsContext, "PNG", resp.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			resp.sendRedirect("image.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
