package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * This implementation of the <code>Command</code> interface allows the 
 * color of the current turtle state to be changed.
 * @author sbolsec
 *
 */
public class ColorCommand implements Command {
	
	/** Color to be added to the current turtle state **/
	private Color color;
	
	/**
	 * Sets the color which will be applied to the current turtle state.
	 * @param color color which will be applied to the current turtle state
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}
	
	/**
	 * Changes the color of the current turtle state to the color provided
	 * in the constructor.
	 * @param ctx context which stores the turtle states
	 * @param painter painter which draws
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}
}
