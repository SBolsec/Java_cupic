package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * This implementation of the <code>Command</code> interface allows lines
 * to be drawn by the turtle.
 * @author sbolsec
 *
 */
public class DrawCommand implements Command {

	/** Step multiplied by unit length gives the effective unit length to draw **/
	private double step;
	
	/**
	 * Constructor which initializes the step.
	 * @param step step to be initialized
	 */
	public DrawCommand(double step) {
		this.step = step;
	}
	
	/**
	 * Draws the next line with the turtle.
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
		double newX = Math.cos(angle) * length;
		double newY = Math.sin(angle) * length;
		Vector2D toBeAdded = new Vector2D(newX, newY);
		
		current.getPosition().add(toBeAdded);
		double x1 = current.getPosition().getX();
		double y1 = current.getPosition().getY();
		
		painter.drawLine(x0, y0, x1, y1, current.getColor(), 1f);
	}
	
	/**
     * Returns the angle of the vector in radians.
     * @return angle of the vector in radians
     */
    public double getAngle(Vector2D angle) {
        double atan = Math.atan2(angle.getY(), angle.getX());

        if (atan < 0) atan += (2 * Math.PI);
        return atan;
    }
}
