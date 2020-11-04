package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * This implementation of the <code>Command</code> interface removes
 * a turtle state from the top of the stack.
 * @author sbolsec
 *
 */
public class PopCommand implements Command {
	
	/**
	 * Removes a turtle state from the top of the stack.
	 * @param ctx context which stores the turtle states
	 * @param painter painter which draws
	 */
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}
}
