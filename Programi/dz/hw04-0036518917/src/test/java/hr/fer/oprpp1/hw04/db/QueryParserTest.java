package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class QueryParserTest {

	@Test
	public void testQueryParser1() {
		QueryParser qp = new QueryParser(" jmbag =\"0123456789\"  ");
		
		assertEquals(true, qp.isDirectQuery());
		assertEquals("0123456789", qp.getQueriedJMBAG());
		assertEquals(1, qp.getQuery().size());
	}
	
	@Test
	public void testQueryParser2() {
		QueryParser qp = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		
		assertEquals(false, qp.isDirectQuery());
		assertEquals(2, qp.getQuery().size());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());
	}
	
	@Test
	public void testQueryParser3() {
		QueryParser qp = new QueryParser("jmbag=\"0000000003\"");
		
		assertEquals(true, qp.isDirectQuery());
		assertEquals("0000000003", qp.getQueriedJMBAG());
		assertEquals(1, qp.getQuery().size());
	}
	
	@Test
	public void testQueryParser4() {
		QueryParser qp = new QueryParser(" lastName =\n\"Blažić\"");
		
		assertEquals(false, qp.isDirectQuery());
		assertEquals(1, qp.getQuery().size());
	}
	
	@Test
	public void testQueryParser5() {
		QueryParser qp = new QueryParser("firstName>\"A\" and lastName LIKE \"B*ć\"");
		
		assertEquals(false, qp.isDirectQuery());
		assertEquals(2, qp.getQuery().size());
	}
	
	@Test
	public void testQueryParser6() {
		QueryParser qp = new QueryParser("firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		
		assertEquals(false, qp.isDirectQuery());
		assertEquals(4, qp.getQuery().size());
	}
}
