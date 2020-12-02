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
import hr.fer.oprpp1.java.hw05.shell.lexer.Lexer;
import hr.fer.oprpp1.java.hw05.shell.lexer.LexerState;
import hr.fer.oprpp1.java.hw05.shell.lexer.Token;
import hr.fer.oprpp1.java.hw05.shell.lexer.TokenType;

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
			if (!Files.exists(path)) {
				env.writeln("Given path does not exist!");
				return ShellStatus.CONTINUE;
			}
			if (!Files.isReadable(path)) {
				env.writeln("Given path is not readable!");
				return ShellStatus.CONTINUE;
			}
			if (!Files.isRegularFile(path)) {
				env.writeln("Given path is not a regular file!");
				return ShellStatus.CONTINUE;
			}
		} catch (ShellIOException e) {
			return ShellStatus.CONTINUE;
		}
		
		Charset charset = null;
		if (charsetName.length() == 0) {
			charset = Charset.defaultCharset();
		} else {
			try {
				charset = Charset.forName(charsetName);
			} catch (Exception e) {
				env.writeln("Given charset is invalid!");
				return ShellStatus.CONTINUE;
			}
		}
		
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
			Lexer lexer = new Lexer(args);
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
			
			if (tokens.size() == 0)
				throw new IllegalArgumentException("There were no arguments!");
			
			if (tokens.get(0).getType() == TokenType.QUOTE_START) {
				if (tokens.size() >= 2 && tokens.get(1).getType() != TokenType.QUOTE) {
					throw new IllegalArgumentException("There was no file path!");
				} else {
					file = tokens.get(1).getValue();
				}
				if (tokens.size() >= 3) {
					if (tokens.get(2).getType() == TokenType.EOF) {
						charset = "";
						return;
					} else if (tokens.get(2).getType() == TokenType.STRING) {
						charset = "";
						if (tokens.size() != 4)
							throw new IllegalArgumentException("Too many arguments!");
					}
				}
			} else if (tokens.get(0).getType() == TokenType.STRING) {
				file = tokens.get(0).getValue();
				if (tokens.size() == 2) {
					if (tokens.get(1).getType() == TokenType.EOF) {
						charset = "";
						return;
					} else {
						throw new IllegalArgumentException("Exception in parser");
					}
				} else if (tokens.size() == 3) {
					if (tokens.get(1).getType() == TokenType.STRING) {
						charset = tokens.get(1).getValue();
						if (tokens.get(2).getType() == TokenType.EOF) {
							return;
						} else {
							throw new IllegalArgumentException("Exception in parser");
						}
					}
				} else {
					throw new IllegalArgumentException("Invalid number of arguments!");
				}
			} else {
				throw new IllegalArgumentException("There were no arguments");
			}
		}
	}
}
