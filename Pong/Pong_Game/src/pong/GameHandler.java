package pong;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameHandler extends Thread {

	private Pane rootPane;
	private Text[] score;
	
	private Paddle player;	//	Player x = 20 y = 145
	private Paddle bot;		//	Bot x = 460 y = 145
	private Ball ball;
	
	final private int SLEEP_TIME = 10;
	
	GameHandler() {
		
	}
	
	GameHandler(Pane rootPane, Text[] score) {
		this.rootPane = rootPane;
		this.score = score;
		player = new Paddle(20, 250, Color.BLUE);
		bot = new Paddle(460, 250, Color.RED);
		ball = new Ball();
		
		this.rootPane.getChildren().addAll(player.getPaddle(), bot.getPaddle(), ball.getBall());
		
		this.rootPane.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				player.updatePaddleYLocation(mouse.getY());
			}
			
		});
	}
	
	public void run() {
		while(score[0].getText() != "3" || score[1].getText() != "3") {
			while(20<= ball.getXCoords() && ball.getXCoords() <= 460) {
				ball.updateBall(ball.getVelocity()+ball.getXCoords(), ball.getYCoords());
				
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) {
					// Create a ThreadException class 
					// should extend Exception and be some message on screen that it broke
					e.printStackTrace();
				}
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
