package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class SkipCommand implements Command {
	
	/** Step multiplied by unit length gives the effective unit length to skip **/
	private double step;
	
	/**
	 * Constructor which initializes the step.
	 * @param step step to be initialized
	 */
	public SkipCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Skips the next line with the turtle. Moves to the next position
	 * without drawing a line.
	 * @param ctx context which stores the turtle states
	 * @param painter painter which draws
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState current = ctx.getCurrentState();
		double x0 = current.getPosition().getX();
		double y0 = current.getPosition().getY();
		
		double length = current.getUnitLength() * step;
		double angle = getAngle(current.getAngle());
		double newX = x0 * Math.cos(angle) * length;
		double newY = y0 * Math.sin(angle) * length;
		Vector2D toBeAdded = new Vector2D(newX, newY);
		
		current.getPosition().add(toBeAdded);
	}
	
	/**
     * Returns the angle of the vector in radians.
     * @return angle of the vector in radians
     */
    public double getAngle(Vector2D angle) {
        double atan = Math.atan2(angle.getX(), angle.getY());

        if (atan < 0) atan += (2 * Math.PI);
        return atan;
    }
}
