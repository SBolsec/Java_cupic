package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CollectionsTest {
	
	@Test
	public void testStringCollection() {
		Collection<String> col = new ArrayIndexedCollection<>();
		col.add("Marko");
		col.add("Jasna");
		
		assertArrayEquals(new Object[] {"Marko",  "Jasna"}, col.toArray());
	}
	
	@Test
	public void testNumberList() {
		List<Number> list = new LinkedListIndexedCollection<>();
		list.add(Integer.valueOf(1));
		list.add(Double.valueOf(3.1415926));
		
		assertArrayEquals(new Object[] {1,  3.1415926}, list.toArray());
	}
	
	@Test
	public void testGetObject() {
		ArrayIndexedCollection<String> col = new ArrayIndexedCollection<>();
		col.add("Marko");
		col.add("Jasna");
		
		assertEquals("Marko", col.get(0));
		assertEquals(false, col.contains(Integer.valueOf(5)));
	}
	
	@Test
	public void testElementsGetter() {
		List<Number> list = new LinkedListIndexedCollection<>();
		list.add(Integer.valueOf(1));
		list.add(Double.valueOf(3.1415926));
		list.add(Integer.valueOf(5));
		list.add(Float.valueOf("1.41"));
		
		ElementsGetter<Number> getter = list.createElementsGetter();
		assertEquals(1, getter.getNextElement());
		assertEquals(3.1415926, getter.getNextElement());
		assertEquals(5, getter.getNextElement());
		assertEquals(1.41f, getter.getNextElement());
	}
	
	@Test
	public void testNested() {
		List<String> col = new ArrayIndexedCollection<>();
		col.add("Marko");
		col.add("Jasna");
		
		ObjectStack<List<String>> stack = new ObjectStack<>();
		stack.push(col);
		
		assertEquals(col, stack.peek());
	}
}
