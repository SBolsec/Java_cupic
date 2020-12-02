package hr.fer.oprpp1.java.hw05.shell.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.java.hw05.shell.Environment;
import hr.fer.oprpp1.java.hw05.shell.ShellIOException;
import hr.fer.oprpp1.java.hw05.shell.ShellStatus;
import hr.fer.oprpp1.java.hw05.shell.Util;

/**
 * Command that creates directory structure
 * @author sbolsec
 *
 */
public class MkdirShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "mkdir";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("\t- takes a single argument: a directory name");
		description.add("\t- creates the appropriate directory structure");
	}
	
	/**
	 * Creates the directotry structure
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path = Util.getPath(arguments, env);
		
		if (path == null)
			return ShellStatus.CONTINUE;
		
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			try {
				env.writeln("Directory was not succesfully created!");
				return ShellStatus.CONTINUE;
			} catch (ShellIOException ex) {
				return ShellStatus.CONTINUE;
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
