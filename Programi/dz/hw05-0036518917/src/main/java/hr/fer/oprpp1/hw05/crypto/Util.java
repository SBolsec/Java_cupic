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
	    		throw new IllegalArgumentException("There was a non-hex character, it was: " + keyText.charAt(i+1) + ".");
	    	
	        data[i / 2] = (byte) ((generateIntFromHexChar(keyText.charAt(i)) << 4)
	                             + generateIntFromHexChar(keyText.charAt(i+1)));
	    }
	    return data;
	}
	
	/**
	 * Generates hexadecimal string from given byte array
	 * @param bytearray array of bytes from which to generate a string of hex-characters
	 * @return generated string of hex-characters
	 * @throws IllegalArgumentException if a hex-character was not able to be generated from a byte in the array
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
	 * Generates int from the given hex-character
	 * @param c hex-character from which to generate int
	 * @return int value of given hex-character
	 * @throws IllegalArgumentException if the input character is not a hex-character
	 */
	private static int generateIntFromHexChar(char c) {
		if (Character.isDigit(c))
			return c - 48; // numbers start at 48 in asci
		
		switch (Character.toLowerCase(c)) {
			case 'a': return 10;
			case 'b': return 11;
			case 'c': return 12;
			case 'd': return 13;
			case 'e': return 14;
			case 'f': return 15;
			default: throw new IllegalArgumentException("Character is not a hex-character, it was: " + c + ".");
		}
	}
	
	/**
	 * Generates a hexadecimal character from given byte
	 * @param b byte from which to generate hexadecimal character
	 * @return hexadecimal character generated from given byte
	 * @throws IllegalArgumentException if the value of the given byte is greater than 15 or less than 0
	 */
	private static char generateCharFromHex(byte b) {
		if (b >= 0 && b <= 9) {
			return (char) (b + 48); // numbers start at 48 in asci
		}
		
		switch(b) {
			case 10: return 'a';
			case 11: return 'b';
			case 12: return 'c';
			case 13: return 'd';
			case 14: return 'e';
			case 15: return 'f';
			default: throw new IllegalArgumentException("Byte could not be converted into a hex-character, it was: " + b + ".");
		}
	}
}
