package pong;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameHandler {

	Pane rootPane;
	Paddle player;
	Paddle bot;
	
	GameHandler(Pane rootPane) {
		this.rootPane = rootPane;
		player = new Paddle(20, 145, Color.BLUE);
		bot = new Paddle(460, 145, Color.RED);
		
		rootPane.getChildren().addAll(player.getPaddle(), bot.getPaddle());
	}
//	Player x = 20 y = 145
//	Bot x = 460 y = 145
}
