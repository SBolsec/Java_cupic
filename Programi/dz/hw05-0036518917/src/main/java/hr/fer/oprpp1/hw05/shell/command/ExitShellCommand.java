package hr.fer.oprpp1.hw05.shell.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Command which terminates the shell.
 * @author sbolsec
 *
 */
public class ExitShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "exit";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("this command terminates the shell");
	}
	
	/**
	 * Terminates the shell.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(description);
	}

}
