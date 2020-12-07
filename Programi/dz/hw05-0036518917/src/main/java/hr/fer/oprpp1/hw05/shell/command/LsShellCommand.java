package hr.fer.oprpp1.hw05.shell.command;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.ShellUtil;

/**
 * Command which prints the contents of a directory (not recursive)
 * @author sbolsec
 *
 */
public class LsShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "ls";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("takes a single argument -> a directory and writes a directory listing (not recursive)");
	}
	
	/**
	 * Writes a directory listing for the given directory
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path = ShellUtil.getDirectoryPath(arguments, env);
		if (path == null) 
			path = Path.of(".");
		
		try {
			try (Stream<Path> stream = Files.list(path)) {
				stream.sorted((a,b) -> a.getFileName().compareTo(b.getFileName()))
					  .forEach(s -> printLine(s, env));
			}
		} catch (Exception e) {
			env.writeln("There was an error!");
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
	 * Prints information about the file or directory
	 * @param path path of the current file or directory
	 */
	private void printLine(Path path, Environment env) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributeView faView = Files.getFileAttributeView(
					path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
			);
			BasicFileAttributes attributes = faView.readAttributes();
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			
			long size = Files.size(path);
			
			env.write(Files.isDirectory(path) ? "d" : "-");
			env.write(Files.isReadable(path) ? "r" : "-");
			env.write(Files.isWritable(path) ? "w" : "-");
			env.write(Files.isExecutable(path) ? "x" : "-");
			env.write(String.format(" %10d ", size));
			env.write(formattedDateTime);
			env.writeln(String.format(" %s", path.getFileName()));
		} catch (Exception e) {
			// do nothing
		}
	}
}
