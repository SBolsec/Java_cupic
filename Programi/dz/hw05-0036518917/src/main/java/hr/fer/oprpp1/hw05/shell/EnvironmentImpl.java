package hr.fer.oprpp1.hw05.shell;

import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.oprpp1.hw05.shell.command.CatShellCommand;
import hr.fer.oprpp1.hw05.shell.command.CharsetsShellCommand;
import hr.fer.oprpp1.hw05.shell.command.CopyShellCommand;
import hr.fer.oprpp1.hw05.shell.command.ExitShellCommand;
import hr.fer.oprpp1.hw05.shell.command.HelpShellCommand;
import hr.fer.oprpp1.hw05.shell.command.HexdumpShellCommand;
import hr.fer.oprpp1.hw05.shell.command.LsShellCommand;
import hr.fer.oprpp1.hw05.shell.command.MkdirShellCommand;
import hr.fer.oprpp1.hw05.shell.command.ShellCommand;
import hr.fer.oprpp1.hw05.shell.command.SymbolShellCommand;
import hr.fer.oprpp1.hw05.shell.command.TreeShellCommand;

/**
 * Implementation of the <code>Environment</code> interface.
 * @author sbolsec
 *
 */
public class EnvironmentImpl implements Environment {

	/** Multi-line symbol **/
	private Character multilineSymbol;
	/** Prompt symbol **/
	private Character promptSymbol;
	/** More lines symbol **/
	private Character morelinesSymbol;
	/** Stores the supported commands **/
	SortedMap<String, ShellCommand> commands = new TreeMap<>();
	/** Don't touch this, it works! **/
	Scanner sc = new Scanner(System.in);
	
	/**
	 * Default constructor which initializes all the symbols
	 * and supported commands.
	 */
	EnvironmentImpl() {
		multilineSymbol = '|';
		promptSymbol = '>';
		morelinesSymbol = '\\';
		
		commands.put("cat", new CatShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("tree", new TreeShellCommand());
	}
	
	@Override
	public String readLine() throws ShellIOException {
		String line = null;
		
		try {
			line = sc.nextLine().trim();
			
			// If there is a multi-line command
			while (true) {
				if ((line.length() >= 2 && line.charAt(line.length()-1) == getMorelinesSymbol() 
						&& Character.isWhitespace(line.charAt(line.length()-2))) || line.equals(String.format("%s", getMorelinesSymbol()))) {
					write(String.format("%s ", getMultilineSymbol()));
					line = line.substring(0, line.length()-1) + " " + sc.nextLine();
				} else {
					break;
				}
			}
		} catch (Exception e) {
			throw new ShellIOException("Read line failed!");
		}
		
		return line.trim();
	}

	@Override
	public void write(String text) throws ShellIOException {
		System.out.printf("%s", text);
	}

	@Override
	public void writeln(String text) throws ShellIOException {
		System.out.println(text);		
	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return Collections.unmodifiableSortedMap(commands);
	}

	@Override
	public Character getMultilineSymbol() {
		return this.multilineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) {
		if (symbol == null) {
			try {
				this.writeln("Multi line symbol can't be null!");
			} catch (ShellIOException e) {
				// do nothing
			}
		} else {
			this.multilineSymbol = symbol;
		}
	}

	@Override
	public Character getPromptSymbol() {
		return this.promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) {
		if (symbol == null) {
			try {
				this.writeln("Prompt symbol can't be null!");
			} catch (ShellIOException e) {
				// do nothing
			}
		} else {
			this.promptSymbol = symbol;
		}
	}

	@Override
	public Character getMorelinesSymbol() {
		return this.morelinesSymbol;
	}

	@Override
	public void setMorelinesSymbol(Character symbol) {
		if (symbol == null) {
			try {
				this.writeln("More lines symbol can't be null!");
			} catch (ShellIOException e) {
				// do nothing
			}
		} else {
			this.morelinesSymbol = symbol;
		}
	}
}
