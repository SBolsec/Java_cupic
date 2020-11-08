package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Database which holds student records.
 * @author sbolsec
 *
 */
public class StudentDatabase {

	/** List that stores student records **/
	List<StudentRecord> students;
	/** Map which serves as an index for faster retrieval of student records using their jmbag **/
	Map<String, StudentRecord> jmbagIndex;
	
	/**
	 * Constructor which parses the input into student records and stores them.
	 * @param list list of students
	 */
	public StudentDatabase(List<String> list) {
		students = new ArrayList<>();
		jmbagIndex = new HashMap<>();
		
		for (String s : list) {
			if (s.isBlank())
				continue;
			
			try (Scanner sc = new Scanner(s)) {
				if (!sc.hasNext()) throw new IllegalArgumentException("There was no jmbag!");
				String jmbag = sc.next();
				if (!isJmbagValid(jmbag)) throw new IllegalArgumentException("Jmbag was not valid, it was: " + jmbag + ".");
				
				if (!sc.hasNext()) throw new IllegalArgumentException("There was no last name!");
				String first = sc.next();
				
				if (!sc.hasNext()) throw new IllegalArgumentException("There was no middle/first name!");
				String second = sc.next();
				String third = null;
				if (!sc.hasNextInt()) {
					if (!sc.hasNext()) throw new IllegalArgumentException("There was no first name or final grade!");
					third = sc.next();
				}
				if (!sc.hasNextInt()) throw new IllegalArgumentException("There was no final grade!");
				int grade = sc.nextInt();
				if (grade < 1 || grade > 5) throw new IllegalArgumentException("Final grade must be in range (1, 5), it was: " + grade + ".");
				
				if (sc.hasNext()) throw new IllegalArgumentException("There was data after final grade!");
				
				String firstName;
				String lastName;
				if (third == null) {
					lastName = first;
					firstName = second;
				} else {
					lastName = first + " " + second;
					firstName = third;
				}
				StudentRecord record = new StudentRecord(jmbag, lastName, firstName, grade);
				students.add(record);
				jmbagIndex.put(jmbag, record);
			}
		}
	}
	
	/**
	 * Returns the student record which has the given jmbag or null if it does not exist.
	 * @param jmbag jmbag of student record which to return
	 * @return student record whihch has the given jmbag or null if it does not exist
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return jmbagIndex.get(jmbag);
	}
	
	/**
	 * Returns a list of student records which satisfied the given filter.
	 * @param filter filter which tests student records
	 * @return list of student records which satisfied the given filter
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> result = new ArrayList<>();
		
		students.forEach(s -> {
			if (filter.accepts(s))
				result.add(s);
		});
		
		return result;
	}
	
	/**
	 * Checks whether the jmbag is valid and that it was not already used.
	 * @param jmbag jmbag to be checked
	 * @return true if the jmbag is valid, false otherwise
	 * @throws IllegalArgumentException if the jmbag was already used
	 */
	private boolean isJmbagValid(String jmbag) {
		if (jmbag.length() != 10) return false;
		for (char c : jmbag.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		if (jmbagIndex.containsKey(jmbag)) throw new IllegalArgumentException("This jmbag was already used!");
		return true;
		
	}
}
