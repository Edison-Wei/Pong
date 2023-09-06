package pong;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	Pane rootPane;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootPane = new Pane();
		rootPane.setStyle("-fx-background-color: Black");
		
		menuScreen();
		
		Scene scene = new Scene(rootPane, 500, 500);
//		scene.setFill(Color.BLACK);
		
		primaryStage.setTitle("Pong");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	// Creates and adds the collection of nodes to the rootPane
	void menuScreen() {
		
		StackPane menu = createMenuScreen();
		menu.setAlignment(Pos.CENTER);
		
		rootPane.getChildren().add(menu);
	}
	
	public StackPane createMenuScreen() {
		Text title = createText("Pong", 15);
		
		Button start = new Button("Start");
		start.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 12));
		start.setTextFill(Color.WHITE);
		// not show the button background
		
		start.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				gameScreen();
			}
		});
		
		return new StackPane(title,start);
	}
	
	
	// Creates the Game environment for Pong to be played on 
	void gameScreen() {
		rootPane.getChildren().remove(0);
		
		VBox scoreBoard = new VBox(5);
		
		Text userText = createText("Player", 12);
		Text botText = createText("Bot", 12);
		Text userScore = createText("0", 12);
		Text botScore = createText("0", 12);
		
		HBox labelPane = new HBox(15, userText, botText);
		HBox scorePane = new HBox(25, userScore, botScore);
		scoreBoard.getChildren().addAll(labelPane, scorePane);
		
		Pane gamePane = new Pane(scoreBoard);
		
		rootPane.getChildren().add(gamePane);
		
		new GameHandler(rootPane);
	}
	
	// Returns a Text object with the given String and font size
	Text createText(String word, int size) {
		Text newText = new Text(word);
		newText.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, size));
		newText.setFill(Color.WHITE);
		
		return newText;
	}
	
	// Can add later
//	void difficulty() {
//		
//	}
	
	public void addchild(Node node) {
		rootPane.getChildren().add(node);
	}

}
