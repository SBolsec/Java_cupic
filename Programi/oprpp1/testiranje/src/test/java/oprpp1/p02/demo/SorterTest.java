package oprpp1.p02.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

public class SorterTest {

	@Test
    public void testAlreadySortedArray() {
    	int[] array = {3, 7};
    	int[] result = Sorter.sortirajDvaElementa(array);
    	int[] expected = new int[] {3, 7};
    	
    	// provjeri element po element jesu li result i expected jednaki
    	// ako jesu, test prolazi, inace pada
    	// for (int i = 0; i < 2; i++) { mora imati dva elemenata!
    	// 	if (result[i] != expected[i]) Assertions.fail();
    	// }
    	
    	assertArrayEquals(expected, result);
	}
	
	@Test
    public void testShouldPerformSorting() {
		assertArrayEquals(
				new int[] {3, 7},
				Sorter.sortirajDvaElementa(new int[] {7, 3})
		);
	}
	
	// Necemo ovako!
	@Disabled
	@Test
	public void testNullArrayShouldThrow() {
		try {
			Sorter.sortirajDvaElementa(null);
		} catch (NullPointerException ex) {
			return;
		}
		
		fail("Ocekivao sam NullPointerException ali ga metoda nije bacila!!!");
	}
	
	@Test
	public void testToShortArrayShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> Sorter.sortirajDvaElementa(new int[] {6}));
	}
	
	@Test
	public void testToLongArrayShouldThrow() {
		assertThrows(IllegalArgumentException.class, () -> Sorter.sortirajDvaElementa(new int[] {6, 3, 15}));
	}
}