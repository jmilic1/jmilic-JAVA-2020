package hr.fer.zemris.model;

import java.awt.Color;

public class Line implements Model{
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private Color stroke;
	private int dimX;
	private int dimY;
	private Color bg;
	
	public Line(int startX, int startY, int endX, int endY, Color stroke, int dimX, int dimY, Color bg) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.stroke = stroke;
		this.dimX = dimX;
		this.dimY = dimY;
		this.bg = bg;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public int getEndX() {
		return endX;
	}
	
	public int getEndY() {
		return endY;
	}
	
	public Color getStroke() {
		return stroke;
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
