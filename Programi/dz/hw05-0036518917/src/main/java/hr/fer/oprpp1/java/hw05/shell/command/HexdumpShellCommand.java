package hr.fer.oprpp1.java.hw05.shell.command;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.java.hw05.shell.Environment;
import hr.fer.oprpp1.java.hw05.shell.ShellIOException;
import hr.fer.oprpp1.java.hw05.shell.ShellStatus;
import hr.fer.oprpp1.java.hw05.shell.Util;

/**
 * Command that prints a hex-dump of a file
 * @author sbolsec
 *
 */
public class HexdumpShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "hexdump";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
	}
	
	/**
	 * Prints hex-dump of file
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path = Util.getFilePath(arguments, env);
		if (path == null) 
			return ShellStatus.CONTINUE;
		
		byte[] buff = new byte[16];
		byte[] res = new byte[16];
		int used = 0;
		int line = 16;
		try (InputStream is = new BufferedInputStream(Files.newInputStream(path), 16)) {
			while (true) {
				int size = is.read(buff);
				if (size < 0) {
					line += 16;
					printLine(res, line, used, env);
					break;
				}
				int start = used;
				int neded = 16 - used;
				int i;
				for (i = 0; i < Math.min(size, neded); i++) {
					res[start + i] = buff[i];
				}
				int end = i - 1;
				used += i;
				
				if (used == 16) {
					line += 16;
					printLine(res, line, used, env);
					for (i = 0; i < 16; i++) {
						res[i] = 0;
					}
					used = 0;
				}
				
				if (end < size) {
					for (i = 0; i < size - end; i++) {
						res[i] = buff[end + i];
					}
					used += i;
				}
			}
		} catch (IOException e) {
			try {
				env.writeln("There was an error while copying!");
				return ShellStatus.CONTINUE;
			} catch (ShellIOException ex) {
				return ShellStatus.CONTINUE;
			}
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

	private void printLine(byte[] array, int line, int size, Environment env) {
		try {
			byte[] a = BigInteger.valueOf(line).toByteArray();
			String hexLine = hr.fer.oprpp1.hw05.crypto.Util.bytetohex(a);
			env.write(String.format("%s%s: ", "0".repeat(8-(a.length*2)), hexLine));
			
			//env.write("000" + line + ": ");
			String hex = hr.fer.oprpp1.hw05.crypto.Util.bytetohex(array);
			
			int i;
			for (i = 0; i < size; i++) {
				env.write(hex.substring(i, Math.min(i+2, hex.length()-1)));
				if (i == 7) {
					env.write("|");
				} else {
					env.write(" ");
				}
			}
			if (i != 16) {
				for (int j = i; j < 16; j++) {
					env.write("  ");
					if (j == 7) {
						env.write("|");
					} else {
						env.write(" ");
					}
				}
			}
			
			env.write("| ");
			
			for (i = 0; i < size; i++) {
				env.write(String.format("%s", replaceChar(array[i])));
			}
			
			env.writeln("");
			
		} catch (ShellIOException e) {
			// do nothing
		}
	}
	
	private char replaceChar(byte b) {
		if (b < 32 || b > 127) {
			return '.';
		}
		return (char) b;
	}
}
