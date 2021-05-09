package hr.fer.zemris.demo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.model.Model;
import hr.fer.zemris.model.Oval;
import hr.fer.zemris.model.Line;

public class Zad1Demo extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextArea writeableArea = new JTextArea();
	private Drawable drawableArea = new Drawable();
	private List<Model> models = new ArrayList<Model>();

	private class Drawable extends JComponent {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {

			models = parse(writeableArea.getText());

			if (models.size() == 0) {
				return;
			}

			drawGraphics(models, (Graphics2D) g.create());
		}

		private void draw() {
			// validate();
			this.repaint();
		}

		private List<Model> parse(String text) {
			String[] lines = text.split("\n");
			List<Model> models = new ArrayList<Model>();

			int dimX = -1;
			int dimY = -1;
			Color bg = null;
			for (String line : lines) {
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
						}
						model = new Oval(centerX, centerY, rx, ry, stroke, fill, dimX, dimY, bg);
						models.add(model);
					}

				}

			}
			return models;
		}

		private Graphics2D drawGraphics(List<Model> models, Graphics2D g2) {

			if (models.size() == 0) {
				return g2;
			}
			Model start = models.get(0);
			if (start instanceof Line) {
				Line line = (Line) start;
				/*
				 * BufferedImage graphicsContext = new BufferedImage(line.getDimX(),
				 * line.getDimY(), BufferedImage.TYPE_INT_RGB); g2 =
				 * graphicsContext.createGraphics();
				 */
				System.out.println(line.getBg());
				g2.setBackground(line.getBg());
			} else {
				Oval oval = (Oval) start;
				/*
				 * BufferedImage graphicsContext = new BufferedImage(oval.getDimX(),
				 * oval.getDimY(), BufferedImage.TYPE_INT_RGB); g2 =
				 * graphicsContext.createGraphics();
				 */
				System.out.println(oval.getBg());
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

			return g2;

		}
	}

	public Zad1Demo() {
		setSize(1000, 600);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		initGUI();

	}

	private void initGUI() {
		setVisible(true);
		setLayout(new BorderLayout());

		Container cp = getContentPane();
		JPanel areas = new JPanel(new GridLayout(2, 1));
		//JSplitPane areas = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		//areas.setDividerLocation(getWidth() / 2);
		cp.add(areas, BorderLayout.CENTER);

		
		areas.add(writeableArea);
		areas.add(drawableArea);

		setToolBar();

	}

	private void setToolBar() {
		JToolBar statusbar = new JToolBar();
		statusbar.setFloatable(true);
		getContentPane().add(statusbar, BorderLayout.PAGE_END);

		// statusbar.setLayout(new BorderLayout());

		JButton button = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		button.setText("Zatvori");
		statusbar.add(button);

		button = new JButton(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				drawableArea.draw();
			}
		});
		button.setText("Nacrtaj");
		statusbar.add(button);

	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			new Zad1Demo().setVisible(true);
		});
	}
}
