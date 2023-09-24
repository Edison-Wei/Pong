package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	private double x;
	private double y;
	private double xVelocity = -2.5;
	private double yVelocity = .5;
	private Circle ball;
	
	Ball(int x, int y) { 
		// Create Ball object and place in center of Pane
		ball = new Circle(7.5, Color.WHITE);
		updateBall(x, y);
	}
	
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
	
	void resetBallLocation() {
		ball.setTranslateX(250);
		ball.setTranslateY(250);
	}
}
