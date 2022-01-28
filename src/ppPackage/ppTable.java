package ppPackage;

import static ppPackage.ppSimParams.HEIGHT;
import static ppPackage.ppSimParams.OFFSET;
import static ppPackage.ppSimParams.WIDTH;
import static ppPackage.ppSimParams.Xmin;
import static ppPackage.ppSimParams.Xs;
import static ppPackage.ppSimParams.Ymin;
import static ppPackage.ppSimParams.Ys;
import static ppPackage.ppSimParams.ymax;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class ppTable {

	GraphicsProgram GProgram; // instance of GraphicsProgram

	/**
	 * The ppTable constructor creates the floor of the game and adds it to GProgram
	 * (ppSim)
	 * 
	 * @params GProgram - Instance of GraphicsProgram that will be used to display
	 *         the floor
	 * @author kevinluo (with code from Assignment 1, 2 and 3 ), inspired from TA.
	 *         Parts of the code is from previous Assignment Handouts.
	 *
	 */

	public ppTable(GraphicsProgram GProgram) {
		this.GProgram = GProgram;

		drawGroundLine(); // creates the line for floor

	}

	/***
	 * Method to convert from world to screen coordinates. From assignment 3.
	 * 
	 * @param P a point object in world coordinates
	 * @return p the corresponding point object in screen coordinates
	 */

	public GPoint W2S(GPoint P) { // Template from assignment 1 doc, by Prof. Ferrie
		double X = P.getX(); // get X coordinate of point P
		double Y = P.getY(); // get Y coordinate of point P

		double x = (X - Xmin) * Xs; // Transform X component to screen coordinates
		double y = ymax - (Y - Ymin) * Ys; // Transform Y to screen coordinates

		return new GPoint(x, y);
	}

	/***
	 * Method to convert from screen to world coordinates. From assignment 3.
	 * 
	 * @param P a point object in world coordinates
	 * @return p the corresponding point object in screen coordinates
	 */

	public GPoint S2W(GPoint P) {
		double ScrX = P.getX();
		double ScrY = P.getY();
		double WorldX = ScrX / Xs + Xmin; // Transform X component to screen coordinates
		double WorldY = (ymax - ScrY) / Ys + Ymin;// Transform Y to screen coordinates

		return new GPoint(WorldX, WorldY);
	}

	/**
	 * Erase all the objects on the display (except buttons) and draws a new ground
	 * line
	 */
	public void newScreen() { // From assignment 4 handout. Inspired from TA
		GProgram.removeAll();
		drawGroundLine();

	}

	/**
	 * This method creates the visual floor of the game and adds it to GProgram
	 */

	public void drawGroundLine() {
		GRect gPlane = new GRect(0, HEIGHT, WIDTH + OFFSET, 3); // A thick line HEIGHT pixels down from the top
		gPlane.setColor(Color.BLACK);
		gPlane.setFilled(true);
		GProgram.add(gPlane); // add gPlane to GProgram

	}
}