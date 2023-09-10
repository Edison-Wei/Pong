package pong;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GameHandler {

	Pane rootPane;
	Paddle player;
	Paddle bot;
	Ball ball;
	
	// for async funtion
//	Thread t1;	// Player movement
//	Thread t2;	// Ball movement
//	Thread t3;	// Bot movement - could combine with ball 
	
	GameHandler(Pane rootPane) {
		this.rootPane = rootPane;
		player = new Paddle(20, 250, Color.BLUE);
		bot = new Paddle(460, 250, Color.RED);
		ball = new Ball();
		
		rootPane.getChildren().addAll(player.getPaddle(), bot.getPaddle(), ball.getBall());
		
		rootPane.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				player.updatePaddleYLocation(mouse.getY());
			}
			
		});
	}
	
	void start() {
		t1.s
		while(20<= ball.getXCoords() && ball.getXCoords() <= 460);
	}
//	Player x = 20 y = 145
//	Bot x = 460 y = 145
}
