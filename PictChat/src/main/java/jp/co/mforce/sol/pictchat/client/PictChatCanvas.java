package jp.co.mforce.sol.pictchat.client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jp.co.mforce.sol.pictchat.client.PictChatClient.DrawLineWebSocketClient;
import jp.co.mforce.sol.pictchat.client.model.DrawActions;
import jp.co.mforce.sol.pictchat.client.model.DrawLineModel;

public class PictChatCanvas extends Canvas {

	private final Color color;
	private final DrawLineWebSocketClient wsClient;

	public PictChatCanvas(Color color, DrawLineWebSocketClient wsClient) {
		this.color = color;
		this.wsClient = wsClient;
		this.setWidth(400);
		this.setHeight(400);
		this.getGraphicsContext2D().setStroke(color);
		this.setOnMousePressed(this::mousePressed);
		this.setOnMouseDragged(this::mouseDragged);
	}

	private void mousePressed(MouseEvent event) {
		wsClient.sendPointXY(event.getX(), event.getY(), Color.BLACK, DrawActions.MOVE_TO);
	}

	private void mouseDragged(MouseEvent event) {
		wsClient.sendPointXY(event.getX(), event.getY(), Color.BLACK, DrawActions.LINE_TO);
	}

	public void drawLine(DrawLineModel lineModel) {
		GraphicsContext context2d = this.getGraphicsContext2D();
		switch (lineModel.getAction()) {
		case MOVE_TO:
			context2d.moveTo(lineModel.getPointX(), lineModel.getPointY());
			break;
		case LINE_TO:
			context2d.lineTo(lineModel.getPointX(), lineModel.getPointY());
			context2d.stroke();
		default:
			break;
		}
	}

	Color getColor() {
		return this.color;
	}
}