package ppPackage;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

/**
 * 
 * A class that stores the parameters and constants needed to set up the screen
 * and for the simulation. Parameters are copy-pasted from assignment 3 document
 * wiht some modifications.
 * 
 * @author kevinluo, copy pasted from Assignment 3
 *
 */

public class ppSimParams {
	// toggle button
	public static JToggleButton traceButton; // instance of the JToggleButton
	public static JLabel scoreboard; // instance of the JLabel
	public static JSlider AgentLagSlide; // instance of the JSlider
	public static JSlider tickSlide; // instance of the JSlider

	public static int AgentScore = 0; // Initial Score of agent
	public static int HumanScore = 0; // Initial Score of Human
	public static String PlayerName = "Human";

	// 1. Paramters defined in screen coordinates (pixels, acm coordinates)
	public static final int WIDTH = 1280; // n.b. screen coordinates
	public static final int HEIGHT = 600;
	public static final int OFFSET = 200;

	// 2. Ping-pong table parameters
	public static final double ppTableXlen = 2.74; // Length
	public static final double ppTableHgt = 1.52; // Ceiling
	public static final double XwallL = 0.05; // Position of l wall
	public static final double XwallR = 2.69; // Position of r wall

	// 3. Parameters defined in simulation coordinates
	public static final double g = 9.8; // MKS
	public static final double k = 0.1316; // Vt constant
	public static final double Pi = 3.1416;
	public static final double bSize = 0.02; // pp ball radius
	public static final double bMass = 0.0027; // pp ball mass
	public static final double TICK = 0.01; // Clock tick duration (sec)
	public static final double ETHR = 0.001; // Minimum ball energy

	public static final double Xmin = 0.0; // Minimum value of X (pp table)
	public static final double Xmax = ppTableXlen; // Maximum value of X
	public static final double Ymin = 0.0; // Minimum value of Y
	public static final double Ymax = ppTableHgt; // Maximum value of Y
	public static final int xmin = 0; // Minimum value of x
	public static final int xmax = WIDTH; // Maximum value of x
	public static final int ymin = 0; // Minimum value of y
	public static final int ymax = HEIGHT; // Maximum value of y
	public static final double Xs = (xmax - xmin) / (Xmax - Xmin); // Scale factor X
	public static final double Ys = (ymax - ymin) / (Ymax - Ymin); // Scale factor Y
	public static final double Xinit = XwallL; // Initial ball location (X)
	public static final double Yinit = Ymax / 2; // Initial ball location (Y)
	public static final double PD = 1; // Trace point diameter
	public static double TSCALE = 2000; // Scaling parameter for pause()

	// 4. Paddle Parameters
	static final double ppPaddleH = 8 * 2.54 / 100; // Paddle height
	static final double ppPaddleW = 0.5 * 2.54 / 100; // Paddle width
	static final double ppPaddleXinit = XwallR - ppPaddleW / 2; // Initial Paddle X
	static final double ppPaddleYinit = Yinit; // Initial Paddle Y
	static final double ppPaddleXgain = 2.0; // Vx gain on paddle hit
	static final double ppPaddleYgain = 1.5; // Vy gain on paddle hit
	static final double LPaddleXinit = XwallL - ppPaddleW / 2;
	static final double LPaddleYinit = Yinit;
	static final double LPaddleXGain = 2.0;
	static final double LPaddleYGain = 2.0;

	// 5. Parameters used by the ppSim class
	static final double YinitMAX = 0.75 * Ymax; // Max inital height at 75% of range
	static final double YinitMIN = 0.25 * Ymax; // Min inital height at 25% of range
	static final double EMIN = 0.2; // Minimum loss coefficient
	static final double EMAX = 0.2; // Maximum loss coefficient
	static final double VoMIN = 5.0; // Minimum velocity
	static final double VoMAX = 5.0; // Maximum velocity
	static final double ThetaMIN = 0.0; // Minimum launch angle
	static final double ThetaMAX = 20.0; // Maximum launch angle
	static final long RSEED = 8976232; // Random number gen. seed value

	// 6. Miscellaneous
	public static final boolean DEBUG = false; // Debug msg. and single step if true
	public static final boolean MESG = true; // Enable status messages on console
	public static final int STARTDELAY = 1000; // Delay between setup and start
}