package hr.fer.oprpp1.java.hw05.shell.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.java.hw05.shell.Environment;
import hr.fer.oprpp1.java.hw05.shell.ShellIOException;
import hr.fer.oprpp1.java.hw05.shell.ShellStatus;

/**
 * Command which lists all available commands, or description of
 * a specific command
 * @author sbolsec
 *
 */
public class HelpShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "help";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("if started with no arguments it lists names of all supported commands");
		description.add("if started with single argument, it prints name and the description of selected command");
	}
	
	/**
	 * Prints list of available commands or name and description of one specific command
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments.length() == 0) {
			try {
				env.commands().keySet().forEach(s -> env.writeln(s));
			} catch (ShellIOException e) {
				// do nothing
			}
		} else {
			ShellCommand command = env.commands().get(arguments);
			try {
				if (command == null) {
					env.writeln("There is no command with the name " + arguments + ".");
				} else {
					env.writeln(command.getCommandName());
					command.getCommandDescription().forEach(s -> env.writeln("\t- " + s));
				}
			} catch (ShellIOException e) {
				// do nothing
			}
		}
		
		return ShellStatus.CONTINUE;
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
