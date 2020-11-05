package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.OptionalInt;
import java.util.Scanner;

/**
 * This class provides the user with a interface to interact with the database
 * and make queries.
 * @author sbolsec
 *
 */
public class StudentDB {
	
	/** Stores the student records **/
	private static StudentDatabase db;
	/** Parses the query **/
	private static QueryParser qp;
	/** Stores the filtered student records **/
	private static List<StudentRecord> filteredRecords;
	
	/**
	 * Initializes the database and starts the interface.
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(
					Paths.get("./database.txt"),
					StandardCharsets.UTF_8
			);
		} catch(IOException e) {
			System.err.println("Could not read file!");
			System.exit(1);
		}
		
		db = new StudentDatabase(lines);
		
		startInterface();
	}
	
	/**
	 * Provides the user with a interface. Takes user input, sanitizes it
	 * and then starts a query of stops the program.
	 */
	public static void startInterface() {
		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.format("> ");
				
				// Wait for user input
				String command = sc.nextLine();
				
				// If input is 'exit' terminate the program
				if (command.toLowerCase().equals("exit")) {
					System.out.println("Goodbye!");
					break;
				}
				
				// If command is does not start with query
				if (!command.trim().startsWith("query")) {
					System.out.println("Command has to start either with the keyword 'query' or 'exit'!");
					continue;
				}
				
				// The parser requires the 'query' keyword to be removed
				String query = command.trim().substring(6, command.length());
				
				// If there is an error while parsing, show a message and continue to next command
				try {
					qp = new QueryParser(query);
				} catch (Exception ex) {
					System.out.println("There was an error with the following message:\n" + ex.getMessage() + "\n");
					continue;
				}
				
				// Execute the query
				query();
			}
		}
	}
	
	/**
	 * Executes the query.
	 */
	public static void query() {
		filterRecords();
		printFilteredRecords();
	}
	
	/**
	 * Filters the records from the database with the given filters in the query.
	 */
	public static void filterRecords() {
		filteredRecords = db.filter(new QueryFilter(qp.getQuery()));
	}
	
	/**
	 * Prints the filtered records.
	 */
	public static void printFilteredRecords() {
		// If there is no records to show
				if (filteredRecords.size() == 0) {
					System.out.println("Records selected: 0\n");
					return;
				}
		
		// Checks whether the index was used for retrieving record
		if (qp.getQuery().size() == 1) {
			ConditionalExpression expr = qp.getQuery().get(0);
			if (expr.getFieldGetter() == FieldValueGetters.JMBAG && expr.getComparisonOperator() == ComparisonOperators.EQUALS) {
				System.out.println("Using index for record retrieval.");
			}
		}
		
		// Max number of characters in last name - used for formating
		OptionalInt maxLastName = filteredRecords.stream()
				.map(StudentRecord::getLastName)
				.mapToInt(String::length)
				.max();
		
		// Max number of characters in first name - used for formating
		OptionalInt maxFirstName = filteredRecords.stream()
				.map(StudentRecord::getFirstName)
				.mapToInt(String::length)
				.max();
		
		// Prints the "header"
		printHeaderFooter(maxLastName.getAsInt(), maxFirstName.getAsInt());
		
		// Prints the records
		filteredRecords.forEach(s -> printRecord(maxLastName.getAsInt(), maxFirstName.getAsInt(), s));
		
		// Prints the "footer"
		printHeaderFooter(maxLastName.getAsInt(), maxFirstName.getAsInt());
		
		// Prints number of records
		System.out.println("Records selected: " + filteredRecords.size() + "\n");
	}
	
	/**
	 * Prints the header/footer, adjusts it to the longest first/last name.
	 * @param maxLastName longest last name
	 * @param maxFirstName longest first name
	 */
	public static void printHeaderFooter(int maxLastName, int maxFirstName) {
		System.out.format("+============+=");
		for (int i = 0; i < maxLastName; i++)
			System.out.format("=");
		System.out.format("=+=");
		for (int i = 0; i < maxFirstName; i++)
			System.out.format("=");
		System.out.println("=+===+");
	}
	
	/**
	 * Prints the student record whilst keeping the formating correct.
	 * @param maxLastName longest last name
	 * @param maxFirstName longest first name
	 * @param s student record to print
	 */
	public static void printRecord(int maxLastName, int maxFirstName, StudentRecord s) {
		StringBuilder sb = new StringBuilder();
		sb.append("| ").append(s.getJmbag()).append(" | ").append(s.getLastName());
		for (int i = 0; i < maxLastName - s.getLastName().length(); i++) {
			sb.append(" ");
		}
		sb.append(" | ").append(s.getFirstName());
		for (int i = 0; i < maxFirstName - s.getFirstName().length(); i++) {
			sb.append(" ");
		}
		sb.append(" | ").append(s.getFinalGrade()).append(" |");
		
		System.out.println(sb.toString());
	}
}
