package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
	double x;
	double y;
	Color colour;
	
	Paddle(double x, double y, Color colour) {
		this.x = x;
		this.y = y;
		this.colour = colour;
		
		createPaddles();
	}

	private void createPaddles() {
		Rectangle paddle = new Rectangle(20,30);
	}
	
	void updatePaddleLocation(double x, double y) {
		
	}
	
	
}
