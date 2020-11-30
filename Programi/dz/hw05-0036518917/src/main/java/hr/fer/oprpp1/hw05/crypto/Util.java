package hr.fer.oprpp1.hw05.crypto;

/**
 * This class provides functions which convert hexadecimal strings into byte arrays
 * and the other way round.
 * @author sbolsec
 *
 */
public class Util {

	/**
	 * Generates byte array from given hexadecimal string
	 * @param keyText input text in hexadecimal format
	 * @return byte array created from hexadecimal input string
	 * @throws IllegalArgumentException if the input was invalid
	 */
	public static byte[] hextobyte(String keyText) {
		int len = keyText.length();
		
		if (len % 2 != 0)
			throw new IllegalArgumentException("Length can not be odd, it was: " + len + ".");
		
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	    	if (!isHex(keyText.charAt(i)))
	    		throw new IllegalArgumentException("There was a non-hex character, it was: " + keyText.charAt(i) + ".");
	    	if (!isHex(keyText.charAt(i+1)))
	    		throw new IllegalArgumentException("There was a non-hex character, it was: " + keyText.charAt(i) + ".");
	    	
	        data[i / 2] = (byte) ((Character.digit(keyText.charAt(i), 16) << 4)
	                             + Character.digit(keyText.charAt(i+1), 16));
	    }
	    return data;
	}
	
	/**
	 * Generates hexadecimal string from given byte array
	 * @param bytearray
	 * @return
	 */
	public static String bytetohex(byte[] bytearray) {
		StringBuilder sb = new StringBuilder();
		
		for (byte b : bytearray) {
			
			byte first = (byte) ((b & 0xf0) >> 4);
			byte second = (byte) (b & 0x0f);
			
			sb.append(generateCharFromHex(first));
			sb.append(generateCharFromHex(second));
		}
		
		return sb.toString();
	}
	
	/**
	 * Checks whether the given character is a hexadecimal character
	 * @param c character to be tested
	 * @return true if the given character is a hexadecimal character, false otherwise
	 */
	private static boolean isHex(char c) {
		c = Character.toLowerCase(c);
		if (Character.isDigit(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f')
			return true;
		return false;
	}
	
	/**
	 * Generates a hexadecimal character from given byte
	 * @param b byte from which to generate hexadecimal character
	 * @return hexadecimal character generated from given byte
	 */
	private static char generateCharFromHex(byte b) {
		switch(b) {
			case 0: return '0';
			case 1: return '1';
			case 2: return '2';
			case 3: return '3';
			case 4: return '4';
			case 5: return '5';
			case 6: return '6';
			case 7: return '7';
			case 8: return '8';
			case 9: return '9';
			case 10: return 'a';
			case 11: return 'b';
			case 12: return 'c';
			case 13: return 'd';
			case 14: return 'e';
			case 15: return 'f';
			default: throw new IllegalArgumentException();
		}
	}
}
