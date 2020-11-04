package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * This implementation of the <code>Command</code> interface allows the
 * effecitve unit length of the current turtle state to be scaled by a factor.
 * @author sbolsec
 *
 */
public class ScaleCommand implements Command {

	/** Factor by which the effective unit length will be scaled **/
	private double factor;
	
	/**
	 * Sets the factor by which to scale the effective unit length of
	 * the current turtle state.
	 * @param factor factor by which the effective unit length of the current turtle state will be scaled
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}
	
	/**
	 * Rotates the current turtle state by the angle given in the constructor.
	 * @param ctx context which stores the turtle states
	 * @param painter painter which draws
	 */
	@Override
	public void execute(Context ctx, Painter painer) {
		TurtleState currentState = ctx.getCurrentState();
		currentState.setUnitLength(currentState.getUnitLength() * factor);
	}
}
