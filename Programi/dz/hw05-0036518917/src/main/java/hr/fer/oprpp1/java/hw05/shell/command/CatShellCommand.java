package hr.fer.oprpp1.java.hw05.shell.command;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.java.hw05.shell.Environment;
import hr.fer.oprpp1.java.hw05.shell.ShellIOException;
import hr.fer.oprpp1.java.hw05.shell.ShellStatus;

/**
 * Command which opens given file and writes its content to console
 * @author sbolsec
 *
 */
public class CatShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "cat";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("takes one or two arguments");
		description.add("the first argument is path to some file and is mandatory");
		description.add("the second argument is charset name that should be used to interpret chars from bytes");
		description.add("if the second argument is not provided, a default platform charset will be used");
		description.add("opens given file and writes its content to console");
	}
	
	/**
	 * Opens given file and writes its content to console
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentParser parser = null;
		try {
			parser = new ArgumentParser(arguments);
		} catch (Exception e) {
			try {
				env.writeln(e.getMessage());
			} catch (ShellIOException ex) {
				// do nothing
			}
			return ShellStatus.CONTINUE;
		}
		String fileName = parser.file;
		String charsetName = parser.charset;
		
		Path path = Path.of(fileName);
		try {
			if (!Files.exists(path))
				env.writeln("Given path does not exist!");
			if (!Files.isReadable(path))
				env.writeln("Given path is not readable!");
		} catch (ShellIOException e) {
			return ShellStatus.CONTINUE;
		}
		
		Charset charset = null;
		if (charsetName.length() == 0) {
			charset = Charset.defaultCharset();
		} else 
			charset = Charset.forName(charsetName);
		
		try {
			List<String> lines = Files.readAllLines(path, charset);
			lines.forEach(s -> env.writeln(s));
		} catch (Exception e) {
			// do nothing
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

	/**
	 * Class that parses the arguments of the cat command
	 * @author sbolsec
	 *
	 */
	private class ArgumentParser {
		/** Stores the value of the first argument **/
		private String file;
		/** Stores the value of the second argument **/
		private String charset;
		
		/**
		 * Constructor which parses the given arguments and stores the parsed values
		 * @param args given arguments
		 * @throws IllegalArgumentException if too many arguments are given
		 */
		ArgumentParser(String args) {
			String[] input = args.split("\\s+");
			
			if (input.length > 2)
				throw new IllegalArgumentException("There are too many arguments!");
			
			file = input[0];
			
			if (input.length == 2) 
				charset = input[1];
			else
				charset = "";
		}
	}
}
