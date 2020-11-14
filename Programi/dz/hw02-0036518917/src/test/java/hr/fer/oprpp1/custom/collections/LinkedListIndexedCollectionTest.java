package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LinkedListIndexedCollectionTest {

    private LinkedListIndexedCollection list;

    @Test
    public void testConstructor() {
        list = new LinkedListIndexedCollection();

        assertEquals(0, list.size());
        assertEquals(true, list.isEmpty());
    }

    @Test
    public void testConstructorOtherCollection() {
        LinkedListIndexedCollection other = new LinkedListIndexedCollection();
        other.add(Integer.valueOf(1));
        other.add(Integer.valueOf(2));
        list = new LinkedListIndexedCollection(other);

        Object[] expected = {1, 2};
        assertEquals(2, list.size());
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
    }

    @Test
    public void testAddNull() {
        list = new LinkedListIndexedCollection();
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    public void testAddFirstElement() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));

        assertEquals(1, list.size());
        assertArrayEquals(new Object[] {1}, list.toArray());
    }

    @Test
    public void testAddElement() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));

        assertEquals(2, list.size());
        assertArrayEquals(new Object[] {1, 2}, list.toArray());
    }

    @Test
    public void testGet() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.add("Test");
        list.add("Haja");
        list.add(Character.valueOf('/'));

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        assertEquals("Test", list.get(2));
        assertEquals("Haja", list.get(3));
        assertEquals(Character.valueOf('/'), list.get(4));
    }

    @Test
    public void testGetIndexLessThanZero() {
        list = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-2));
    }

    @Test
    public void testGetIndexGreaterThanSize() {
        list = new LinkedListIndexedCollection();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
    }

    @Test
    public void testClear() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.clear();

        assertEquals(0, list.size());
        assertArrayEquals(new Object[] {}, list.toArray());
    }

    @Test
    public void testInsertIndexOutOfBounds() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(Integer.valueOf(1), 20));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(Integer.valueOf(1), -2));
    }

    @Test
    public void testInsertNull() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertThrows(NullPointerException.class, () -> list.insert(null, 1));
    }

    @Test
    public void testInsert() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.insert(Integer.valueOf(5), 1);
        list.insert(Integer.valueOf(6), 4);
        list.add("Test");
        list.add("Haja");
        list.insert(Character.valueOf('*'), 3);
        list.insert(Character.valueOf('/'), 6);
        Object[] expected = {1, 5, 2, '*', 1, 6, '/', 2, "Test", "Haja"};
        assertEquals(10, list.size());
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void testInsertAtStart() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.insert(Integer.valueOf(5), 0);
        Object[] expected = {5, 1, 2};
        assertEquals(3, list.size());
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void testInsertAtEnd() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.insert(Integer.valueOf(5), 2);
        Object[] expected = {1, 2, 5};
        assertEquals(3, list.size());
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void testIndexOf() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertEquals(1, list.indexOf(Integer.valueOf(2)));
    }

    @Test
    public void testIndexOfNull() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertEquals(-1, list.indexOf(null));
    }

    @Test
    public void testIndexOfObjectNotInList() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertEquals(-1, list.indexOf(Integer.valueOf(5)));
    }
    
    @Test
    public void testRemoveIndexLessThanZero() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-2));
    }

    @Test
    public void testRemoveIndexGreaterThanSize() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    }

    @Test
    public void testRemove() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.remove(2);
        Object[] expected = {1, 2, 2};
        assertEquals(3, list.size());
        assertArrayEquals(expected, list.toArray());
    }
    
    @Test
    public void testRemoveOnlyOneElement() {
    	list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.remove(0);
        assertEquals(0, list.size());
    }

    @Test
    public void testRemoveFromStart() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.remove(0);
        Object[] expected = {2, 1, 2};
        assertEquals(3, list.size());
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void testRemoveFromEnd() {
        list = new LinkedListIndexedCollection();
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.add(Integer.valueOf(1));
        list.add(Integer.valueOf(2));
        list.remove(3);
        Object[] expected = {1, 2, 1};
        assertArrayEquals(expected, list.toArray());
    }
    
    @Test
	public void testContains() {
    	list = new LinkedListIndexedCollection();
    	list.add(Integer.valueOf(1));
    	list.add(Integer.valueOf(2));
    	list.add("Test");
    	list.add(Character.valueOf('*'));
		assertEquals(true, list.contains(1));
		assertEquals(true, list.contains("Test"));
		assertEquals(false, list.contains(7));
		assertEquals(false, list.contains('/'));
		assertEquals(false, list.contains(null));
	}

	@Test
	public void testRemoveObject() {
		list = new LinkedListIndexedCollection();
		list.add(Integer.valueOf(1));
		list.add(Integer.valueOf(2));
		list.add("Test");
		list.add(Character.valueOf('*'));
		assertEquals(true, list.remove(Integer.valueOf(1)));
		assertEquals(false, list.remove(Integer.valueOf(1)));
		assertEquals(true, list.remove("Test"));
		assertEquals(false, list.remove(Integer.valueOf(7)));
		assertEquals(false, list.remove(Character.valueOf('/')));
		assertEquals(true, list.remove(Character.valueOf('*')));
		assertEquals(false, list.remove(null));
	}
}
