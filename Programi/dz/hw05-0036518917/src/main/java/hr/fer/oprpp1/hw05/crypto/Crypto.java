package hr.fer.oprpp1.hw05.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	
	/** Size of the buffer which will be used to read the file **/
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Starting point of the program
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("There were no program arguments!");
			System.exit(1);
		}
		
		try {
			switch(args[0]) {
			case "checksha":
				if (args.length != 2) {
					System.err.println("File not provided!");
					System.exit(1);
				}
				checksha(Paths.get(args[1]));
				break;
			case "encrypt":
				if (args.length != 3) {
					System.err.println("Missing input arguments!");
					System.exit(1);
				}
				encrypt(true, Paths.get(args[1]), Paths.get(args[2]));
				break;
			case "decrypt":
				if (args.length != 3) {
					System.err.println("Missing input arguments!");
					System.exit(1);
				}
				encrypt(false, Paths.get(args[1]), Paths.get(args[2]));
				break;
			default : 
				System.err.println("Unsupported function, supported functions are: checksha, encrypt, decrypt. It was: " + args[0] + ".");
				System.exit(1);
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Checks the SHA-256 digest of the given file
	 * @param path file which SHA-256 will be calculated and tested
	 * @throws NoSuchAlgorithmException if such an algorithm does not exist
	 * @throws IOException if there was a problem while reading the file
	 */
	public static void checksha(Path path) throws NoSuchAlgorithmException, IOException {
		System.out.println("Please provide expected sha-256 digest for " + path.getFileName() + ":");
		System.out.format("> ");
		Scanner sc = new Scanner(System.in);
		String digest = sc.nextLine();
		sc.close();
		
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		byte[] buff = new byte[BUFFER_SIZE];
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(path), BUFFER_SIZE)) {
			while (true) {
				int size = is.read(buff);
				if (size == -1) break;
				sha.update(buff, 0, size);
			}
		}
		
		byte[] result = sha.digest();
		String res = Util.bytetohex(result);
		
		if (digest.equals(res)) {
			System.out.println("Digesting completed. Digest of " + path.getFileName() 
				+ " matches expected digest.");
		} else {
			System.out.println("Digesting completed. Digest of " + path.getFileName()
				+ " does not match the expected digest. Digest was: " + res);
		}
	}
	
	/**
	 * Encrypts or decrypts the given input file and stores the result in the given output file
	 * @param encrypt if true encrypt the given file, otherwise decrypt the given file
	 * @param input input file
	 * @param output output file
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static void encrypt(boolean encrypt, Path input, Path output) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
		System.out.format("> ");
		String password = sc.nextLine();
		System.out.println("Please provide initializaion vector as hex-encoded text (32 hex-digits):");
		System.out.format("> ");
		String initVector = sc.nextLine();
		sc.close();
		
		String keyText = password;
		String ivText = initVector;
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
	
		byte[] buff = new byte[BUFFER_SIZE];
		byte[] result = null;
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(input), BUFFER_SIZE);
				OutputStream os = new BufferedOutputStream(Files.newOutputStream(output), BUFFER_SIZE)) {
			while (true) {
				int size = is.read(buff);
				if (size <= 0) {
					result = cipher.doFinal();
					os.write(result);
					os.flush();
					break;
				}
				result = cipher.update(buff, 0, size);
				os.write(result);
				os.flush();
			}
		}
		
//		try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(output), BUFFER_SIZE)) {
//			for (int i = 0; i < result.length; i++) {
//				os.write(result);
//			}
//			os.flush();
//		}
		
		System.out.println((encrypt ? "Encryption" : "Decryption") + " completed. " 
			+ "Generated file " + output.getFileName() + " based on " + input.getFileName() + ".");
	}
}
