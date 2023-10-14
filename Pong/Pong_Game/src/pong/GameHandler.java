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
	
	private Paddle player;	//	Player x = 20 y = 145
	private Paddle bot;		//	Bot x = 460 y = 145
	private Ball ball;
	
	private int SLEEP_TIME = 10;
	
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
		player = new Paddle(20, 250, Color.BLUE);
		bot = new Paddle(465, 250, Color.RED);
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
			// Fix this broken math 
			while(player.getXCoords()+player.getWidth()<= ball.getXCoords() && ball.getXCoords() <= bot.getXCoords()+5) { // 20 465
				double newX = ball.getXCoords() + ball.getXVelocity();
				double newY = ball.getYCoords() + ball.getYVelocity();
				
				// tests if the ball exceeds the y borders
				// if true switch the velocity to either positive or negative
				if(checkBallInBounds(newY)) {
					ball.setyVelocity(ball.getYVelocity()*-1);
					// this sets the ball right at the border so it does not clip any edges
					newY = (ball.getRadius() > newY) ? ball.getRadius() : 500-ball.getRadius();
				}
				
				
				// This part needs to be fixed when hitting the paddle
				// write a check if the ball were to pass the paddles
				// This checks if the next position of the ball is going to pass either paddles
				if(30+ball.getRadius() >= newX || newX >= 470-ball.getRadius()) {
					if(ballPassedBotPaddle(newX, newY))
						addOnePoint(score[0]); // add a point to Bot score
					else if(ballPassedPlayerPaddle(newX, newY))
						addOnePoint(score[1]); // add a point to Player score
//					if(ball.getXCoords() <= )
						// 
						bounceOffPaddle();
				}
				
				ball.updateBall(newX, newY);
				
				try {
					Thread.sleep(SLEEP_TIME);
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
	
	// ball.getYCoords() <= 7.5 || ball.getYCoords() >= 492.5
	private boolean checkBallInBounds(double newY) {
		return (7.5 > newY || newY > 492.5) ? true : false;
	}
	
	private boolean resetBallLocation() {
		System.out.println("finished");
		try {
			Thread.sleep(1000);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		ball.centerBallLocation();
		return true;
	}
	
	// might use animationtimer to do this instead but will need to extend it and create a subclass for it
	private boolean sleepCountDown() {
		System.out.println("Sleeper");
		int countDownTimer = 3;
		Text countDownTimerText = new Text("3");
		Pane cdt = new Pane(countDownTimerText);
//		countDownTimerText.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15));
//		countDownTimerText.setFill(Color.WHITE);
//		countDownTimerText.setTranslateX(235);
//		countDownTimerText.setTranslateY(235);
		
		rootPane.getChildren().add(cdt);
		
		while(countDownTimer > 0) {
			System.out.println("in");
			try {
				Thread.sleep(1000);
			} catch(Exception e) {
				e.printStackTrace();
//				return false;
			}
			countDownTimerText.setText(""+ countDownTimer);
			countDownTimer--;
		}
		
		rootPane.getChildren().remove(countDownTimerText);
		return true;
	}

}
