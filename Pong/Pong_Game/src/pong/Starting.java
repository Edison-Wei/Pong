package pong;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Starting extends Application {

	Pane centerPane;
	int currentAngle = 0;
	ArrayList<Node> smileList = new ArrayList<Node>();
	ArrayList<Node> rainbowList = new ArrayList<Node>();

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		BorderPane rootPane = new BorderPane();
		rootPane.setPadding(new Insets(5, 5, 5, 5));

		centerPane = new Pane();

		Rectangle bg = new Rectangle(200, 0, 290, 450);
		bg.setFill(Color.LIGHTBLUE);

		Rectangle ground = new Rectangle(200, 350, 290, 100);
		ground.setFill(Color.DARKGREEN);

		Rectangle trunk = new Rectangle(250, 300, 20, 90);
		trunk.setFill(Color.SADDLEBROWN);

		Ellipse leaves = new Ellipse(260, 280, 35, 50);
		leaves.setFill(Color.rgb(30, 120, 80));
		leaves.setStroke(Color.rgb(30, 120, 80));
		leaves.setStrokeWidth(10);

//This is added Extra to perform Rotation.

//		Rotate rotate = new Rotate(180, 150, 270);
//		trunk.getTransforms().add(rotate);
//		leaves1.getTransforms().add(rotate);

		VBox textElemPane = new VBox(20); // start
		textElemPane.setPadding(new Insets(100, 10, 0, 0));
		textElemPane.setAlignment(Pos.CENTER);

		VBox bgPane = new VBox(5);
		bgPane.setAlignment(Pos.CENTER);

		Label bgtext = new Label("Background");
		bgtext.setFont(Font.font("Serif", FontWeight.MEDIUM, FontPosture.REGULAR, 20));

		HBox cbPane = new HBox(10);
		cbPane.setPadding(new Insets(0, 0, 0, 35));
		CheckBox rainbow = new CheckBox("Rainbow");
		rainbow.setUserData("Rainbow");
		rainbow.selectedProperty().addListener(new createRainbow());

		CheckBox smile = new CheckBox("Smile");
		smile.setUserData("Smile");
		smile.selectedProperty().addListener(new createSmile());

		cbPane.getChildren().addAll(rainbow, smile);
		bgPane.getChildren().addAll(bgtext, cbPane);

		VBox roPane = new VBox(5);
		roPane.setAlignment(Pos.CENTER);

		Label roText = new Label("Rotation");
		roText.setFont(Font.font("Serif", FontWeight.MEDIUM, FontPosture.REGULAR, 20));

		HBox rad1 = new HBox(10);
		rad1.setAlignment(Pos.CENTER);

		RadioButton r1 = new RadioButton("0");
		r1.setUserData("0");
		RadioButton r2 = new RadioButton("90");
		r2.setUserData("90");

		rad1.getChildren().addAll(r1, r2);

		HBox rad2 = new HBox(10);
		rad2.setAlignment(Pos.CENTER);

		RadioButton r3 = new RadioButton("180");
		r3.setUserData("180");
		RadioButton r4 = new RadioButton("270");
		r4.setUserData("270");

		rad2.getChildren().addAll(r3, r4);

		ToggleGroup roRad = new ToggleGroup();
		r1.setToggleGroup(roRad);
		r2.setToggleGroup(roRad);
		r3.setToggleGroup(roRad);
		r4.setToggleGroup(roRad);

		roRad.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> toggle, Toggle oldValue, Toggle newValue) {
				int times = 0;
				while (currentAngle != Integer.parseInt(roRad.getSelectedToggle().getUserData().toString())) {
					times++;
					currentAngle += 90;
					if (currentAngle > 270) {
						currentAngle -= 360;
					}
				}
				Rotate rotate = new Rotate(90 * times, 250, 330);

				trunk.getTransforms().add(rotate);
				leaves.getTransforms().add(rotate);
			}
		});

		roPane.getChildren().addAll(roText, rad1, rad2);

		VBox capPane = new VBox(5);
		capPane.setAlignment(Pos.CENTER);

		Label capText = new Label("Caption");
		capText.setFont(Font.font("Serif", FontWeight.MEDIUM, FontPosture.REGULAR, 20));

		TextField capTextField = new TextField();
		capTextField.setEditable(true);
		capTextField.setPrefSize(200, 15);

		Button capBtn = new Button("Change Text");
		capBtn.setFont(Font.font("Serif", FontWeight.MEDIUM, FontPosture.REGULAR, 15));
		capBtn.setPrefSize(100, 30);

		Text textInput = new Text();
		textInput.setFont(Font.font("Serif", FontWeight.BOLD, FontPosture.REGULAR, 30));
		textInput.setFill(Color.TRANSPARENT);
		textInput.setX(220);
		textInput.setY(400);

		capBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				textInput.setFill(Color.WHITE);
				textInput.setText(capTextField.getText().toString());

			}
		});

		capPane.getChildren().addAll(capText, capTextField, capBtn);

		textElemPane.getChildren().addAll(bgPane, roPane, capPane);
		centerPane.getChildren().addAll(bg, ground, textInput, trunk, leaves);
		centerPane.getChildren().add(textElemPane);

		Button closeBtn = new Button("Close");
		closeBtn.setFont(Font.font("Serif", FontWeight.MEDIUM, FontPosture.REGULAR, 15));
		closeBtn.setAlignment(Pos.CENTER);
		closeBtn.setPadding(new Insets(5, 30, 5, 0));
		closeBtn.setOnAction(e -> {
			primaryStage.close();
		});
		closeBtn.setTranslateX(420);
		closeBtn.setTranslateY(460);

		centerPane.getChildren().add(closeBtn);

		rootPane.setCenter(centerPane);

		Scene scene = new Scene(rootPane, 500, 500);

		primaryStage.setTitle("JavaFxTree");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	class createRainbow implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> selected, Boolean oldValue, Boolean newValue) {

			if (newValue) {
				Color[] colors = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE,
						Color.DARKMAGENTA };
				if (rainbowList.size() != colors.length) {
					int x = 345;
					int y = 175;
					int xradius = 200;
					int yradius = 140;
					int startAngle = 45;
					int endAngle = 90;

					for (Color color : colors) {
						Arc arc = new Arc(x, y, xradius, yradius, startAngle, endAngle);
						arc.setFill(Color.TRANSPARENT);
						arc.setType(ArcType.OPEN);
						arc.setStroke(color);
						arc.setStrokeWidth(5);

						rainbowList.add(arc);
						xradius -= 5;
						yradius -= 5;
						startAngle -= 2;
						endAngle += 4;
					}
				}
				centerPane.getChildren().addAll(rainbowList);
			}
			if (oldValue) {
				centerPane.getChildren().removeAll(rainbowList);
			}
		}
	}

	class createSmile implements ChangeListener<Boolean> {

		@Override
		public void changed(ObservableValue<? extends Boolean> selected, Boolean oldValue, Boolean newValue) {
			if (newValue) {
				if (smileList.size() != 4) {
					Circle head = new Circle(345, 250, 100);
					head.setFill(Color.YELLOW);

					Ellipse eye1 = new Ellipse(300, 215, 10, 20);
					eye1.setFill(Color.BLACK);

					Ellipse eye2 = new Ellipse(390, 215, 10, 20);
					eye2.setFill(Color.BLACK);

					Arc smile = new Arc(345, 250, 80, 80, 180, 180);
					smile.setFill(Color.BLACK);
					smile.setType(ArcType.CHORD);

					smileList.add(0, head);
					smileList.add(1, eye1);
					smileList.add(2, eye2);
					smileList.add(3, smile);
				}

				centerPane.getChildren().addAll(2, smileList);
			}
			if (oldValue) {
				centerPane.getChildren().removeAll(smileList);
			}

		}

	}

}
