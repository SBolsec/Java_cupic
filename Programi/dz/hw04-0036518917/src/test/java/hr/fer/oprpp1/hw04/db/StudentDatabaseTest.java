package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StudentDatabaseTest {
	
	@Test
	public void testDuplicateJmbag() {
		List<String> list = Arrays.asList(
				"0000000001	Akšamović	Marin	2",
				"0000000002	Bakamović	Petra	3",
				"0000000001	Glavinić Pecotić	Kristijan	4"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testNoNameOrLastName() {
		List<String> list = Arrays.asList(
				"0000000001	2"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testNoLastName() {
		List<String> list = Arrays.asList(
				"0000000001 Marin	2"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testNoFirstName() {
		List<String> list = Arrays.asList(
				"0000000001 Akšamović	2"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testNoFinalGrade() {
		List<String> list = Arrays.asList(
				"0000000001	Akšamović	Marin"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testFinalGradeTooLow() {
		List<String> list = Arrays.asList(
				"0000000001	Akšamović	Marin 0"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testFinalGradeTooBig() {
		List<String> list = Arrays.asList(
				"0000000001	Akšamović	Marin 6"
		);
		
		assertThrows(IllegalArgumentException.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testForJmbag() {
		List<String> list = Arrays.asList(
			"0000000001	Akšamović	Marin	2",
			"0000000002	Bakamović	Petra	3",
			"0000000015	Glavinić Pecotić	Kristijan	4"
		);
		StudentDatabase db = new StudentDatabase(list);
		
		StudentRecord s1 = new StudentRecord("0000000001", "Akšamović", "Marin", 2);
		StudentRecord s2 = new StudentRecord("0000000002", "Bakamović", "Petra", 3);
		StudentRecord s3 = new StudentRecord("0000000015", "Glavinić Pecotić", "Kristijan", 4);
		
		assertEquals(s1, db.forJMBAG("0000000001"));
		assertEquals(s2, db.forJMBAG("0000000002"));
		assertEquals(s3, db.forJMBAG("0000000015"));
		assertEquals(null, db.forJMBAG("asd"));
	}
	
	@Test
	public void testFilterAlwaysTrue() {
		List<String> list = Arrays.asList(
			"0000000001	Akšamović	Marin	2",
			"0000000002	Bakamović	Petra	3",
			"0000000015	Glavinić Pecotić	Kristijan	4"
		);
		StudentDatabase db = new StudentDatabase(list);
		
		StudentRecord s1 = new StudentRecord("0000000001", "Akšamović", "Marin", 2);
		StudentRecord s2 = new StudentRecord("0000000002", "Bakamović", "Petra", 3);
		StudentRecord s3 = new StudentRecord("0000000015", "Glavinić Pecotić", "Kristijan", 4);
		
		List<StudentRecord> expected = new ArrayList<>();
		expected.add(s1);
		expected.add(s2);
		expected.add(s3);
		
		List<StudentRecord> actual = db.filter(a -> true); // always true filter
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFilterAlwaysFalse() {
		List<String> list = Arrays.asList(
			"0000000001	Akšamović	Marin	2",
			"0000000002	Bakamović	Petra	3",
			"0000000015	Glavinić Pecotić	Kristijan	4"
		);
		StudentDatabase db = new StudentDatabase(list);
		
		List<StudentRecord> expected = new ArrayList<>();
		
		List<StudentRecord> actual = db.filter(a -> false);
		
		assertEquals(expected, actual);
	}
}
