package jp.co.mforce.sol.pictchat.client.ws;

import java.io.IOException;
import java.net.URI;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.google.gson.Gson;

import javafx.scene.paint.Color;
import jp.co.mforce.sol.pictchat.client.model.DrawActions;
import jp.co.mforce.sol.pictchat.client.model.DrawLineModel;

@ClientEndpoint
public class DrawLineWebSocketClient {
	private final String URL = "ws://localhost:8080/pict";
	private Session session;

	@Inject
	Event<DrawLineModel> models;

	// private final LineDrawer drawer;

	public DrawLineWebSocketClient() {
		// drawer = new LineDrawer(canvasPane, this);
	}
	// public DrawLineWebSocketClient(Pane canvasPane) {
	// drawer = new LineDrawer(canvasPane, this);
	// }

	@OnError
	public void onError(Session session, Throwable t) {
		t.printStackTrace(System.err);
	}

	@OnMessage
	public void onMessage(String msg, Session session) {
		Gson gson = new Gson();
		DrawLineModel lineModel = gson.fromJson(msg, DrawLineModel.class);
		models.fire(lineModel);
		
		// drawer.drawLine(lineModel);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println("closed");
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("opened");
	}

	public void connect() {
		connect(URL);
	}

	public void connect(String url) {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			session = container.connectToServer(this, URI.create(url));
		} catch (DeploymentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPointXY(double pointX, double pointY, Color color, DrawActions action) {
		DrawLineModel drawInfo = new DrawLineModel();
		drawInfo.setId(this.session.getId());
		drawInfo.setColor(color);
		drawInfo.setPointX(pointX);
		drawInfo.setPointY(pointY);
		drawInfo.setAction(action);

		Gson gson = new Gson();
		try {
			session.getBasicRemote().sendText(gson.toJson(drawInfo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Session getSession() {
		return this.session;
	}
}
