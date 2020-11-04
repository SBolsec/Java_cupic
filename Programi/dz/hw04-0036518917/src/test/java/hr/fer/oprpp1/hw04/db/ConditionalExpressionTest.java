package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConditionalExpressionTest {

	@Test
	public void testConditionalExpression1() {
		ConditionalExpression expr = new ConditionalExpression(
			FieldValueGetters.LAST_NAME,
			"Bos*",
			ComparisonOperators.LIKE
		);
		
		StudentRecord record = new StudentRecord("0000000000", "Bosnic", "Jasna", 4);
		
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),	// returns lastName from given record
				expr.getStringLiteral()				// returns "Bos*"
		);
		
		assertEquals(true, recordSatisfies);
	}
	
	@Test
	public void testConditionalExpression2() {
		ConditionalExpression expr = new ConditionalExpression(
			FieldValueGetters.JMBAG,
			"0000000000",
			ComparisonOperators.EQUALS
		);
		
		StudentRecord record = new StudentRecord("0000000000", "Bosnic", "Jasna", 4);
		
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),	// returns lastName from given record
				expr.getStringLiteral()				// returns "Bos*"
		);
		
		assertEquals(true, recordSatisfies);
	}
	
	@Test
	public void testConditionalExpression3() {
		ConditionalExpression expr = new ConditionalExpression(
			FieldValueGetters.FIRST_NAME,
			"*na",
			ComparisonOperators.LIKE
		);
		
		StudentRecord record = new StudentRecord("0000000000", "Bosnic", "Jasna", 4);
		
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),	// returns lastName from given record
				expr.getStringLiteral()				// returns "Bos*"
		);
		
		assertEquals(true, recordSatisfies);
	}
	
	@Test
	public void testConditionalExpression() {
		ConditionalExpression expr = new ConditionalExpression(
			FieldValueGetters.LAST_NAME,
			"Bosa*",
			ComparisonOperators.LIKE
		);
		
		StudentRecord record = new StudentRecord("0000000000", "Bosnic", "Jasna", 4);
		
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),	// returns lastName from given record
				expr.getStringLiteral()				// returns "Bos*"
		);
		
		assertEquals(false, recordSatisfies);
	}
	
	@Test
	public void testConditionalExpression4() {
		ConditionalExpression expr = new ConditionalExpression(
			FieldValueGetters.LAST_NAME,
			"Peric",
			ComparisonOperators.LESS
		);
		
		StudentRecord record = new StudentRecord("0000000000", "Bosnic", "Jasna", 4);
		
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),	// returns lastName from given record
				expr.getStringLiteral()				// returns "Bos*"
		);
		
		assertEquals(true, recordSatisfies);
	}
}
