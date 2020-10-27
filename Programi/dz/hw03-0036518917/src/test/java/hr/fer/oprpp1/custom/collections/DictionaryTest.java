package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DictionaryTest {
	
	@Test
	public void testDictionary() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals(false, dict.isEmpty());
		assertEquals(2, dict.size());
		assertEquals("Marko", dict.get(5));
		assertEquals(null, dict.get("a09h"));
	}
	
	@Test
	public void testDictionaryPutOverride() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		dict.put(5, "Ivo");
		assertEquals(2, dict.size());
		assertEquals("Ivo", dict.get(5));
	}
	
	@Test
	public void testDictionaryPutOverrideReturnsOldValue() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals("Marko", dict.put(5, "Ivo"));
		assertEquals(2, dict.size());
	}
	
	@Test
	public void testDictionaryRemove() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals("Marko", dict.remove(5));
		assertEquals(1, dict.size());
	}
	
	@Test
	public void testDictionaryRemoveNonExisting() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals(null, dict.remove(7));
		assertEquals(2, dict.size());
	}
	
	@Test
	public void testDictionaryIsEmpty() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		assertEquals(true, dict.isEmpty());
		
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals(false, dict.isEmpty());
	}
	
	@Test
	public void testDictionarySize() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		assertEquals(0, dict.size());
		
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals(2, dict.size());
		
		dict.put(1, "Ivo");
		
		assertEquals(2, dict.size());
		
		dict.remove(5);
		
		assertEquals(1, dict.size());
	}
	
	@Test
	public void testDictionaryClear() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(1, "Jasmina");
		dict.put(5, "Marko");
		
		assertEquals(false, dict.isEmpty());
		assertEquals(2, dict.size());
		
		dict.clear();
		
		assertEquals(0, dict.size());
	}
	
	@Test
	public void testDictionaryList() {
		List<Number> list = new LinkedListIndexedCollection<>();
		list.add(1);
		list.add(3.1415926);
		list.add(1.41f);
		
		Dictionary<String, List<Number>> dict = new Dictionary<>();
		dict.put("Prvi", list);
		dict.put("Drugi", list);
		
		assertEquals(2, dict.size());
		assertEquals(list, dict.get("Prvi"));
		assertEquals(list, dict.get("Drugi"));
	}
	
	
}
