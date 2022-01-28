package ppPackage;

import static ppPackage.ppSimParams.AgentLagSlide;
import static ppPackage.ppSimParams.AgentScore;
import static ppPackage.ppSimParams.EMAX;
import static ppPackage.ppSimParams.EMIN;
import static ppPackage.ppSimParams.HumanScore;
import static ppPackage.ppSimParams.LPaddleXinit;
import static ppPackage.ppSimParams.LPaddleYinit;
import static ppPackage.ppSimParams.OFFSET;
import static ppPackage.ppSimParams.PlayerName;
import static ppPackage.ppSimParams.RSEED;
import static ppPackage.ppSimParams.STARTDELAY;
import static ppPackage.ppSimParams.TSCALE;
import static ppPackage.ppSimParams.ThetaMAX;
import static ppPackage.ppSimParams.ThetaMIN;
import static ppPackage.ppSimParams.VoMAX;
import static ppPackage.ppSimParams.VoMIN;
import static ppPackage.ppSimParams.Xinit;
import static ppPackage.ppSimParams.YinitMAX;
import static ppPackage.ppSimParams.YinitMIN;
import static ppPackage.ppSimParams.ppPaddleXinit;
import static ppPackage.ppSimParams.ppPaddleYinit;
import static ppPackage.ppSimParams.scoreboard;
import static ppPackage.ppSimParams.tickSlide;
import static ppPackage.ppSimParams.traceButton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/**
 * This class manages the items on the screen. It will also track the actions
 * performed by the user.
 * 
 * @author kevinluo, inspired from TA
 *
 */

public class ppSim extends GraphicsProgram {
	ppTable myTable; // instance of ppTable for floor of game
	ppPaddle RPaddle; // instance of ppPaddle for Right Paddle
	ppPaddleAgent LPaddle; // Instance of ppPaddle for Left Paddle
	ppBall myBall; // Instance of ppBall to add it to screen
	RandomGenerator rgen; // instance of RandomGenerator that will be used to randomize ball parameters

	public static void main(String[] args) {
		new ppSim().start(args);
	}

	/**
	 * Initializes the items on the screen that only updates once in the game and
	 * puts the on the screen. This class contains items such as the ServeButton,
	 * Quit Button, Clear Button and the Slider.
	 * 
	 * 
	 * @author kevinluo inspired from TA
	 */

	public void init() {
		// Create buttons
		this.setSize(ppSimParams.WIDTH + OFFSET, ppSimParams.HEIGHT + OFFSET);// Resizing screen to desired size

		// Creating Buttons
		JButton newServeButton = new JButton("New Serve");
		JButton quitBtn = new JButton("Quit");
		JButton clearBtn = new JButton("Clear");
		JButton rlag = new JButton("rlag");
		JButton rtime = new JButton("rtime");

		traceButton = new JToggleButton("Trace"); // Toggle for tracing

		scoreboard = new JLabel("Agent: " + AgentScore + " " + PlayerName + ": " + HumanScore); // Put text to screen

		AgentLagSlide = new JSlider(0, 10, 5); // Slider adjusting AgentLag
		tickSlide = new JSlider(1000, 3000, 2000); //

		// Add buttons, labels and sliders on the bottom of the screen
		add(newServeButton, SOUTH);
		add(quitBtn, SOUTH);
		add(traceButton, SOUTH);
		add(clearBtn, SOUTH);

		// Adjust time slider to screen

		add(new JLabel("+t"), SOUTH);
		add(tickSlide, SOUTH);
		add(new JLabel("-t"), SOUTH);
		add(rtime, SOUTH);

		// Adjust AgentLag slider to screen

		add(new JLabel("-lag"), SOUTH);
		add(AgentLagSlide, SOUTH);
		add(new JLabel("+lag"), SOUTH);
		add(rlag, SOUTH);

		add(scoreboard, NORTH); // adds scoreboard to top of the screen

		// Addding listeners for the mouse and the buttons

		addMouseListeners();
		addActionListeners();

		rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED); // Asssigning seed for RandomGenerator

		myTable = new ppTable(this); // adds the floor to the screen
		pause(1000);
		newGame(); // creates new game

	}

	/**
	 * Creates new ball when the game starts and assigns it to random parameters.
	 * 
	 * @return an instance ppBall
	 */

	ppBall newBall() {
		Color iColor = Color.RED;
		// Randomizing ball parameters
		double iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
		double iLoss = rgen.nextDouble(EMIN, EMAX);
		double iVel = rgen.nextDouble(VoMIN, VoMAX);
		double iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX);
		myBall = new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, myTable, this); // Create ppBall instance and
																						// adds it to the screen
		return myBall;

	}

	/**
	 * Method call that sets the items at the positions found in the beginning of
	 * the game
	 */

	public void newGame() {
		if (myBall != null)
			myBall.kill(); // stop current game in play

		myTable.newScreen(); // resets screen by removing all items
		myBall = newBall();

		RPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, myTable, Color.GREEN, this); // creates and adds paddle
																							// of Human to
																							// screen

		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this); // Creates and adds agent
																							// paddle to screen
		TSCALE = tickSlide.getValue(); // Adjust TSCALE according to slider position

		// Setting the paddles
		LPaddle.attachBall(myBall);
		myBall.setRightPaddle(RPaddle);
		myBall.setLeftPaddle(LPaddle);

		pause(STARTDELAY);

		myBall.start();
		LPaddle.start();
		RPaddle.start();

	}

	/**
	 * Resets/Clears the scoreboard to 0 and 0 for the Agent and the Human,
	 * respectively
	 */

	public void clear() {
		// Resetting scores of both players
		AgentScore = 0;
		HumanScore = 0;
		scoreboard.setText("Agent: " + AgentScore + " " + PlayerName + ": " + HumanScore); // resetting text for
																							// scoreboard
																							// reset
		add(scoreboard, NORTH); // adds scoreboard to top of the display.
	}

	/**
	 * This method tracks the movement of the mouse and makes the paddle follow the
	 * movement of the mouse. From assignent 3 handout
	 * 
	 * @param e reference variable for MouseEvent class
	 */

	public void mouseMoved(MouseEvent e) {
		if (myTable == null || RPaddle == null)
			return;
		GPoint Pm = myTable.S2W(new GPoint(e.getX(), e.getY()));
		double PaddleX = RPaddle.getP().getX();
		double PaddleY = Pm.getY();
		RPaddle.setP(new GPoint(PaddleX, PaddleY));
	}

	/**
	 * This method tracks if the user has clicked any buttons on the screen and
	 * performs the functionality assigned to the button when clicked.
	 */

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("New Serve")) {
			newGame(); // Start a new game when detects clicked button

		}

		else if (command.equals("Clear")) {
			clear(); // Trigger that resets the scoreboard when clear button is clicked

		}

		else if (command.equals("rlag")) {
			AgentLagSlide.setValue(5);
		}

		else if (command.equals("rtime")) {
			tickSlide.setValue(2000);
		}

		else if (command.equals("Quit")) {
			System.exit(0); // Terminates the program

		}

	}

}
