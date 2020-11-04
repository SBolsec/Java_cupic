package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ComparisonOperatorsTest {

	@Test
	public void testLessOperator() {
		IComparisonOperator comparison = ComparisonOperators.LESS;
		
		assertEquals(true, comparison.satisfied("1", "2"));
		assertEquals(true, comparison.satisfied("Ana", "Jasna"));
		assertEquals(true, comparison.satisfied("A", "AA"));
		assertEquals(false, comparison.satisfied("1", "1"));
		assertEquals(false, comparison.satisfied("2", "1"));
		assertEquals(false, comparison.satisfied("Jasna", "Ana"));
		assertEquals(false, comparison.satisfied("A", "A"));
	}
	
	@Test
	public void testLessOrEqualsOperator() {
		IComparisonOperator comparison = ComparisonOperators.LESS_OR_EQUALS;
		
		assertEquals(true, comparison.satisfied("1", "2"));
		assertEquals(true, comparison.satisfied("Ana", "Jasna"));
		assertEquals(true, comparison.satisfied("A", "AA"));
		assertEquals(true, comparison.satisfied("1", "1"));
		assertEquals(false, comparison.satisfied("2", "1"));
		assertEquals(false, comparison.satisfied("Jasna", "Ana"));
		assertEquals(true, comparison.satisfied("A", "A"));
	}
	
	@Test
	public void testGreaterOperator() {
		IComparisonOperator comparison = ComparisonOperators.GREATER;
		
		assertEquals(false, comparison.satisfied("1", "2"));
		assertEquals(false, comparison.satisfied("Ana", "Jasna"));
		assertEquals(false, comparison.satisfied("A", "AA"));
		assertEquals(false, comparison.satisfied("1", "1"));
		assertEquals(true, comparison.satisfied("2", "1"));
		assertEquals(true, comparison.satisfied("Jasna", "Ana"));
		assertEquals(false, comparison.satisfied("A", "A"));
	}
	
	@Test
	public void testGreaterOrEqualsOperator() {
		IComparisonOperator comparison = ComparisonOperators.GREATER_OR_EQUALS;
		
		assertEquals(false, comparison.satisfied("1", "2"));
		assertEquals(false, comparison.satisfied("Ana", "Jasna"));
		assertEquals(false, comparison.satisfied("A", "AA"));
		assertEquals(true, comparison.satisfied("1", "1"));
		assertEquals(true, comparison.satisfied("2", "1"));
		assertEquals(true, comparison.satisfied("Jasna", "Ana"));
		assertEquals(true, comparison.satisfied("A", "A"));
	}
	
	@Test
	public void testEqualsOperator() {
		IComparisonOperator comparison = ComparisonOperators.EQUALS;
		
		assertEquals(false, comparison.satisfied("1", "2"));
		assertEquals(false, comparison.satisfied("Ana", "Jasna"));
		assertEquals(false, comparison.satisfied("A", "AA"));
		assertEquals(true, comparison.satisfied("1", "1"));
		assertEquals(false, comparison.satisfied("2", "1"));
		assertEquals(false, comparison.satisfied("Jasna", "Ana"));
		assertEquals(true, comparison.satisfied("A", "A"));
	}
	
	@Test
	public void testNotEqualsOperator() {
		IComparisonOperator comparison = ComparisonOperators.NOT_EQUALS;
		
		assertEquals(true, comparison.satisfied("1", "2"));
		assertEquals(true, comparison.satisfied("Ana", "Jasna"));
		assertEquals(true, comparison.satisfied("A", "AA"));
		assertEquals(false, comparison.satisfied("1", "1"));
		assertEquals(true, comparison.satisfied("2", "1"));
		assertEquals(true, comparison.satisfied("Jasna", "Ana"));
		assertEquals(false, comparison.satisfied("A", "A"));
	}
	
	@Test
	public void testLikeOperator() {
		IComparisonOperator comparison = ComparisonOperators.LIKE;
		
		assertEquals(false, comparison.satisfied("AAA", "AA*AA"));
		assertEquals(false, comparison.satisfied("Zagreb", "Aba*"));
		assertEquals(true, comparison.satisfied("AAAA", "AA*AA"));
		assertEquals(true, comparison.satisfied("Nešto", "*"));
		assertEquals(true, comparison.satisfied("Marko", "*arko"));
		assertEquals(true, comparison.satisfied("Darko", "*arko"));
		assertEquals(false, comparison.satisfied("Damir", "*arko"));
		assertEquals(true, comparison.satisfied("Jasna", "Ja*a"));
		assertEquals(false, comparison.satisfied("Jasno", "Ja*a"));
		assertEquals(true, comparison.satisfied("Marko", "Mar*"));
		assertEquals(false, comparison.satisfied("Mislav", "Mar*"));
	}
}
