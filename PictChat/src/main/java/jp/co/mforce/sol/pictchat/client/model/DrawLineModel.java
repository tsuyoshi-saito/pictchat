package jp.co.mforce.sol.pictchat.client.model;

import javafx.scene.paint.Color;

public class DrawLineModel {

	private String id;
	private Color color;
	private double pointX;
	private double pointY;

	private DrawActions action;

	public String getId() {
		return id;
	}

	public DrawActions getAction() {
		return action;
	}

	public void setAction(DrawActions action) {
		this.action = action;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setPointX(double pointX) {
		this.pointX = pointX;
	}

	public void setPointY(double pointY) {
		this.pointY = pointY;
	}

	public double getPointX() {
		return pointX;
	}

	public double getPointY() {
		return pointY;
	}
}
