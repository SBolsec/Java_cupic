package hr.fer.oprpp1.java.hw05.shell.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.java.hw05.shell.Environment;
import hr.fer.oprpp1.java.hw05.shell.ShellStatus;

public class SymbolShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "symbol";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		// TODO Auto-generated method stub
		return null;
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
