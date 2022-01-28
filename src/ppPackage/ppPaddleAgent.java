package ppPackage;

import static ppPackage.ppSimParams.AgentLagSlide;
import static ppPackage.ppSimParams.TICK;
import static ppPackage.ppSimParams.TSCALE;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

/**
 * This method contains the functionality for the Agent Player position itself
 * to hit ball. It makes the Agent paddle follow the Y position of the ball to
 * position itself to counter the ball.
 * 
 * @author kevinluo, inspired from TA. Some code are from the Assignment 4
 *         handout.
 *
 */

public class ppPaddleAgent extends ppPaddle {

	ppBall myBall;

	/**
	 * 
	 * The constructor for ppPaddleAgent extends ppPaddle. The constructor uses
	 * super() to get the instance variables of the ppPaddle class. A paddle of
	 * GRect for the Agent will be created according to the parameters of the
	 * pppaddleAgent
	 * 
	 * @param X       - position of paddle on the x-position
	 * @param Y       - position of paddle on y-position
	 * @param myTable - instance of ppTable that links this class to ppTable
	 * @myColor - instance of Color to assign color to the Agent's paddle
	 * @param - GProgram is an instance of GraphicsProgram that adds the Agent
	 *          paddle to the display
	 * 
	 * @author kevinluo (The code is inspired from instructions given in Tutorials,
	 *         code from TA)
	 *
	 */

	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		super(X, Y, myTable, myColor, GProgram);
	}

	public void run() {
		int ballSkip = 0;
		int AgentLag = AgentLagSlide.getValue(); // Assigns AgentLag to value assigned in the slider
		double lastX = X; // Initial X position before time interval
		double lastY = Y;// Initial Y position before time interval

		while (true) {
			Vx = (X - lastX) / TICK; // Calculate velocity in X position
			Vy = (Y - lastY) / TICK; // Calculate velocity in Y position
			lastX = X; // Update the lastX to calculate next iteration of speed in X component
			lastY = Y; // Update the lastY to calculate next iteration of speed in Y component

			if (ballSkip++ >= AgentLag) { // update the Y-position of ppPaddleAgent only after a certain number of
											// iteration (AgentLag)
				ballSkip = 0; // reset ballSkip
				// get the ball Y position
				double Y = myBall.getP().getY();
				this.setP(new GPoint(this.getP().getX(), Y)); // move the paddleAgent to Y-position of ball
			}
			GProgram.pause(TICK * TSCALE);
		}
	}

	/**
	 * This method makes a reference to an instance of ppBall. From assignment 4
	 * handout.
	 * 
	 * @param myBall
	 */

	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}

}
