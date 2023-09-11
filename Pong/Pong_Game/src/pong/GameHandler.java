package pong;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameHandler extends Thread {

	Pane rootPane;
	Text[] score;
	
	Paddle player;	//	Player x = 20 y = 145
	Paddle bot;		//	Bot x = 460 y = 145
	Ball ball;
	
	// will need one thread to handle the ball and bot movements
//	Thread t1;	// Ball and Bot movement - will be started in Main class 
	
	GameHandler(Pane rootPane, Text[] score) {
		this.rootPane = rootPane;
		this.score = score;
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
	
	public void run() {
		while(score[0].getText() != "3" || score[1].getText() != "3") {
			while(20<= ball.getXCoords() && ball.getXCoords() <= 460) {
				ball.updateBall(ball.velocity+ball.getXCoords(), ball.getYCoords());
				try {
					wait(1000);
				} catch (Exception e) {
					e.printStackTrace();
				} // Depends on difficultly - lower more difficult
				
//				y = nextYCoords();
//				if(checkBallInBound()) // if the ball were to go out of bounds in the y direction switch
				
//				if(hitPlayerPaddle());
//				if(hitBotPaddle());
			}
			
			if(ball.getXCoords() > 460)
				addOnePoint(score[0]);
			else
				addOnePoint(score[1]);
		}
	}

	void addOnePoint(Text current) {
		int num = Integer.parseInt(current.getText()) + 1;
		current.setText("" + num);
	}
	
	boolean checkBallInBound() {
		return (ball.getYCoords() <= 7.5 || ball.getYCoords() >= 492.5) ? true : false;
	}

}
