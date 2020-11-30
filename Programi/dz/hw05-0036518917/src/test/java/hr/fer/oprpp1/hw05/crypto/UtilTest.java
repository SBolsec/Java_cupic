package hr.fer.oprpp1.hw05.crypto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UtilTest {

	@Test
	public void testHextobyteOddSized() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("1aa"));
	}
	
	@Test
	public void testHextobyteInvalidCharacters1() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("P1"));
	}
	
	@Test
	public void testHextobyteInvalidCharacters2() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("1P"));
	}
	
	@Test
	public void testHextobyteInvalidCharacters3() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("PP"));
	}
	
	@Test
	public void testHextobyteEmptyInput() {
		assertEquals(0, Util.hextobyte("").length);
	}
	
	@Test
	public void testHextobyteValidInput() {
		byte[] expected = {1, -82, 34};
		byte[] actual = Util.hextobyte("01aE22");
		
		compareByteArrays(expected, actual);
	}
	
	private void compareByteArrays(byte[] expected, byte[] actual) {
		if (expected.length != actual.length)
			fail();
		
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}
	
	@Test
	public void testBytetohexZeroLength() {
		assertEquals("", Util.bytetohex(new byte[0]));
	}
	
	@Test
	public void testBytetohex() {
		String expected = "01ae22";
		String actual = Util.bytetohex(new byte[] {1, -82, 34});
		assertEquals(expected, actual);
	}
}
