package pong;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	
	Pane rootPane;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane rootPane = new Pane();
		
		
		
		Scene scene = new Scene(rootPane, 500, 500);
		scene.setFill(Color.BLACK);
		
		primaryStage.setTitle("Pong");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	// Can add later
//	void difficulty() {
//		
//	}

}
