package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	private double xVelocity = -2.5;
	private double yVelocity = .7;
	private Circle ball;
	
	/***
	 * Constructor with (int x, int y)
	 * Creates a new Ball object with the given (x,y) coordinates on a 2D plane
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
	
	void increaseVelocity(double increase) {
		xVelocity = (xVelocity < 0) ? xVelocity+(increase*-1) : xVelocity+increase;
		yVelocity = (yVelocity < 0) ? yVelocity+(increase*-1) : yVelocity+increase;
	}

	void randomDirection() {
		double xVelo = Math.random() * Math.PI/4;
		double yVelo = Math.random() * Math.PI/4;
		
		int quadrant = (int)(Math.random() * 4);
		
		switch(quadrant) {
			case 2:
				xVelocity = (-1) * xVelo;
				yVelocity = yVelo;
				break;
			case 3:
				xVelocity = (-1) * xVelo;
				yVelocity = (-1) * yVelo;
				break;
			case 4:
				xVelocity = xVelo;
				yVelocity = (-1) * yVelo;
				break;
			default:
				xVelocity = xVelo;
				yVelocity = yVelo;
				break;
		}
	}
	
	void centerLocation() {
		ball.setTranslateX(250);
		ball.setTranslateY(250);
	}
}
