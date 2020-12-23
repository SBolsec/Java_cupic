package hr.fer.oprpp1.hw08.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

/**
 * This class provides utility functions.
 * @author sbolsec
 *
 */
public class Util {
	
	/**
	 * Returns icon from provided path or null
	 * @param name path of image
	 * @return <code>ImageIcon</code> created from provided path
	 */
	public ImageIcon getIcon(String name) {
		InputStream is = this.getClass().getResourceAsStream(name);
		if (is == null)
			return null;
		
		byte[] bytes;
		try {
			bytes = is.readAllBytes();
			is.close();
			return new ImageIcon(bytes);
		} catch (IOException e) {
			return null;
		}
	}
}
