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
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
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
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
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
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
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
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertTrue(examMarks.containsKey("Jasna"));
    }

    @Test
    public void testDoesNotContainKey() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertFalse(examMarks.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertTrue(examMarks.containsValue(2));
    }

    @Test
    public void testDoesNotContainValue() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 3);
        examMarks.put("Jasna", 4);

        assertFalse(examMarks.containsValue(20));
    }

    @Test
    public void testIsEmptyTrue() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);

        assertTrue(examMarks.isEmpty());
    }

    @Test
    public void testIsEmptyFalse() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(4);
        examMarks.put("Ante", 3);

        assertFalse(examMarks.isEmpty());
    }

    @Test
    public void testToArray() {
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(16);
        // fill data:
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
        // create collection:
        SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
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
        // create collection:
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
            // create collection:
            SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
            // fill data:
            examMarks.put("Ivana", 2);
            examMarks.put("Ante", 2);
            examMarks.put("Jasna", 2);
            examMarks.put("Kristina", 5);
            examMarks.put("Ivana", 5); // overwrites old grade for Ivana
            // query collection:
            Integer kristinaGrade = examMarks.get("Kristina");
            System.out.println("Kristina's exam grade is: " + kristinaGrade); // writes: 5
            // What is collection's size? Must be four!
            System.out.println("Number of stored pairs: " + examMarks.size()); // writes: 4

            assertEquals("[Ante=2, Ivana=5, Jasna=2, Kristina=5]", examMarks.toString());
    }

    @Test
    public void testIteratorNormalOutput() { // important console output
        // create collection:
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }
    }

    @Test
    public void testParallelIterator() { //Cartesie (spelling!)
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        for(SimpleHashtable.TableEntry<String,Integer> pair1 : examMarks) {
            for(SimpleHashtable.TableEntry<String,Integer> pair2 : examMarks) {
                System.out.printf(
                        "(%s => %d) - (%s => %d)%n",
                        pair1.getKey(), pair1.getValue(),
                        pair2.getKey(), pair2.getValue()
                        );
            }
        }
    }

    @Test
    public void testIteratorEmptyTable() {
        // create collection:
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }
    }

    @Test
    public void testIteratorRemove() { //Cartesie (spelling!)
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
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
    public void testIteratorDoubleRemove() { //Cartesie (spelling!)
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
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
    public void testIteratorOuterRemove() { //Cartesie (spelling!)
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
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

    @Test
    public void testFinalOutput() {
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
        // fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
        while(iter.hasNext()) {
            SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        System.out.printf("Veličina: %d%n", examMarks.size());
    }
}
