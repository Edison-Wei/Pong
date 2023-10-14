package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	private double x;
	private double y;
	private double xVelocity = -2.5;
	private double yVelocity = .5;
	private Circle ball;
	
	/***
	 * Constructor with (int x, int y)
	 * Creates a new Ball object with the given (x,y) coordinates on a pane
	 * @param x an int with the coordinates in the x-axis
	 * @param y an int with the coordinates in the y-axis
	 */
	Ball(int x, int y) { 
		// Create Ball object and place in center of Pane
		ball = new Circle(7.5, Color.WHITE);
		updateBall(x, y);
	}
	
	/***
	 * Translates the Circle object to the given (x,y) coordinates
	 * @param x an int with the new x coordinate
	 * @param y an int with the new y coordinate
	 */
	void updateBall(double x, double y) {
		ball.setTranslateX(x);
		ball.setTranslateY(y);
	}
	
	Circle getBall() {
		return ball;
	}
	
	double getXCoords() {
		return ball.getTranslateX();
	}
	
	double getYCoords() {
		return ball.getTranslateY();	
	}
	
	double getRadius() {
		return ball.getRadius();
	}
	
	double getXVelocity() {
		return xVelocity;
	}
	
	double getYVelocity() {
		return yVelocity;
	}
	
	void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	int sendToRandomDirection() {
		// Find a way to send the ball in a random direction
		// either use trig angles to calculate
		// or calculate from 180 degrees and figure the x and y after
		
		
		return 0;
	}
	
	void centerBallLocation() {
		ball.setTranslateX(250);
		ball.setTranslateY(250);
	}
}
