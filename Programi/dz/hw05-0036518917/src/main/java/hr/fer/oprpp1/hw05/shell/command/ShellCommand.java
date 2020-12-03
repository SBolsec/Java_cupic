package hr.fer.oprpp1.hw05.shell.command;

import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Interface that models a shell command
 * @author sbolsec
 *
 */
public interface ShellCommand {
	/**
	 * Executes the command and returns a status so the shell knows
	 * how to continue working.
	 * @param env shell environment
	 * @param arguments command arguments
	 * @return shell status
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns the name of the command.
	 * @return name of the command
	 */
	String getCommandName();
	
	/**
	 * Returns the command description.
	 * @return command description
	 */
	List<String> getCommandDescription();
}
