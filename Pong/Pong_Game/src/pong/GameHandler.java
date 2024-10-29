package pong;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameHandler implements Runnable{

	private Stage stageEnvironment;
	private Pane gamePane;
	private Pane endScreen;
	private Text[] score; // score[0] == Player, score[1] == Bot
	private Text countDownTimerText;
	private Text winnerLabel;
	
	private Paddle player;	//	Player x = 20 y = 145
	private Paddle bot;		//	Bot x = 460 y = 145
	private Ball ball;
	
	private int botScore;
	private int playerScore;
	
	private final int SLEEP_TIME_MILLISECONDS = 10;
	private double INCREASEBYVECLOCITY = 11.2;
	private boolean ballPassed = false;
	private boolean pauseGame = false;
	private boolean closeGame = false;
	
//	private TimeOut timeOutGame;
//	private Thread timeOutThread;
	
	/**
	 * Constructor to setup the game environment
	 * Creates a Paddles for Player and Bot
	 * Creates a Ball object
	 * @param rootPane a Pane object with connected to the Stage
	 * @param score a Text array containing the points of each side
	 */
	GameHandler(Stage stageEnvironment, Pane gamePane, Text[] score) {
		this.stageEnvironment = stageEnvironment;
		this.gamePane = gamePane;
		this.score = score;
		this.playerScore = 0;
		this.botScore = 2;
		this.ballPassed = false;
		
		
		endScreen = createEndScreen();
		countDownTimerText = createCountDownTimer();
		player = new Paddle(20, 250, Color.BLUE);
		bot = new Paddle(465, 250, Color.RED);
		ball = new Ball(250, 250);
		
		this.gamePane.getChildren().addAll(player.getPaddle(), bot.getPaddle(), ball.getBall(), countDownTimerText, endScreen);
		
		this.gamePane.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouse) {
				player.updatePaddleYLocation(mouse.getY() - 22.5);
			}
			
		});
		
		this.gamePane.setOnKeyPressed(e -> { // fix 
			System.out.println("Someting happening");
			if(e.getCode() == KeyCode.A) {
				System.out.println("Pressed" + e.getCode());
				pauseGame = !pauseGame;
			}
		});
	}

	
	@Override
	public void run() {
		System.out.println("Game Started");
		while(!closeGame) {
			sleepCountDown();
			while(playerScore < 3 && botScore < 3) {
				ball.randomDirection();
				while(!ballPassed) {
					if(pauseGame) {
						
						try {
							Thread.sleep(1000000);
						} catch(InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
					
					double newX = ball.getXCoords() + ball.getXVelocity();
					double newY = ball.getYCoords() + ball.getYVelocity();
					
					// Check if the next position of the ball exceeds the top or bottom borders
					// if true switch the velocity of y to positive or negative 
					if(checkBallNextYPositionIsInBounds(newY)) {
						bounceOffWall();
						// Sets the ball next to the top or bottom border so no clipping occurs
						newY = (ball.getRadius() > newY) ? ball.getRadius() : 500-ball.getRadius();
					}
					
					// This checks if the next position of the ball is going to pass either paddles
					if(player.getXCoords()+player.getWidth() >= newX || newX >= bot.getXCoords()) {
						if(newX > 490) {
							addOnePoint(score[0]);
							ballPassed = true;
						}
						else if(newX < 10) {
							addOnePoint(score[1]);
							ballPassed = true;
						}
						else if(checkBallHitsAPaddle(newX, newY)) {
							bounceOffPaddle();
							ball.increaseVelocity(INCREASEBYVECLOCITY);
						}
					}
					
					// Update the bot y location
					bot.updatePaddleYLocation(ball.getYCoords() - 22.5);
					
					// Take (430 - positionOfBallY)/yVelocity = numberOfSteps //maybe another way
					ball.updateBall(newX, newY);
					
					try {
						Thread.sleep(SLEEP_TIME_MILLISECONDS);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				
				if(playerScore == 3 || botScore == 3) {
					System.out.println("Someone reached 3");
					continue;
				}
				
				// Once a point has been scored
				// sleep the thread for 5 seconds 
				// and recenter the ball location
				ballPassed = false;
				resetBallLocation(); // 1 second sleep
				sleepCountDown(); 	// 4 second sleep with a count down timer 
			}
			
			String winner = (playerScore == 3 ? "You win!!" : "Bot win!!");
			
			winnerLabel.setText(winner);
			endScreen.setTranslateX(0);
			
		}
	}
	
	
	
	/***
	 * When the ball is about to touch a wall 
	 * switch the Y direction to be kept in play
	 */
	private void bounceOffWall() {
		ball.setyVelocity(ball.getYVelocity()*-1);
	}
	
	/***
	 * When the ball is about to a paddle
	 * switch the X direction to be kept in play
	 */
	private void bounceOffPaddle() {
		ball.setxVelocity(ball.getXVelocity()*-1);
	}

	/***
	 * 
	 * @param current
	 */
	private void addOnePoint(Text current) {
		if(ball.getXCoords() < 250) {
			botScore++;
			current.setText("" + botScore);
		}
		else {
			playerScore++;
			current.setText("" + playerScore);
		}
	}
	
	private boolean checkBallNextYPositionIsInBounds(double newY) {
		return (7.5 > newY || newY > 492.5) ? true : false;
	}
	
	/***
	 * 
	 * @param newX
	 * @param newY
	 * @return
	 */
	private boolean checkBallHitsAPaddle(double newX, double newY) {
		double playerXCoords = player.getXCoords();
		double botXCoords = bot.getXCoords();
		double playerYCoords = player.getYCoords();
		double botYCoords = bot.getYCoords();

		
		if(playerXCoords-5 <= newX && playerXCoords+player.getWidth() >= newX) {
			if(playerYCoords <= newY && playerYCoords + 45 >= newY) {
				return true;
			}	
		}
		
		if(botXCoords <= newX && botXCoords+bot.getWidth()+5 >= newX) {
			if(botYCoords <= newY && botYCoords + 45 >= newY) {
				return true;
			}	
		}
		return false;
	}
	
	
	/***
	 * Resets the Ball location to be centered at
	 * x = 250
	 * y = 250
	 * with a 1 second delay  
	 */
	private void resetBallLocation() {
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		ball.centerLocation();
	}
	
	/***
	 * Displays a Count Down Timer from 3, 2, 1 each with a delay of 1 second.
	 * Moves the countDownTimerText out of bounds at 0
	 */
	private void sleepCountDown() {
		int countDownTimer = 3;
		countDownTimerText.setText("" + countDownTimer); 
		countDownTimerText.setTranslateX(235);
		countDownTimerText.setTranslateY(120);
		
		while(countDownTimer > 0) {
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			countDownTimer--;
			countDownTimerText.setText(""+ countDownTimer);
		}
		countDownTimerText.setTranslateX(500);
		countDownTimerText.setTranslateY(500);
	}
	
	private Text createCountDownTimer() {
		Text newText = new Text("3");
		newText.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 50));
		newText.setFill(Color.WHITE);
		newText.setTranslateX(500);
		newText.setTranslateY(500);
		return newText;
	}
	
	/***
	 * Makes a Text object with Font style of "Arial", Weight of MEDIUM
	 * Posture of REGULAR and the size given
	 * @param text a String containing a character phrase
	 * @param size a int that the text size will become
	 * @return an Text object with customized style
	 */
	Text createText(String text, int size) {
		Text newText = new Text(text);
		newText.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, size));
		newText.setFill(Color.WHITE);
		
		return newText;
	}
	
	/***
	 * Creates a Pane object that will be used when the player or bot has won the match
	 * Player can decide to play another match or quit the game.
	 * @return a Pane that contains children of winnerLabel, playAgainLabel, yesbtn, nobtn
	 */
	Pane createEndScreen() {
		Pane endScreen = new Pane();
		endScreen.setStyle("-fx-background-color: Black");
		endScreen.setPrefHeight(500);
		endScreen.setPrefWidth(500);
		endScreen.setTranslateX(500);
		
		winnerLabel = createText("... won", 25);
		winnerLabel.setTranslateX(200);
		winnerLabel.setTranslateY(100);
		Text playAgainLabel = createText("Do you want to play again?", 25);
		playAgainLabel.setTranslateX(100);
		playAgainLabel.setTranslateY(250);
		
		Button yesbtn = new Button("YES");
		yesbtn.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 30));
		yesbtn.setTextFill(Color.WHITE);
		yesbtn.setTranslateX(100);
		yesbtn.setTranslateY(350);
		yesbtn.setStyle("-fx-background-color: Black");
		yesbtn.setUnderline(true);
		Button nobtn = new Button("NO");
		nobtn.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 30));
		nobtn.setTextFill(Color.WHITE);
		nobtn.setTranslateX(300);
		nobtn.setTranslateY(350);
		nobtn.setStyle("-fx-background-color: Black");
		nobtn.setUnderline(true);
		
		yesbtn.setOnAction(e -> {
			playerScore = 0;
			botScore = 0;
			score[0].setText("" + 0);
			score[1].setText("" + 0);
			
			
			endScreen.setTranslateX(500);
			resetBallLocation();
		});
		
		nobtn.setOnAction(e -> {
			Thread.currentThread().interrupt();
			stageEnvironment.close();
		});
		
		endScreen.getChildren().addAll(winnerLabel, playAgainLabel, yesbtn, nobtn);
		
		return endScreen;
	}

}
