package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class QueryFilterTest {

	@Test
	public void testQueryFilter1() {
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
		
		StudentDatabase db = new StudentDatabase(lines);
		QueryParser qp1 = new QueryParser(" jmbag  = \"0000000001\"  ");
		
		List<StudentRecord> students = db.filter(new QueryFilter(qp1.getQuery()));
		StudentRecord expected = new StudentRecord("0000000001", "Akšamović", "Marin", 2);
		
		assertEquals(1, students.size());
		assertEquals(expected, students.get(0));
	}
	
	@Test
	public void testQueryFilter2() {
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
		
		StudentDatabase db = new StudentDatabase(lines);
		QueryParser qp1 = new QueryParser(" jmbag<=\"0000000020\" and firstName LIKE  \"Ma*a\" ");
		
		List<StudentRecord> students = db.filter(new QueryFilter(qp1.getQuery()));
		StudentRecord expected1 = new StudentRecord("0000000013", "Gagić", "Mateja", 2);
		StudentRecord expected2 = new StudentRecord("0000000018", "Gužvinec", "Matija", 3);
		
		
		assertEquals(2, students.size());
		assertEquals(expected1, students.get(0));
		assertEquals(expected2, students.get(1));
	}
}
