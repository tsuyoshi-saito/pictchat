package jp.co.mforce.sol.pictchat.client.model;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import jp.co.mforce.sol.pictchat.client.PictChatCanvas;
import jp.co.mforce.sol.pictchat.client.ws.DrawLineWebSocketClient;

public class LineDrawer {

	private Map<String, PictChatCanvas> canvases = new HashMap<>();
	private final Pane canvasPane;
	private final DrawLineWebSocketClient wsClient;

	public LineDrawer(Pane canvasPane, DrawLineWebSocketClient wsClient) {
		this.canvasPane = canvasPane;
		PictChatCanvas initCanvas = new PictChatCanvas(Color.BLACK, wsClient);
		canvasPane.getChildren().add(initCanvas);
		this.wsClient = wsClient;
	}

	public void drawLine(DrawLineModel lineModel) {
		PictChatId picId = new PictChatId(lineModel.getId(), lineModel.getColor());
		if (!canvases.containsKey(picId.getPictId())) {
			PictChatCanvas canvas = new PictChatCanvas(lineModel.getColor(), wsClient);
			canvases.put(picId.getPictId(), canvas);
			canvasPane.getChildren().add(canvas);
		}
		canvases.get(picId.getPictId()).drawLine(lineModel);
	}
}
