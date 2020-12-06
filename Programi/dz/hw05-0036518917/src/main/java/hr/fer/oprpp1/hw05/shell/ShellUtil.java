package hr.fer.oprpp1.hw05.shell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.lexer.Lexer;
import hr.fer.oprpp1.hw05.shell.lexer.LexerState;
import hr.fer.oprpp1.hw05.shell.lexer.Token;
import hr.fer.oprpp1.hw05.shell.lexer.TokenType;

/**
 * Helper functions for shell commands
 * @author sbolsec
 *
 */
public class ShellUtil {
	
	/**
	 * Parses the path if it is the only argument, if there is no input
	 * it return null
	 * @param arguments input arguments
	 * @return path
	 */
	private static String parsePath(String arguments) {
		Lexer lexer = new Lexer(arguments);
		List<Token> tokens = new ArrayList<>();
		String file = null;
		
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
				if (tokens.get(2).getType() != TokenType.EOF) {
					throw new IllegalArgumentException("Too many arguments!");
				}
			}
		} else if (tokens.get(0).getType() == TokenType.STRING) {
			file = tokens.get(0).getValue();
			if (tokens.size() != 2 || tokens.get(1).getType() != TokenType.EOF) {
				throw new IllegalArgumentException("Too many arguments!");
			}
		}
		return file;
	}
	
	/**
	 * Returns path from input arguments, only the path has to be specified in
	 * the arguments.
	 * @param arguments input arguments
	 * @param env shell environment
	 * @return path
	 */
	public static Path getPath(String arguments, Environment env) {
		String name = null;
		try {
			name = parsePath(arguments);
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
		}
		
		if (name == null) {
			return null;
		}
		return Path.of(name);
	}
	
	/**
	 * Returns path from given string or null.
	 * @param name string
	 * @param env shell environment
	 * @return path from string
	 */
	public static Path getPathFromString(String name, Environment env) {
		if (name == null) {
			env.writeln("No path was provided!");
			return null;
		}
		return Path.of(name);
	}
	
	/**
	 * Returns directory from arguments if it is the only argument or null
	 * @param arguments input text
	 * @param env shell environment
	 * @return directory path or null
	 */
	public static Path getDirectoryPath(String arguments, Environment env) {
		Path path = getPath(arguments, env);
		
		if (path == null)
			return null;
		
		if (!Files.exists(path)) {
			env.writeln("Given path does not exist!");
			return null;
		}
		if (!Files.isReadable(path)) {
			env.writeln("Given path is not readable");
			return null;
		}
		if (!Files.isDirectory(path)) {
			env.writeln("Given path is not a directory!");
			return null;
		}
		
		return path;
	}
	
	/**
	 * Returns file from arguments if it is the only argument or null
	 * @param arguments input text
	 * @param env shell environment
	 * @return directory path or null
	 */
	public static Path getFilePath(String arguments, Environment env) {
		Path path = getPath(arguments, env);
		
		if (path == null)
			return null;
		
		if (!Files.exists(path)) {
			env.writeln("Given path does not exist!");
			return null;
		}
		if (!Files.isReadable(path)) {
			env.writeln("Given path is not readable");
			return null;
		}
		if (!Files.isRegularFile(path)) {
			env.writeln("Given path is not a regular file!");
			return null;
		}
		
		return path;
	}
}
