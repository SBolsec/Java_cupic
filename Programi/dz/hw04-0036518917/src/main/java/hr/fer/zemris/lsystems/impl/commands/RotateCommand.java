package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * This implementation of the <code>Command</code> interface rotates the
 * direction in which the turtle is facing in the state on top of the stack
 * @author sbolsec
 *
 */
public class RotateCommand implements Command {

	/** Angle by which to rotate the current state **/
	private double angle;
	
	/**
	 * Sets the angle which to add to the direction of the turtle.
	 * @param angle angle to be added to the direction of the turtle
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Rotates the current turtle state by the angle given in the constructor.
	 * @param ctx context which stores the turtle states
	 * @param painter painter which draws
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState currentState = ctx.getCurrentState();
		currentState.setAngle(currentState.getAngle().rotated(angle));
	}
}
