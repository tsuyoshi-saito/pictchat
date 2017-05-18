package jp.co.mforce.sol.pictchat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.enterprise.event.Observes;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

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
import javafx.stage.Stage;
import jp.co.mforce.sol.pictchat.client.model.DrawLineModel;
import jp.co.mforce.sol.pictchat.client.model.LineDrawer;
import jp.co.mforce.sol.pictchat.client.ws.DrawLineWebSocketClient;

public class PictChatClient extends Application implements Initializable {
	@FXML
	private ChoiceBox<Colors> choiceList;

	@FXML
	private Pane canvasPane;

	private DrawLineWebSocketClient wsClient;
	private PictChatCanvas canvas;

	private static LineDrawer lineDrawer;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void initialize(URL fxml, ResourceBundle resources) {
		ObservableList<Colors> list = FXCollections.observableArrayList(Colors.values());
		choiceList.setItems(FXCollections.observableArrayList());
		choiceList.getItems().addAll(list);
		choiceList.setValue(Colors.BLACK);

		Weld weld = new Weld();
		WeldContainer container = weld.initialize();
		wsClient = container.instance().select(DrawLineWebSocketClient.class).get();
		lineDrawer = new LineDrawer(canvasPane, wsClient);
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
		stage.show();

	}

	public static void update(@Observes DrawLineModel lineModel) {
		lineDrawer.drawLine(lineModel);
	}

	public void choiceColors() {
	}
}

enum Colors {
	RED, GREEN, BLUE, PURPLE, YELLOW, BLACK, ERASER;
}
