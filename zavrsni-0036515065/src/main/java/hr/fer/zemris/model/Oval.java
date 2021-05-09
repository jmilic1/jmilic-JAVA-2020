package hr.fer.zemris.model;

import java.awt.Color;

public class Oval implements Model{
	private int centerX;
	private int centerY;
	private int radiusX;
	private int radiusY;
	private Color stroke;
	private Color fill;
	private int dimX;
	private int dimY;
	private Color bg;
	
	public Oval(int centerX, int centerY, int radiusX, int radiusY, Color stroke, Color fill, int dimX, int dimY, Color bg) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radiusX = radiusX;
		this.radiusY = radiusY;
		this.stroke = stroke;
		this.fill = fill;
		this.dimX = dimX;
		this.dimY = dimY;
		this.bg = bg;
	}
	
	public int getCenterX() {
		return centerX;
	}
	
	public int getCenterY() {
		return centerY;
	}
	
	public int getRadiusX() {
		return radiusX;
	}
	
	public int getRadiusY() {
		return radiusY;
	}
	
	public Color getStroke() {
		return stroke;
	}
	
	public Color getFill() {
		return fill;
	}
	
	public Color getBg() {
		return bg;
	}
	
	public int getDimX() {
		return dimX;
	}
	
	public int getDimY() {
		return dimY;
	}
}
