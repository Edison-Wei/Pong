package pong;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	int x;
	int y;
	int velocity = 0;
	Circle ball;
	
	Ball() { 
		// Create Ball and place in center of Pane
		ball = new Circle(7.5, Color.WHITE);
		ball.setTranslateX(250);
		ball.setTranslateY(155);
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
	
	int sendToRandomDirection() {
		// Find a way to send the ball in a random direction
		return 0;
	}
	
}
