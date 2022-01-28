package ppPackage;

import static ppPackage.ppSimParams.AgentScore;
import static ppPackage.ppSimParams.ETHR;
import static ppPackage.ppSimParams.HumanScore;
import static ppPackage.ppSimParams.PD;
import static ppPackage.ppSimParams.Pi;
import static ppPackage.ppSimParams.PlayerName;
import static ppPackage.ppSimParams.TICK;
import static ppPackage.ppSimParams.TSCALE;
import static ppPackage.ppSimParams.VoMAX;
import static ppPackage.ppSimParams.Xs;
import static ppPackage.ppSimParams.Ymax;
import static ppPackage.ppSimParams.Ys;
import static ppPackage.ppSimParams.bMass;
import static ppPackage.ppSimParams.bSize;
import static ppPackage.ppSimParams.g;
import static ppPackage.ppSimParams.k;
import static ppPackage.ppSimParams.ppPaddleW;
import static ppPackage.ppSimParams.ppPaddleXgain;
import static ppPackage.ppSimParams.ppPaddleYgain;
import static ppPackage.ppSimParams.scoreboard;
import static ppPackage.ppSimParams.traceButton;

import java.awt.Color;

import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;

/**
 * 
 * This class contains the code used to create the ball representing the
 * ping-pong ball
 * 
 * 
 * @author kevinluo (with code from Assignment 1 and 2 from the Assignment 3
 *         handout with code from the tutorials)
 *
 */

/**
 * 
 * The ppBall class simulates the ping-pong ball's trajectory and its collision
 * with the paddle and floor. Boundaries are defined in the screen. When the
 * ball reaches the boundary,the game is over and a point is added to the player
 * that scored. Most of the code is taken from Assignment 3.
 * 
 * 
 * @author kevinluo, inspired from TA
 *
 */

public class ppBall extends Thread {

	private double Xinit; // Initial position of ball - X
	private double Yinit; // Initial position of ball - Y
	private double Vo; // Initial velocity (Magnitude)
	private double theta; // Initial direction
	private double loss; // Energy loss on collision
	private Color color; // Color of ball
	private GraphicsProgram GProgram; // Instance of ppSim class (this)
	GOval myBall; // Graphics object representing ball
	ppTable myTable; //
	ppPaddle RPaddle; // Right Paddle representing Player
	ppPaddle LPaddle; // Left Paddle representing the Computer
	double X, Xo, Y, Yo;
	double Vx, Vy; // Velocity in X and Y position
	boolean running;

	/**
	 * The constructor for the ppBall class copies parameters to instance variables,
	 * creates an instance of a GOval to represent the ping-pong ball, and adds it
	 * to the GProgram.
	 * 
	 * @param Xinit    - starting position of the ball X (meters)
	 * @param Yinit    - starting position of the ball Y (meters)
	 * @param Vo       - initial velocity (meters/second)
	 * @param theta    - initial angle to the horizontal (degrees)
	 * @param loss     - loss on collision ([0,1])
	 * @param color    - ball color (Color)
	 * @param GProgram - a reference to the ppSim class used to manage the display
	 * 
	 * @author Copy pasted from Assignment 3. Some code are from Prof. Ferrie.
	 */

	public ppBall(double Xinit, double Yinit, double Vo, double theta, double loss, Color color, ppTable myTable,
			GraphicsProgram GProgram) {
		this.Xinit = Xinit;
		this.Yinit = Yinit;
		this.Vo = Vo;
		this.theta = theta;
		this.loss = loss;
		this.color = color;
		this.GProgram = GProgram; // add it to the GProgram
		this.myBall = new GOval(Xinit * Xs, Yinit * Ys, 2 * bSize * Xs, 2 * bSize * Ys);
		this.myTable = myTable;

		// Create Ball

		GPoint p = myTable.W2S(new GPoint(Xinit, Yinit));
		double ScrX = p.getX();
		double ScrY = p.getY();
		this.myBall = new GOval(ScrX, ScrY, 2 * bSize * Xs, 2 * bSize * Ys);
		this.myBall.setColor(color);
		this.myBall.setFilled(true);
		this.GProgram.add(myBall);
		GProgram.pause(1000);

	}

	public void run() {

		// Copy paste from assg3

		// Initialize simulation parameters

		Xo = Xinit; // Set initial X position and add bSize since ball do not start in wall
		Yo = Yinit; // Set initial Y position
		double time = 0; // Time starts at 0 and counts up
		double Vt = bMass * g / (4 * Pi * bSize * bSize * k); // Terminal velocity
		double Vox = Vo * Math.cos(theta * Pi / 180); // X component of velocity
		double Voy = Vo * Math.sin(theta * Pi / 180);

		// Initialize main simulation loop
		running = true;

		// System.out.printf("\t\t\t Ball Position and Velocity\n");

		while (running) {

			// Template from Sim1, by Prof. Ferrie
			X = Vox * Vt / g * (1 - Math.exp(-g * time / Vt)); // Update relative position
			Y = Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time;
			Vx = Vox * Math.exp(-g * time / Vt); // Update velocity
			Vy = (Voy + Vt) * Math.exp(-g * time / Vt) - Vt;
			double KEx = 0.5 * bMass * Vx * Vx; // Update kinetic energy
			double KEy = 0.5 * bMass * Vy * Vy;
			double PE = bMass * g * (Y + Yo - bSize); // Calculate potential energy from bottom of the ball

			if (Y + Yo > Ymax) {// Check if ball hits the ceiling
				if (Vx > 0) {
					HumanScore++; // add score to human when agent sends ball to ceiling
				} else {
					AgentScore++; // add score to agent when human sends ball to ceiling
				}

				kill(); // stop simulation at next iteration

			}

			// Collision with the floor

			if (Vy < 0 && Yo + Y <= bSize) {

				// Updating energy after collision with floor with energy loss

				KEx = 0.5 * bMass * Vx * Vx * (1 - loss);
				KEy = 0.5 * bMass * Vy * Vy * (1 - loss);
				PE = 0; // No potential energy on ground

				// Update velocity after floor collision

				Vox = Math.sqrt(2 * KEx / bMass);
				Voy = Math.sqrt(2 * KEy / bMass);

				if (Vx < 0)
					Vox = -Vox;

				time = 0; // reset time for next interval
				Xo += X; // need to accumulate distance between collisions
				Yo = bSize; // the absolute position of the ball on the ground
				X = 0; // Reset X and Y for next interval
				Y = 0;

				if ((KEx + KEy + PE) < ETHR)
					kill(); // Indication to step out of loop when ball below energy threshold and collision
							// with ground
			}

			// Collision on right paddle

			if ((Vx > 0) && (Xo + X) >= (RPaddle.getP().getX() - ppPaddleW / 2 - bSize)) {

				// Update energy value when ball collides with right wall
				if (RPaddle.contact(X + Xo, Y + Yo)) {
					KEx = 0.5 * bMass * Vx * Vx * (1 - loss);
					KEy = 0.5 * bMass * Vy * Vy * (1 - loss);
					PE = bMass * g * (Yo + Y - bSize); // Potential energy from bottom of the ball

					// Update velocity after collision with energy loss

					Vox = -Math.sqrt(2 * KEx / bMass);
					Voy = Math.sqrt(2 * KEy / bMass);

					Vox = Vox * ppPaddleXgain; // Add gain in velocity in x position after collision

					// Sets Vox to VoMAX when Vox passes VoMax due to ppPaddleXGain
					if (Math.abs(Vox) > VoMAX) {
						Vox = -VoMAX;
					}
					Voy = Voy * ppPaddleYgain * RPaddle.getSgnVy(); // Add gain in velocity in x position after
																	// collision

					time = 0; // Reset time for next interval
					Xo = RPaddle.getP().getX() - ppPaddleW / 2 - bSize; // Update X and Y positions to expected
					// position
					Yo += Y; // Need to accumulate distance between collisions
					X = 0; // Reset X for next interval
					Y = 0; // Reset Y for next interval
				} else {
					AgentScore++; // Add score to Agent when paddle misses the ball on right side of screen
					kill(); // ends loop at next iteration
				}
			}

			// Collision on left paddle

			if ((Vx < 0) && (Xo + X) <= (LPaddle.getP().getX() + ppPaddleW / 2 + bSize)) {

				// Update energy value when ball collides with right wall
				if (LPaddle.contact(X + Xo, Y + Yo)) {
					KEx = 0.5 * bMass * Vx * Vx * (1 - loss);
					KEy = 0.5 * bMass * Vy * Vy * (1 - loss);
					PE = bMass * g * (Yo + Y - bSize); // Potential energy from bottom of the ball

					// Update velocity after collision with energy loss

					Vox = Math.sqrt(2 * KEx / bMass);
					Voy = Math.sqrt(2 * KEy / bMass);

					Vox = Vox * ppPaddleXgain; // Add gain in velocity in x position after collision

					// Sets Vox to VoMAX when Vox passes VoMax due to ppPaddleXGain
					if (Math.abs(Vox) > VoMAX) {
						Vox = VoMAX;
					}
					Voy = Voy * ppPaddleYgain * LPaddle.getSgnVy(); // Add gain in velocity in x position after
																	// collision

					time = 0; // Reset time for next interval
					Xo = LPaddle.getP().getX() + ppPaddleW / 2 + bSize; // Update X and Y positions to expected
					// position
					Yo += Y; // Need to accumulate distance between collisions
					X = 0; // Reset X for next interval
					Y = 0; // Reset Y for next interval
				} else {
					HumanScore++;// Add score to Human when paddle misses the ball on left side of screen
					kill(); // Ends loop at next iteration

				}
			}

			// System.out.printf("t: %.2f\t\t X: %.2f\t Y: %.2f\t Vx: %.2f\t Vy: %.2f\n",
			// time, X + Xo, Y + Yo, Vx, Vy); // Print
			// out
			// data
			// values

			// Update and display ball

			GPoint p = myTable.W2S(new GPoint(Xo + X - bSize, Yo + Y + bSize)); // Get current position in screen
																				// coordinates
			// Converts X and Y coordinates to screen coordinates
			double ScrX = p.getX();
			double ScrY = p.getY();

			this.myBall.setFilled(true);
			this.myBall.setColor(color);// Set color of ball
			this.myBall.setLocation(ScrX, ScrY); // Update position of ball on screen
			if (traceButton.isSelected()) {
				trace(ScrX, ScrY); // Add ball plot to screen when traceButton is toggled

			}
			time += TICK; // increment time

			// Pause display

			GProgram.pause(TICK * TSCALE);

		}
	}

	/**
	 * Plots the current position of the ball on display
	 * 
	 * @param ScrX The current X position of the ball in screen coordinates
	 * @param ScrY The current Y position of the ball in screen coordinates
	 */

	private void trace(double ScrX, double ScrY) { // Template from assignment 1 doc, by Prof. Ferrie
		GOval dot = new GOval(ScrX + bSize * Xs, ScrY + bSize * Ys, PD, PD); // Creating a plot at ScrX and ScrY and
																				// shifting plot to center of ball
		dot.setFilled(true);
		GProgram.add(dot);
	}

	/**
	 * This method sets the right paddle
	 * 
	 * @param myPaddle - instance of ppPaddle
	 */
	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;
	}

	/**
	 * This method sets the left paddle
	 * 
	 * @param myPaddle - instance of ppPaddle
	 */

	public void setLeftPaddle(ppPaddle myPaddle) {
		this.LPaddle = myPaddle;
	}

	/**
	 * This method gets the velocity of the ball
	 * 
	 * @return - GPoint with the velocity in X and Y for the ball
	 */

	public GPoint getV() {
		return new GPoint(Vx, Vy);
	}

	/**
	 * This method gets the position of the ball
	 * 
	 * @return - GPoint with the X and Y components of the ball
	 */

	public GPoint getP() {
		return new GPoint(X + Xo, Y + Yo);
	}

	/**
	 * This method stops the loop at the next iteration by setting running to false.
	 */

	void kill() {
		scoreboard.setText("Agent: " + AgentScore + " " + PlayerName + ": " + HumanScore); // update scores on
																							// scoreboard
		running = false;
	}

}
