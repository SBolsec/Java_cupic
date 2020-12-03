package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

import hr.fer.oprpp1.hw05.shell.command.ShellCommand;

/**
 * This interface handles the input/output functions of the shell, stores
 * environment variables (i.e. prompt symbol, ...) and stores the supported commands
 * @author sbolsec
 *
 */
public interface Environment {
	/**
	 * Reads a line from the user and trims the input.
	 * @return read user inputed line
	 * @throws ShellIOException if reading was not successful
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes the given string to the shell without a new line at the end.
	 * @param text text to be written
	 * @throws ShellIOException if writing was not successful
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Writes the given string to the shell with a new line at the end.
	 * @param text text to be written
	 * @throws ShellIOException if writing was not successful
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Returns map of supported commands.
	 * @return map of supported commands
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns the multi-line symbol.
	 * @return multi-line symbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets the multi-line symbol.
	 * @param symbol symbol to be set as multi-line symbol
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns the prompt symbol.
	 * @return prompt symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets the prompt symbol.
	 * @param symbol symbol to be set as prompt symbol
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Return the more lines symbol.
	 * @return more lines symbol
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Sets the more lines symbol.
	 * @param symbol symbol to be set as the more lines symbol
	 */
	void setMorelinesSymbol(Character symbol);
}
