package ppPackage;

import static ppPackage.ppSimParams.TICK;
import static ppPackage.ppSimParams.TSCALE;
import static ppPackage.ppSimParams.Xs;
import static ppPackage.ppSimParams.Ys;
import static ppPackage.ppSimParams.ppPaddleH;
import static ppPackage.ppSimParams.ppPaddleW;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

/**
 * This class contains the code that creates a rectangle, which represents the
 * paddles in the game. The paddle can move in the y-axis, but not the x-axis.
 * This class extends Thread. The code is from Assignment 3 and some parts are
 * from the assignment documents.
 * 
 * @author kevinluo, inspired from TA
 *
 */
public class ppPaddle extends Thread {

	double X; // X position of paddle
	double Y; // Y position of paddle
	double Vx; // Speed in x position
	double Vy; // speed in y position
	private ppTable myTable; // instance of ppTable
	private GRect myPaddle; // instance of GRect that creates the paddle's shape
	public GraphicsProgram GProgram; // Instance of GProgrma that displays the paddle
	private Color myColor; // instance of Color to assign a color to paddle

	/**
	 * 
	 * The constructor for the ppPaddle class copies the parameters to instance
	 * variables, creates an instance of GRect for the paddle, and adds it to the
	 * display
	 * 
	 * @param X       - position of paddle on the x-position
	 * @param Y       - position of paddle on y-position
	 * @param myTable - instance of ppTable
	 * @myColor - instance of Color
	 * @param - GProgram refers to the ppSum class which manages to display
	 * 
	 * @author kevinluo (The code is inspired from instructions given in Tutorials,
	 *         code from TA)
	 *
	 */

	public ppPaddle(double X, double Y, ppTable myTable, Color myColor, GraphicsProgram GProgram) {
		this.X = X;
		this.Y = Y;
		this.myTable = myTable;
		this.GProgram = GProgram;
		this.myColor = myColor;

		// Calculating the left corner in X and Y position to screen coordinates

		double upperLeftX = X - ppPaddleW / 2; // left corner of paddle in X Position
		double upperLeftY = Y + ppPaddleH / 2; // left corner of paddle in Y Position
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		double ScrX = p.getX();
		double ScrY = p.getY();

		// Creates the paddle

		this.myPaddle = new GRect(ScrX, ScrY, ppPaddleW * Xs, ppPaddleH * Ys);
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		GProgram.add(myPaddle); // adds it to GProgram
	}

	public void run() {// From assignment 3
		double lastX = X;
		double lastY = Y;
		while (true) {

			// Updating Vx and Vy position
			Vx = (X - lastX) / TICK;
			Vy = (Y - lastY) / TICK;
			lastX = X; // Value of previous X position
			lastY = Y; // Value of previous Y position
			GProgram.pause(TICK * TSCALE); // Update time
		}
	}

	/**
	 * This method gets the X and Y position of the paddle
	 * 
	 * @return the X and Y position of the paddle
	 */
	public GPoint getP() {// From assignment 3
		return new GPoint(X, Y);
	}

	/**
	 * Sets the paddle to position X & position Y and moves the paddle to the
	 * position X & position Y
	 * 
	 * @param P Reference variable for the GPoint class
	 */

	public void setP(GPoint P) {// From assignment 3
		// Update instance variables
		this.X = P.getX();
		this.Y = P.getY();

		double upperLeftX = X - ppPaddleW / 2; // left corner of the paddle in the X position
		double upperLeftY = Y + ppPaddleH / 2; // left corner of the paddle in the Y position
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		double ScrX = p.getX(); // Convert X to screen coordinates of X
		double ScrY = p.getY(); // Convert Y to screen coordinates of Y

		this.myPaddle.setLocation(ScrX, ScrY); // Move the paddle to a new position
	}

	/**
	 * This method gets the velocity of the paddle in the Vx and Vy
	 * 
	 * @return the the Velocity in the X component and Y component in a GPoint
	 */

	public GPoint getV() {// From assignment 3
		// Return Vx and Vy inside GPoint
		return new GPoint(Vx, Vy);
	}

	/**
	 * This method evaluates the sign of the Vy component
	 * 
	 * @return 1 if Vy is positive and -1 if Vy is negative
	 */

	public double getSgnVy() { // From assignment 3
		// Return -1 if Vy<0 and 1 if Vy>=0
		if (Vy >= 0)
			return 1;
		else
			return -1;
	}

	/**
	 * This method detects if there is contact on the paddle. From assignment 3.
	 * 
	 * @param Sx X position of the surface of the paddle
	 * @param Sy Y position of the surface of the paddle
	 * @return true if a surface at (Sx, Sy) is in contact with the paddle
	 */

	public boolean contact(double Sx, double Sy) {
		return (Sy >= Y - ppPaddleH / 2) && (Sy <= Y + ppPaddleH / 2);
	}
}
