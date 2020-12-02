package hr.fer.oprpp1.java.hw05.shell;

/**
 * Shell statuses that commands can return indicating what should
 * the shell do next
 * @author sbolsec
 *
 */
public enum ShellStatus {
	/** Shell should continue and read next command **/
	CONTINUE,
	/** Shell should stop and terminate **/
	TERMINATE
}
