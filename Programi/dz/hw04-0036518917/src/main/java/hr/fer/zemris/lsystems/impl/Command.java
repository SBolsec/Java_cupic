package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * This interface offers a method which can execute a command.
 * @author sbolsec
 *
 */
@FunctionalInterface
public interface Command {
	
	/**
	 * Executes a command with the given arguments.
	 * @param ctx context which stores turtle states
	 * @param painter painter which draws
	 */
	void execute(Context ctx, Painter painter);
}
