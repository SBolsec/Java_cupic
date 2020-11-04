package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FieldValueGettersTest {
	
	@Test
	public void testFirstName() {
		StudentRecord s1 = new StudentRecord("0000000001", "Peric", "Pero", 3);
		StudentRecord s2 = new StudentRecord("0000000002", "Ivic", "Ivo", 2);
		
		IFieldValueGetter getter = FieldValueGetters.FIRST_NAME;
		
		assertEquals("Pero", getter.get(s1));
		assertEquals("Ivo", getter.get(s2));
	}
	
	@Test
	public void testLastName() {
		StudentRecord s1 = new StudentRecord("0000000001", "Peric", "Pero", 3);
		StudentRecord s2 = new StudentRecord("0000000002", "Ivic", "Ivo", 2);
		
		IFieldValueGetter getter = FieldValueGetters.LAST_NAME;
		
		assertEquals("Peric", getter.get(s1));
		assertEquals("Ivic", getter.get(s2));
	}
	
	@Test
	public void testJmbag() {
		StudentRecord s1 = new StudentRecord("0000000001", "Peric", "Pero", 3);
		StudentRecord s2 = new StudentRecord("0000000002", "Ivic", "Ivo", 2);
		
		IFieldValueGetter getter = FieldValueGetters.JMBAG;
		
		assertEquals("0000000001", getter.get(s1));
		assertEquals("0000000002", getter.get(s2));
	}
}
