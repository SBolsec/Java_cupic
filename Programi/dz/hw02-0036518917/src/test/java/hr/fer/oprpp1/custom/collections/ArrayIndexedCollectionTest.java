package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {

	private ArrayIndexedCollection array;

	@Test
	public void testDefaultConstrucor() {
		array = new ArrayIndexedCollection();
		assertEquals(0, array.size());
		assertArrayEquals(new Object[] {}, array.toArray());
	}
	
	@Test
	public void testConstructorCustomInitialCapacity() {
		array = new ArrayIndexedCollection(8);
		assertEquals(0, array.size());
		assertArrayEquals(new Object[] {}, array.toArray());
	}
	
	@Test
	public void testConstructorInitialCapacityLessThanOne() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
	}
	
	@Test
	public void testConstructorWithNullPassedAsOtherCollection() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}
	
	@Test
	public void testConstructorOtherEmptyCollection() {
		ArrayIndexedCollection other = new ArrayIndexedCollection(8);
		array = new ArrayIndexedCollection(other);
		assertEquals(0, array.size());
		assertArrayEquals(new Object[] {}, array.toArray());
	}
	
	@Test
	public void testConstructorWithPassedCollection() {
		Collection other = new ArrayIndexedCollection(4);
		other.add(Integer.valueOf(1));
		other.add(Integer.valueOf(2));

		Object[] expected = {1, 2};
		assertArrayEquals(expected, new ArrayIndexedCollection(other).toArray());
	}

	@Test
	public void testConstructorPassedCollectionIsSmallerThanCapacity() {
		Collection other = new ArrayIndexedCollection(4);
		other.add(Integer.valueOf(1));
		other.add(Integer.valueOf(2));

		Object[] expected = {1, 2};
		assertArrayEquals(expected, new ArrayIndexedCollection(other, 5).toArray());

	}
	
	@Test
	public void testConstructorPassedCollectionIsGreaterThanCapacity() {
		Collection other = new ArrayIndexedCollection(5);
		other.add(Integer.valueOf(1));
		other.add(Integer.valueOf(2));

		Object[] expected = {1, 2};
		assertArrayEquals(expected, new ArrayIndexedCollection(other, 3).toArray());

	}
	
	@Test
	public void testConstructorPassedCollectionAndCapacityIsSmallerThanOne() {
		Collection other = new ArrayIndexedCollection(5);
		other.add(Integer.valueOf(1));
		other.add(Integer.valueOf(2));

		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(other, -1));

	}
	
	@Test
	public void testConstructorPassedCollectionIsNullAndCapacityIsGreaterThanOne() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 16));	
	}
	
	@Test
	public void testConstructorPassedCollectionIsNullAndCapacityIsSmallerThanOne() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 0));
	}

	@Test
	public void testAddNull() {
		array = new ArrayIndexedCollection();
		assertThrows(NullPointerException.class, () -> array.add(null));
	}

	@Test
	public void testAddFirstElement() {
		array = new ArrayIndexedCollection(1);
		array.add(Integer.valueOf(1));

		assertEquals(1, array.size());
		assertArrayEquals(new Object[] {1}, array.toArray());
	}

	@Test
	public void testAddElement() {
		array = new ArrayIndexedCollection(2);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));

		assertEquals(2, array.size());
		assertArrayEquals(new Object[] {1, 2}, array.toArray());
	}

	@Test
	public void testAddReallocate() {
		array = new ArrayIndexedCollection(2);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add(Integer.valueOf(3));

		assertEquals(3, array.size());
		assertArrayEquals(new Object[] {1, 2, 3}, array.toArray());
	}

	@Test
	public void testGet() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));

		assertEquals(Integer.valueOf(1), array.get(0));
		assertEquals(Integer.valueOf(2), array.get(1));
	}

	@Test
	public void testGetIndexLessThanZero() {
		array = new ArrayIndexedCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(-2));
	}

	@Test
	public void testGetIndexGreaterThanSize() {
		array = new ArrayIndexedCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(2));
	}

	@Test
	public void testClear() {
		array = new ArrayIndexedCollection(2);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.clear();

		assertEquals(0, array.size());
		assertArrayEquals(new Object[] {}, array.toArray());
	}

	@Test
	public void testInsertIndexOutOfBounds() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert(Integer.valueOf(1), 20));
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert(Integer.valueOf(1), -2));
	}

	@Test
	public void testInsertNull() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertThrows(NullPointerException.class, () -> array.insert(null, 1));
	}

	@Test
	public void testInsert() {
		array = new ArrayIndexedCollection(6);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.insert(Integer.valueOf(5), 1);
		array.insert(Integer.valueOf(6), 4);
		Object[] expected = {1, 5, 2, 1, 6, 2};
		assertEquals(6, array.size());
		assertArrayEquals(expected, array.toArray());
	}

	@Test
	public void testInsertAtStart() {
		array = new ArrayIndexedCollection(5);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.insert(Integer.valueOf(5), 0);
		Object[] expected = {5, 1, 2};
		assertEquals(3, array.size());
		assertArrayEquals(expected, array.toArray());
	}

	@Test
	public void testInsertAtEnd() {
		array = new ArrayIndexedCollection(5);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.insert(Integer.valueOf(5), 2);
		Object[] expected = {1, 2, 5};
		assertEquals(3, array.size());
		assertArrayEquals(expected, array.toArray());
	}

	@Test
	public void testIndexOf() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertEquals(1, array.indexOf(Integer.valueOf(2)));
	}

	@Test
	public void testIndexOfNull() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertEquals(-1, array.indexOf(null));
	}

	@Test
	public void testIndexOfObjectNotInList() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertEquals(-1, array.indexOf(Integer.valueOf(5)));
	}

	@Test
	public void testRemoveIndexLessThanZero() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-2));
	}

	@Test
	public void testRemoveIndexGreaterThanSize() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(5));
	}

	@Test
	public void testRemove() {
		array = new ArrayIndexedCollection(5);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.remove(2);
		Object[] expected = {1, 2, 2};
		assertEquals(3, array.size());
		assertArrayEquals(expected, array.toArray());
	}

	@Test
	public void testRemoveFromStart() {
		array = new ArrayIndexedCollection(5);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.remove(0);
		Object[] expected = {2, 1, 2};
		assertEquals(3, array.size());
		assertArrayEquals(expected, array.toArray());
	}

	@Test
	public void testRemoveFromEnd() {
		array = new ArrayIndexedCollection(5);
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.remove(3);
		Object[] expected = {1, 2, 1};
		assertArrayEquals(expected, array.toArray());
	}
	
	@Test
	public void testContains() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add("Test");
		array.add(Character.valueOf('*'));
		assertEquals(true, array.contains(1));
		assertEquals(true, array.contains("Test"));
		assertEquals(false, array.contains(7));
		assertEquals(false, array.contains('/'));
		assertEquals(false, array.contains(null));
	}

	@Test
	public void testRemoveObject() {
		array = new ArrayIndexedCollection();
		array.add(Integer.valueOf(1));
		array.add(Integer.valueOf(2));
		array.add("Test");
		array.add(Character.valueOf('*'));
		assertEquals(true, array.remove(Integer.valueOf(1)));
		assertEquals(false, array.remove(Integer.valueOf(1)));
		assertEquals(true, array.remove("Test"));
		assertEquals(false, array.remove(Integer.valueOf(7)));
		assertEquals(false, array.remove(Character.valueOf('/')));
		assertEquals(true, array.remove(Character.valueOf('*')));
		assertEquals(false, array.remove(null));
	}
}
