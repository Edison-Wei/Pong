package pong;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameHandler extends Thread {

	private Pane rootPane;
	private Text[] score; // score[0] == Player, score[1] == Bot
	private Text countDownTimerText;
	
	private Paddle player;	//	Player x = 20 y = 145
	private Paddle bot;		//	Bot x = 460 y = 145
	private Ball ball;
	
	private int SLEEP_TIME_MILLISECONDS = 10;
	private double INCREASEBYVECLOCITY = .2;
	
	/**
	 * Constructor to setup the game environment
	 * Creates a Paddles for Player and Bot
	 * Creates a Ball object
	 * @param rootPane a Pane object with connected to the Stage
	 * @param score a Text array containing the points of each side
	 */
	GameHandler(Pane rootPane, Text[] score) {
		this.rootPane = rootPane;
		this.score = score;
		countDownTimerText = createCountDownTimer();
		player = new Paddle(20, 250, Color.BLUE);
		bot = new Paddle(465, 250, Color.RED);
		ball = new Ball(250, 250);
		
		this.rootPane.getChildren().addAll(player.getPaddle(), bot.getPaddle(), ball.getBall(), countDownTimerText);
		
		this.rootPane.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				player.updatePaddleYLocation(mouse.getY() - 22.5);// center it
			}
			
		});
	}
	
	public void run() {
		while(bot.getScore() != 3 && player.getScore() != 3) { // Once finished show a game over screen
			// Fix this broken math
			while(!ballPassedBotPaddle(ball.getXCoords(), ball.getYCoords()) && !ballPassedPlayerPaddle(ball.getXCoords(), ball.getYCoords())) {
				double newX = ball.getXCoords() + ball.getXVelocity();
				double newY = ball.getYCoords() + ball.getYVelocity();
				
				// tests if the next movement of the ball exceeds the y borders
				// if true switch the velocity to either positive or negative
				if(checkBallNextPositionInBounds(newY)) {
					bounceOffWall();
					// this sets the ball right at the border so it does not clip any edges
					newY = (ball.getRadius() > newY) ? ball.getRadius() : 500-ball.getRadius();
				}
				
				// This part needs to be fixed when hitting the paddle
				// write a check if the ball were to pass the paddles
				// This checks if the next position of the ball is going to pass either paddles
				if(player.getXCoords()+player.getWidth() >= newX || newX >= bot.getXCoords()) {
					if(ballPassedBotPaddle(newX, newY))
						addOnePoint(score[0]); // add a point to Player score
					else if(ballPassedPlayerPaddle(newX, newY))
						addOnePoint(score[1]); // add a point to Bot score	
					else if(!checkIfBallHitsAPaddle(newX, newY)) {
						bounceOffPaddle();
						ball.increaseVelocity(INCREASEBYVECLOCITY);
					}
				}
				
				// Update bot location
				updateBotLocation();
				ball.updateBall(newX, newY);
				
				try {
					Thread.sleep(SLEEP_TIME_MILLISECONDS);
				} catch (Exception e) {
					// Create a ThreadException class 
					// should extend Exception and be some message on screen that it broke
					e.printStackTrace();
				}
			}
			// Once the ball has passed either paddle
			// Cause the Thread to sleep for 5 seconds
			// reset ball location to center
			resetBallLocation(); // 1 second sleep
			sleepCountDown(); 	// 4 second sleep with a count down timer 
		}
	}
	
	/***
	 * Does a check if the Ball next position is going to pass half the Bot paddle
	 * @param newX an double Ball next X position
	 * @param newY an double Ball next Y position
	 * @return true if the new position of the ball passes half the paddle, false if not
	 */
	private boolean ballPassedBotPaddle(double newX, double newY) {
		if(newX > bot.getXCoords()-7.5)
			if(bot.getYCoords() >= newY || bot.getYCoords() + 45 <= newY)
				return true;
		return false;
	}

	/***
	 * Does a check if the Ball next position is going to pass half the Player paddle
	 * @param newX an double Ball next X position
	 * @param newY an double Ball next Y position
	 * @return true if the new position of the ball passes half the paddle, false if not
	 */
	private boolean ballPassedPlayerPaddle(double newX, double newY) {
		if(newX < player.getXCoords()+7.5)
			if(player.getYCoords() >= newY || player.getYCoords() + 45 <= newY)
				return true;
		return false;
	}
	
	private void bounceOffWall() {
		ball.setyVelocity(ball.getYVelocity()*-1);
	}
	
	private void bounceOffPaddle() {
		ball.setxVelocity(ball.getXVelocity()*-1);
	}

	private void addOnePoint(Text current) {
		if(ball.getXCoords() < 250) {
			bot.increaseScore(); // could combine to one method
			current.setText("" + bot.getScore());
		}
		else {
			player.increaseScore(); // could combine to one method
			current.setText("" + player.getScore());
		}
	}
	
	private boolean checkBallNextPositionInBounds(double newY) {
		return (7.5 > newY || newY > 492.5) ? true : false;
	}
	
	// inclusive
	private boolean checkIfBallHitsAPaddle(double newX, double newY) {
		if((player.getXCoords()+7.5 <= newX && player.getXCoords()+player.getWidth() >= newX)
				|| (bot.getXCoords() <= newX && bot.getXCoords()+7.5 >= newX)) {
			if(player.getYCoords() <= newY && player.getYCoords() + 45 >= newY)
				return true;
			if(bot.getYCoords() <= newY && bot.getYCoords() + 45 >= newY)
				return true;
		}
		return false;
	}
	
	private void updateBotLocation() {
		bot.updatePaddleYLocation(ball.getYCoords() - 22.5);
	}
	
	private boolean resetBallLocation() {
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		ball.centerBallLocation();
		return true;
	}
	
	private boolean sleepCountDown() {
		int countDownTimer = 3;
		countDownTimerText.setText("" + countDownTimer); 
		countDownTimerText.setTranslateX(235);
		countDownTimerText.setTranslateY(120);
		
		while(countDownTimer > 0) {
			try {
				Thread.sleep(1000);
			} catch(Exception e) {
				e.printStackTrace();
				return false;
			}
			countDownTimer--;
			countDownTimerText.setText(""+ countDownTimer);
		}
		countDownTimerText.setTranslateX(500);
		countDownTimerText.setTranslateY(500);
		return true;
	}
	
	private Text createCountDownTimer() {
		Text newText = new Text("3");
		newText.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 50));
		newText.setFill(Color.WHITE);
		newText.setTranslateX(500);
		newText.setTranslateY(500);
		return newText;
	}

}
