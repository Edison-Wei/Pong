package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle {
	private Rectangle paddle;
	// most likely do not need
//	double x; // current x position of paddle
//	double y; // current y position of paddle
	
	
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
	
	/***
	 * Checks if the location given is within the screen 500 x 500
	 * @param y an double 
	 * @return
	 */
	boolean checkValidYPosition(double y) {
		return (0 <= y && y <= 455) ? true : false;
	}
	
	/***
	 * Get the X coordinate of the paddle
	 * @return an double of the X position the paddle is at currently 
	 */
	public double getXCoords() {
		return paddle.getTranslateX();
	}
	
	/***
	 * Get the Y coordinate of the paddle
	 * @return an double of the Y position the paddle is at currently
	 */
	public double getYCoords() {
		return paddle.getTranslateY();
	}	
}
