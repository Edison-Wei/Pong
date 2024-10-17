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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootPane = new Pane();
		rootPane.setStyle("-fx-background-color: Black");
		
		// Creates a menu screen
		Pane menu = createMenuScreen();
		menu.setTranslateX(190);
		menu.setTranslateY(150);
		
		rootPane.getChildren().add(menu);
		
		Scene scene = new Scene(rootPane, 500, 500);
//		scene.setFill(Color.BLACK);
		
		primaryStage.setTitle("Pong");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public Pane createMenuScreen() {
		Text title = createText("Pong", 50);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 50));
		
		Button start = new Button("Start");
		start.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 30));
		start.setTextFill(Color.WHITE);
		start.setPrefHeight(50);
		start.setPrefWidth(150);
		start.setTranslateY(125);
		// not show the button background
		
		start.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				gameScreen();
			}
		});
		
		return new Pane(title,start);
	}
	
	
	// Creates the Game environment for Pong to be played on 
	void gameScreen() {
		rootPane.getChildren().remove(0);
		
		VBox scoreBoard = new VBox(5);
		
		Text userText = createText("Player", 20);
		Text botText = createText("Bot", 20);
		Text[] score = {createText("0", 18), createText("0", 18)};
		
		// Change the x coordinates of labelPane, ScorePane, and scoreBoard
		HBox labelPane = new HBox(15, userText, botText);
		HBox scorePane = new HBox(50, score[0], score[1]);
		scorePane.setTranslateX(20);
		scoreBoard.setTranslateX(200);
		scoreBoard.getChildren().addAll(labelPane, scorePane);
		
		Pane gamePane = new Pane(scoreBoard);
		
		rootPane.getChildren().add(gamePane);
		
		game = new GameHandler(rootPane, score);
		Thread t1 = new Thread(game);
		t1.start();
		
		
//		endScreen();
	}
	
	/***
	 * Makes a Text object with .setFont style of
	 * "Arial" , FontWeight.MEDIUM, FontPosture.REGULAR, and size given
	 * @param text an String of the given word
	 * @param size an int on the size of text
	 * @return an Text object with customized style
	 */
	Text createText(String text, int size) {
		Text newText = new Text(text);
		newText.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, size));
		newText.setFill(Color.WHITE);
		
		return newText;
	}
}
