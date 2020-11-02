package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleHashtableTest {

    @Test
    public void testIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<>(0));
    }

    @Test
    public void testPut() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        assertNull(examMarks.put("Ivana", 2));

        assertEquals(2, examMarks.get("Ivana"));
    }

    @Test
    public void testPutChange() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        assertNull(examMarks.put("Ivana", 2));
        assertEquals(2, examMarks.put("Ivana", 52));

        assertEquals(52, examMarks.get("Ivana"));
    }

    @Test
    public void testPutNull() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        assertThrows(NullPointerException.class, () -> examMarks.put(null, 2));
    }

    @Test
    public void testGetNonexistent() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

        assertNull(examMarks.get("marko"));
    }

    @Test
    public void testGet() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);

        assertEquals(2, examMarks.get("Ivana"));
    }

    @Test
    public void testRemove() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertEquals(2, examMarks.get("Ivana"));
        assertEquals(3, examMarks.get("Ante"));
        assertEquals(4, examMarks.get("Jasna"));

        examMarks.remove("Jasna");

        assertEquals(2, examMarks.get("Ivana"));
        assertEquals(3, examMarks.get("Ante"));
        assertNull(examMarks.get("Jasna"));
    }

    @Test
    public void testRemoveNull() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertEquals(2, examMarks.get("Ivana"));
        assertEquals(3, examMarks.get("Ante"));
        assertEquals(4, examMarks.get("Jasna"));

        assertNull(examMarks.remove(null));
    }

    @Test
    public void testRemoveNonexistent() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertEquals(2, examMarks.get("Ivana"));
        assertEquals(3, examMarks.get("Ante"));
        assertEquals(4, examMarks.get("Jasna"));

        assertNull(examMarks.remove("marko"));
    }

    @Test
    public void testContainsKey() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertTrue(examMarks.containsKey("Jasna"));
    }

    @Test
    public void testDoesNotContainKey() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertFalse(examMarks.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertTrue(examMarks.containsValue(2));
    }

    @Test
    public void testDoesNotContainValue() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertFalse(examMarks.containsValue(20));
    }

    @Test
    public void testIsEmptyTrue() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);

        assertTrue(examMarks.isEmpty());
    }

    @Test
    public void testIsEmptyFalse() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ante", 3);

        assertFalse(examMarks.isEmpty());
    }

    @Test
    public void testToArray() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(16);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Marko", 5); // overwrites old grade for Ivana

        SimpleHashtable.TableEntry<String, Integer>[] array = examMarks.toArray();

        int testSize = 0;
        for(SimpleHashtable.TableEntry<String, Integer> entry : array) {
            testSize++;
            assertEquals(entry.getValue(), examMarks.get(entry.getKey()));
        }
        assertEquals(5, testSize);
    }

    @Test
    public void testExpansion() { //only achievable using compiler
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Josip", 5);
        examMarks.put("Sinisa", 8);
        examMarks.put("Nikola", 5);
        examMarks.put("Branimir", 11);
        examMarks.put("Martina", 5);


        SimpleHashtable.TableEntry<String, Integer>[] array = examMarks.toArray();

        int testSize = 0;
        for(SimpleHashtable.TableEntry<String, Integer> entry : array) {
            testSize++;
            assertEquals(entry.getValue(), examMarks.get(entry.getKey()));
        }
        assertEquals(9, testSize);
    }

    @Test
    public void testClear() {
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Josip", 5);
        examMarks.put("Sinisa", 8);
        examMarks.put("Nikola", 5);
        examMarks.put("Branimir", 11);
        examMarks.put("Martina", 5);
        examMarks.clear();
        assertTrue(examMarks.isEmpty());
    }

    @Test
    public void testFunctionality() {
            SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
            examMarks.put("Ivana", 2);
            examMarks.put("Ante", 2);
            examMarks.put("Jasna", 2);
            examMarks.put("Kristina", 5);
            examMarks.put("Ivana", 5); // overwrites old grade for Ivana
            
            assertEquals(5, examMarks.get("Kristina"));
            assertEquals(4, examMarks.size());
            assertEquals("[Ante=2, Ivana=5, Jasna=2, Kristina=5]", examMarks.toString());
    }

    @Test
    public void testIteratorRemove() { 
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            if(pair.getKey().equals("Ivana")) {
                iter.remove();
            }
        }
        assertFalse(examMarks.containsKey("Ivana"));
    }

    @Test
    public void testIteratorDoubleRemove() {
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            if(pair.getKey().equals("Ivana")) {
                iter.remove();
                assertThrows(IllegalStateException.class, () -> iter.remove());
            }
        }
        assertFalse(examMarks.containsKey("Ivana"));
    }

    @Test
    public void testIteratorOuterRemove() {
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            if(pair.getKey().equals("Ivana")) {
                examMarks.remove("Ivana");
                break;
            }
        }
        assertThrows(ConcurrentModificationException.class, () -> iter.hasNext());
    }
}
