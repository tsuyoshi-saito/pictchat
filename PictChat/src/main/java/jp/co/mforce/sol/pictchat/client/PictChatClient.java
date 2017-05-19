package jp.co.mforce.sol.pictchat.client;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.event.Observes;
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

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import com.google.gson.Gson;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jp.co.mforce.sol.pictchat.client.model.DrawActions;
import jp.co.mforce.sol.pictchat.client.model.DrawLineModel;
import jp.co.mforce.sol.pictchat.client.model.PictChatId;

public class PictChatClient extends Application implements Initializable {
	@FXML
	private ChoiceBox<Colors> choiceList;

	@FXML
	private Pane canvasPane;

	private DrawLineWebSocketClient wsClient;

	private Map<String, PictChatCanvas> canvases = new HashMap<>();

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void initialize(URL fxml, ResourceBundle resources) {
		ObservableList<Colors> list = FXCollections.observableArrayList(Colors.values());
		choiceList.setItems(FXCollections.observableArrayList());
		choiceList.getItems().addAll(list);
		choiceList.setValue(Colors.BLACK);

		wsClient = new DrawLineWebSocketClient();
		wsClient.connect();

	}

	@Override
	public void start(Stage stage) throws IOException {

		URL fxml = getClass().getResource(getClass().getSimpleName() + ".fxml");
		FXMLLoader fxmllorder = new FXMLLoader(fxml, null);
		fxmllorder.setController(this);

		Parent root = fxmllorder.load();

		stage.setTitle("SampleDraw");
		stage.setScene(new Scene(root));
		
		PictChatCanvas initCanvas = new PictChatCanvas(Color.BLACK, wsClient);
		canvasPane.getChildren().add(initCanvas);

		stage.show();

	}

	public void choiceColors() {
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

	@ClientEndpoint
	public class DrawLineWebSocketClient {
		private final String URL = "ws://localhost:8080/pict";
		private Session session;

		public DrawLineWebSocketClient() {
		}

		@OnError
		public void onError(Session session, Throwable t) {
			t.printStackTrace(System.err);
		}

		@OnMessage
		public void onMessage(String msg, Session session) {
			Gson gson = new Gson();
			DrawLineModel lineModel = gson.fromJson(msg, DrawLineModel.class);
			drawLine(lineModel);
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

}

enum Colors {
	RED, GREEN, BLUE, PURPLE, YELLOW, BLACK, ERASER;
}
