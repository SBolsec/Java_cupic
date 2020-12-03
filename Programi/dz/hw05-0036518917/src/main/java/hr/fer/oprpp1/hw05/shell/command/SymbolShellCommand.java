package hr.fer.oprpp1.hw05.shell.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.lexer.Lexer;
import hr.fer.oprpp1.hw05.shell.lexer.LexerState;
import hr.fer.oprpp1.hw05.shell.lexer.Token;
import hr.fer.oprpp1.hw05.shell.lexer.TokenType;

/**
 * Command that lets user switch shell symbols
 * @author sbolsec
 *
 */
public class SymbolShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "symbol";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("this command  allows the change or printing of various shell symobls");
		description.add("to print prompt symbol use 'symbol PROMPT'");
		description.add("to change the prompt symbol use 'symbol PROMPT [new_symbol]'");
		description.add("to print more lines symbol use 'symbol MORELINES'");
		description.add("to change the more lines symbol use 'symbol MORELINES [new_symbol]'");
		description.add("to print multi-line symbol use 'symbol MULTILINE'");
		description.add("to change the multi-line symbol use 'symbol MULTILINE [new_symbol]'");
	}
	
	/**
	 * Changes or prints one of the shell symbols
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if (arguments == null) {
			try {
				env.writeln("There were no arguments");
				return ShellStatus.CONTINUE;
			} catch (ShellIOException e) {
				return ShellStatus.CONTINUE;
			}
		}
		
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
		
		// i.e. symbol PROMPT
		if (tokens.size() == 2) {
			if (tokens.get(0).getType() != TokenType.STRING || tokens.get(1).getType() != TokenType.EOF) {
				try {
					env.writeln("Arguments invalid! Use 'help symbol' to see usage of this command!");
					return ShellStatus.CONTINUE;
				} catch (ShellIOException e) {
					return ShellStatus.CONTINUE;
				}
			}
			switch (tokens.get(0).getValue()) {
				case "PROMPT":
					try {
						env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
						return ShellStatus.CONTINUE;
					} catch (ShellIOException e) {
						return ShellStatus.CONTINUE;
					}
				case "MORELINES":
					try {
						env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
						return ShellStatus.CONTINUE;
					} catch (ShellIOException e) {
						return ShellStatus.CONTINUE;
					}
				case "MULTILINES":
					try {
						env.writeln("Symbol for MULTILINES is '" + env.getMultilineSymbol() + "'");
						return ShellStatus.CONTINUE;
					} catch (ShellIOException e) {
						return ShellStatus.CONTINUE;
					}
				default:
					try {
						env.writeln("Unsupported argument, it was: " + tokens.get(0).getValue());
						return ShellStatus.CONTINUE;
					} catch (ShellIOException e) {
						return ShellStatus.CONTINUE;
					}
			}
		}
		
		// i.e. symbol PROMPT #
		if (tokens.size() != 3) {
			try {
				env.writeln("Invalid number of arguments!");
				return ShellStatus.CONTINUE;
			} catch (ShellIOException e) {
				return ShellStatus.CONTINUE;
			}
		}
		
		if (tokens.get(0).getType() != TokenType.STRING || tokens.get(1).getType() != TokenType.STRING
				|| tokens.get(2).getType() != TokenType.EOF) {
			try {
				env.writeln("Arguments invalid! Use 'help symbol' to see usage of this command!");
				return ShellStatus.CONTINUE;
			} catch (ShellIOException e) {
				return ShellStatus.CONTINUE;
			}
		}
		
		char old;
		switch (tokens.get(0).getValue()) {
			case "PROMPT":
				old = env.getPromptSymbol();
				env.setPromptSymbol(tokens.get(1).getValue().charAt(0));
				try {
					env.writeln("Symbol for PROMPT changed from '" + old + "' to '" + tokens.get(1).getValue().charAt(0) + "'");
					return ShellStatus.CONTINUE;
				} catch (ShellIOException e) {
					return ShellStatus.CONTINUE;
				}
			case "MORELINES":
				env.setMorelinesSymbol(tokens.get(1).getValue().charAt(0));
				try {
					old = env.getMorelinesSymbol();
					env.writeln("Symbol for PROMPT changed from '" + old + "' to '" + tokens.get(1).getValue().charAt(0) + "'");
					return ShellStatus.CONTINUE;
				} catch (ShellIOException e) {
					return ShellStatus.CONTINUE;
				}
			case "MULTILINES":
				old = env.getMultilineSymbol();
				env.setMultilineSymbol(tokens.get(1).getValue().charAt(0));
				try {
					env.writeln("Symbol for PROMPT changed from '" + old + "' to '" + tokens.get(1).getValue().charAt(0) + "'");
					return ShellStatus.CONTINUE;
				} catch (ShellIOException e) {
					return ShellStatus.CONTINUE;
				}
			default:
				try {
					env.writeln("Unsupported argument, it was: " + tokens.get(0).getValue());
					return ShellStatus.CONTINUE;
				} catch (ShellIOException e) {
					return ShellStatus.CONTINUE;
				}
		}
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
