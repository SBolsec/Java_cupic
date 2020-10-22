package hr.fer.oprpp1.hw02;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * This class tests the parser.
 * @author sbolsec
 *
 */
public class SmartScriptTester {
	public static void main(String[] args) throws IOException {
//		if (args.length != 1) {
//			throw new IllegalArgumentException("There must be one input, there was: " + args.length + ".");
//		}
//		String filepath = args[0];
//		String docBody = new String(
//				Files.readAllBytes(Paths.get(filepath)),
//				StandardCharsets.UTF_8);
		SmartScriptTester tester = new SmartScriptTester();
		
		String docBody = tester.readExample(0);

		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		boolean same = document.equals(document2);
		System.out.println(same);
	}
	
	private String readExample(int n) {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
			if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
		    byte[] data = is.readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		} catch(IOException ex) {
			throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		}
	}
}
