package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
	Rectangle paddle;
	// most likely do not need
//	double x; // current x position of paddle
//	double y; // current y position of paddle
	Color colour;
	
	
	Paddle(double x, double y, Color colour) {
		paddle = new Rectangle(15, 45, colour);
		
		paddle.setTranslateX(x);
		paddle.setTranslateY(y);
	}
	
	Rectangle getPaddle() {
		return paddle;
	}

	
	public void updatePaddleYLocation(double y) {
		if(checkValidYPosition(y))
			paddle.setTranslateY(y);
	}
	
	boolean checkValidYPosition(double y) {
		return (0 <= y && y <= 455) ? true : false;
	}
	
	public double getXCoords() {
		return paddle.getTranslateX();
	}
	
	public double getYCoords() {
		return paddle.getTranslateY();
	}
	
	
}
