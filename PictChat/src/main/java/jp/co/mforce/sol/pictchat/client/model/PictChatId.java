package jp.co.mforce.sol.pictchat.client.model;

import javafx.scene.paint.Color;

public class PictChatId {
	private final String id;
	private final Color color;

	public PictChatId(String id, Color color) {
		this.id = id;
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public Color getColor() {
		return color;
	}

	public String getPictId() {
		return id + color.toString();
	}
}
