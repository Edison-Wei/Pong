package pong;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameHandler extends Thread {

	private Pane rootPane;
	private Text[] score; // score[0] == Player, score[1] == Bot
	
	private Paddle player;	//	Player x = 20 y = 145
	private Paddle bot;		//	Bot x = 460 y = 145
	private Ball ball;
	
	private int SLEEP_TIME = 10;
	
	GameHandler(Pane rootPane, Text[] score) {
		this.rootPane = rootPane;
		this.score = score;
		player = new Paddle(20, 250, Color.BLUE);
		bot = new Paddle(460, 250, Color.RED);
		ball = new Ball(250, 250);
		
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
			while(15<= ball.getXCoords() && ball.getXCoords() <= 465) { // 20 460
				double newX = ball.getXCoords() + ball.getXVelocity();
				double newY = ball.getYCoords() + ball.getYVelocity();
				
				// tests if the ball exceeds the y borders switch the velocity to negative
				if(7.5 > newY || newY > 492.5) {
					ball.setyVelocity(ball.getYVelocity()*-1);
					// sets the ball right at the border so it does not clip any edges
					newY = (7.5 > newY) ? 7.5 : 492.5;
				}
				
				//write a check if the ball were to pass the paddles
				// This checks if the next position of the ball is going to pass either paddles
				if(30+ball.getRadius() >= newX || newX >= 470-ball.getRadius()) {
					if(ballPassedBotPaddle(newX, newY))
						addOnePoint(score[0]); // add a point to Bot score
					if(ballPassedPlayerPaddle(newX, newY))
						addOnePoint(score[1]); // add a point to Player score
					bounceOffPaddle(); // Changes direction of ball velocity
				}
				
//				Boolean atWall = checkIfBallBounceBoorder(newY);
//				checkIfBallBouncePaddle(newX);
				
				ball.updateBall(newX, newY);
				
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) {
					// Create a ThreadException class 
					// should extend Exception and be some message on screen that it broke
					e.printStackTrace();
				}
			}
		}
	}
	
	/***
	 * Does a check if the Ball next position is going to pass the Bot paddle
	 * @param newX an double Ball next X position
	 * @param newY an double Ball next Y position
	 * @return true if the new position of the ball passes the paddle, false if not
	 */
	private boolean ballPassedBotPaddle(double newX, double newY) {
		if(newX >= 470-ball.getRadius())
			if(bot.getYCoords() >= newY || bot.getYCoords() + 45 <= newY)
				return true;
		return false;
	}

	/***
	 * Does a check if the Ball next position is going to pass the Player paddle
	 * @param newX an double Ball next X position
	 * @param newY an double Ball next Y position
	 * @return true if the new position of the ball passes the paddle, false if not
	 */
	private boolean ballPassedPlayerPaddle(double newX, double newY) {
		if(newX <= 30-ball.getRadius())
			if(player.getYCoords() >= newY || player.getYCoords() + 45 <= newY)
				return true;
		return false;
	}
	
	private void bounceOffPaddle() {
		ball.setxVelocity(ball.getXVelocity()*-1);
	}

	private void addOnePoint(Text current) {
		int num = Integer.parseInt(current.getText());
		num =+ 1;
		current.setText("" + num);
	}
	
	private boolean checkBallInBound() {
		return (ball.getYCoords() <= 7.5 || ball.getYCoords() >= 492.5) ? true : false;
	}

}
