package pong;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GameHandler {
	
	Pane rootPane;
	Main menu;
	
	GameHandler(Pane rootPane) {
		this.rootPane = rootPane;
		menu = new Main();
		
		Rectangle box = new Rectangle(20,30);
		
		rootPane.getChildren().add(box);
	}
}
