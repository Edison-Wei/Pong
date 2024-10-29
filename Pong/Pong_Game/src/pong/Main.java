package pong;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	Pane rootPane;
	GameHandler game;
	Stage stageEnvironment;
	
	public void main() {}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stageEnvironment = primaryStage;
		
		rootPane = new Pane();
		rootPane.setStyle("-fx-background-color: Black");
		
		// Creates a menu screen
		Pane menu = createMenuScreen();
		
		rootPane.getChildren().add(menu);
		
		Scene scene = new Scene(rootPane, 500, 500);
		
		primaryStage.setTitle("Pong");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public Pane createMenuScreen() {
		Pane menuScreen = new Pane();
		menuScreen.setTranslateX(165);
		menuScreen.setTranslateY(150);
		Text title = createText("Pong", 50);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		title.setX(20);
		
		Button start = new Button("Start");
		start.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 30));
		start.setTextFill(Color.WHITE);
		start.setPrefHeight(60);
		start.setPrefWidth(150);
		start.setTranslateY(175);
		start.setTranslateX(5);
		start.setStyle("-fx-background-color: Black");
		start.setUnderline(true);
		
		start.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				gameScreen();
			}
		});
		
		menuScreen.getChildren().addAll(title,start);
		
		return menuScreen;
	}
	
	
	/***
	 * Create and run the game environment.
	 */
	void gameScreen() {
		rootPane.getChildren().remove(0);
		
		Pane gamePane = new Pane();
		
		Text[] score = createScoreBoard(gamePane);
		
		rootPane.getChildren().add(gamePane);
		
		game = new GameHandler(stageEnvironment, gamePane, score);
		
		// Has to use a thread to continue with the game
		Thread gameThread = new Thread(game);
		gameThread.start();
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
	 * 
	 * @param gamePane
	 * @return
	 */
	Text[] createScoreBoard(Pane gamePane) {
		VBox scoreBoard = new VBox(5);
		
		Text userText = createText("Player", 20);
		Text botText = createText("Bot", 20);
		Text[] score = {createText("0", 18), createText("0", 18)};
		
		// Change the x coordinates of labelPane, ScorePane, and scoreBoard
		HBox labelPane = new HBox(15, userText, botText);
		HBox scorePane = new HBox(50, score[0], score[1]);
		scorePane.setTranslateX(20);
		scoreBoard.setTranslateX(195);
		scoreBoard.getChildren().addAll(labelPane, scorePane);
		
		gamePane.getChildren().addAll(scoreBoard);
		
		
		return score;
	}
}
