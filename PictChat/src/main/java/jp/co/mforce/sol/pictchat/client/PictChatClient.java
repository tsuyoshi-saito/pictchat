package jp.co.mforce.sol.pictchat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import jp.co.mforce.sol.pictchat.client.ws.DrawLineWebSocketClient;

public class PictChatClient extends Application implements Initializable {
	@FXML
	private ChoiceBox<Colors> choiceList;

	@FXML
	private Pane canvasPane;

	private DrawLineWebSocketClient wsClient;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void initialize(URL fxml, ResourceBundle resources) {
		ObservableList<Colors> list = FXCollections.observableArrayList(Colors.values());
		choiceList.setItems(FXCollections.observableArrayList());
		choiceList.getItems().addAll(list);
		choiceList.setValue(Colors.BLACK);
	}

	@Override
	public void start(Stage stage) throws IOException {

		URL fxml = getClass().getResource(getClass().getSimpleName() + ".fxml");
		FXMLLoader fxmllorder = new FXMLLoader(fxml, null);
		fxmllorder.setController(this);

		Parent root = fxmllorder.load();
		wsClient = new DrawLineWebSocketClient(canvasPane);
		wsClient.connect();

		stage.setTitle("SampleDraw");
		stage.setScene(new Scene(root));
		stage.show();

	}

	public void choiceColors() {
	}
}

enum Colors {
	RED, GREEN, BLUE, PURPLE, YELLOW, BLACK, ERASER;
}
