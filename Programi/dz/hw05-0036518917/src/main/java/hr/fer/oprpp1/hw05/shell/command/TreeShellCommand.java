package hr.fer.oprpp1.hw05.shell.command;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellStatus;
import hr.fer.oprpp1.hw05.shell.ShellUtil;

/**
 * Command that creates a tree of all the content inside a directory (recursive)
 * @author sbolsec
 *
 */
public class TreeShellCommand implements ShellCommand {

	/** Name of the command **/
	private static final String COMMAND_NAME = "tree";
	/** Description of the command **/
	private static List<String> description;
	
	static {
		description = new ArrayList<>();
		description.add("this command expects a single argument: a directory name");
		description.add("prints content of directory as a tree");
	}
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Path path = ShellUtil.getDirectoryPath(arguments, env);
		if (path == null) 
			path = Path.of(".");
		
		try {
			Files.walkFileTree(path, new MyFileVisitor(env, path));
		} catch (Exception e) {
			env.writeln("There was an error while walking the file tree!");
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
	 * File visitor that prints all files under a specified directory
	 */
	private class MyFileVisitor extends SimpleFileVisitor<Path> {

		/** Current level **/
		private int level;
		/** Shell environment **/
		private Environment env;
		
		public MyFileVisitor(Environment env, Path root) {
			this.env = env;
			level = 0;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			try {
				String dirName = dir.getName(dir.getNameCount()-1).toString();
				env.write(" ".repeat(level*2) + "");
				env.writeln(dirName);
			} catch (Exception e) {
				
			}
			level++;
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			env.write(" ".repeat(level*2) + "| ");
			env.writeln(file.getName(file.getNameCount()-1).toString());
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			env.write("".repeat(level*2));
			env.writeln(file.toString());
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			
			return FileVisitResult.CONTINUE;
		}
	}
}
