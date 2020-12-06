package hr.fer.oprpp1.hw05.shell.command;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Command which writes the available charsets.
 * @author sbolsec
 *
 */
public class CharsetsShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "charsets";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("prints all the available charsets");
	}
	
	/**
	 * Writes the available charsets.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments.length() != 0) {
			env.writeln("Command 'charsets' does not support any arguments!");
			return ShellStatus.CONTINUE;
		}
		
		Charset.availableCharsets().forEach((s, c) -> env.writeln(s));
		
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
