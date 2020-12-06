package hr.fer.oprpp1.hw05.shell.command;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.ShellUtil;
import hr.fer.oprpp1.hw05.shell.lexer.Lexer;
import hr.fer.oprpp1.hw05.shell.lexer.LexerState;
import hr.fer.oprpp1.hw05.shell.lexer.Token;
import hr.fer.oprpp1.hw05.shell.lexer.TokenType;

/**
 * Command that copies a file
 * @author sbolsec
 *
 */
public class CopyShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "copy";
	/** Description of the command **/
	private static List<String> description;
	/** Path to input file **/
	private String input;
	/** Path to output file **/
	private String output;
	
	static {
		description = new ArrayList<>();
		description.add("this command expects two arguments: source file name and destination file name");
		description.add("works only with files");
		description.add("if the second path is a directory, source file will be copyied into that directory with the same name");
	}
	
	/**
	 * Copies a file
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			parsePaths(arguments);
		} catch (Exception e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		Path first = ShellUtil.getPathFromString(input, env);
		Path second = ShellUtil.getPathFromString(output, env);
		
		if (first == null) {
			env.writeln("Input path invalid!");
			return null;
		}
		if (second == null) {
			env.writeln("Output path invalid!");
			return null;
		}
		
		// Check input path
		if (!Files.exists(first)) {
			env.writeln("Source file does not exist!");
			return ShellStatus.CONTINUE;
		}
		if (!Files.isReadable(first)) {
			env.writeln("Source file is not readable!");
			return ShellStatus.CONTINUE;
		}
		if (!Files.isRegularFile(first)) {
			env.writeln("Source file is not a regular file!");
			return ShellStatus.CONTINUE;
		}
		
		// Check output path
		if (Files.exists(second)) {
			if (Files.isDirectory(second)) {
				second = second.resolve(first.getFileName());
			}
			if (Files.isRegularFile(second)) {
				if (!Files.isWritable(second)) {
					env.writeln("Output file already exists and can not be overwritten!");
					return ShellStatus.CONTINUE;
				}
				String response = null;
				do {
					env.write("Do you want to overwrite the existing file? [y/n] > ");
					response = env.readLine();
					if (response.equalsIgnoreCase("n")) {
						env.writeln("Copying canceled!");
						return ShellStatus.CONTINUE;
					}
				} while (!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("n"));
			}
		}
		
		// Actual copying
		byte[] buff = new byte[4096];
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(first), 4096);
				OutputStream os = new BufferedOutputStream(Files.newOutputStream(second), 4096)) {
			while (true) {
				int size = is.read(buff);
				if (size <= 0) {
					os.flush();
					break;
				}
				os.write(buff, 0, size);
				os.flush();
			}
		} catch (IOException e) {
			env.writeln("There was an error while copying!");
			return ShellStatus.CONTINUE;
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
	 * Parses the arguments for two paths
	 * @param arguments input arguments
	 */
	private void parsePaths(String arguments) {
		Lexer lexer = new Lexer(arguments);
		List<Token> tokens = new ArrayList<>();
		
		while (true) {
			Token token = lexer.nextToken();
			tokens.add(token);
			if (token.getType() == TokenType.QUOTE_START) {
				lexer.setState(LexerState.QUOTES);
			} else if (token.getType() == TokenType.QUOTE) {
				lexer.setState(LexerState.BASIC);
			} else if (token.getType() == TokenType.EOF) {
				break;
			}
		}
		
		if (tokens.size() < 3)
			throw new IllegalArgumentException("There were no arguments!");
		
		if (tokens.get(0).getType() == TokenType.QUOTE_START) {
			if (tokens.size() >= 2 && tokens.get(1).getType() != TokenType.QUOTE) {
				throw new IllegalArgumentException("There was no source path!");
			} else {
				input = tokens.get(1).getValue();
			}
			if (tokens.size() >= 3) {
				if (tokens.get(2).getType() == TokenType.QUOTE_START) {
					if (tokens.size() >= 4 && tokens.get(3).getType() != TokenType.QUOTE) {
						throw new IllegalArgumentException("There was no output path!");
					} else {
						output = tokens.get(3).getValue();
						if (tokens.get(4).getType() != TokenType.EOF)
							throw new IllegalArgumentException("Too many arguments!");
					}
				} else if (tokens.get(2).getType() == TokenType.STRING) {
					output = tokens.get(2).getValue();
					if (tokens.get(3).getType() != TokenType.EOF)
						throw new IllegalArgumentException("Too many arguments!");
				} else {
					throw new IllegalArgumentException("Too few arguments!");
				}
			} else {
				throw new IllegalArgumentException("Too few arguments!");
			}
		} else if (tokens.get(0).getType() == TokenType.STRING) {
			input = tokens.get(0).getValue();
			if (tokens.size() >= 3) {
				if (tokens.get(1).getType() == TokenType.QUOTE_START) {
					if (tokens.get(2).getType() != TokenType.QUOTE) {
						throw new IllegalArgumentException("There was no output path!");
					} else {
						output = tokens.get(2).getValue();
						if (tokens.get(3).getType() != TokenType.EOF)
							throw new IllegalArgumentException("Too many arguments!");
					}
				} else if (tokens.get(1).getType() == TokenType.STRING) {
					output = tokens.get(1).getValue();
					if (tokens.get(2).getType() != TokenType.EOF)
						throw new IllegalArgumentException("Too many arguments!");
				} else {
					throw new IllegalArgumentException("Too few arguments!");
				}
			}
		} else {
			throw new IllegalArgumentException("Too few arguments!");
		}
	}
}
