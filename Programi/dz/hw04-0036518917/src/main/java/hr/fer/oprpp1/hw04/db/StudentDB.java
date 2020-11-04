package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StudentDB {
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
		
		StudentDatabase db = new StudentDatabase(lines);
	}
}
