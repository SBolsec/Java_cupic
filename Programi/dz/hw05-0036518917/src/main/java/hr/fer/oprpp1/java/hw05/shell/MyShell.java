package hr.fer.oprpp1.java.hw05.shell;

import hr.fer.oprpp1.java.hw05.shell.command.ShellCommand;

/**
 * Basic shell
 * @author sbolsec
 *
 */
public class MyShell {
	
	/**
	 * Starting point of the shell
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		Environment environment = new EnvironmentImpl();
		ShellStatus status = ShellStatus.CONTINUE;
		
		environment.writeln("Welcome to MyShell v 1.0");
		
		do {
			try {
				environment.write(String.format("%s ", environment.getPromptSymbol()));
				String line = environment.readLine();
			
				String commandName = extractCommandName(line);
				String arguments = extractArguments(line);
				
				ShellCommand command = environment.commands().get(commandName);
				if (command == null) {
					environment.writeln("Command '" + commandName + "' does not exist!");
					environment.writeln("Write 'help' for a list of available commands!");
					status = ShellStatus.CONTINUE;
				} else {
					status = command.executeCommand(environment, arguments);
				}
			} catch (ShellIOException e) {
				status = ShellStatus.TERMINATE;
			}
		} while (status != ShellStatus.TERMINATE);
		
	}
	
	private static String extractCommandName(String line) {
		int firstSpace = line.indexOf(" ");
		
		if (firstSpace != -1)
			return line.substring(0, firstSpace).trim();
		else 
			return line;
	}
	
	private static String extractArguments(String line) {
		int firstSpace = line.indexOf(" ");
		
		if (firstSpace != -1)
			return line.substring(firstSpace + 1).trim();
		else
			return "";
	}
}
