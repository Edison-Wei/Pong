package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	private double x;
	private double y;
	private double velocity = .5;
	private Circle ball;
	
	Ball() { 
		// Create Ball object and place in center of Pane
		ball = new Circle(7.5, Color.WHITE);
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
	
	double getVelocity() {
		return velocity;
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
