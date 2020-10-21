package hr.fer.oprpp1.hw02.prob1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.hw02.prob1.Lexer;
import hr.fer.oprpp1.hw02.prob1.Token;
import hr.fer.oprpp1.hw02.prob1.TokenType;

public class Prob1Test {

	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testNullInput() {
		// must throw!
		assertThrows(NullPointerException.class, () -> new Lexer(null));
	}

	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		Lexer lexer = new Lexer("");
		
		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	@Test
	public void testRadAfterEOF() {
		Lexer lexer = new Lexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void testNoActualContent() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("   \r\n\t    ");
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had no content. Lexer should generated only EOF token.");
	}

	@Test
	public void testTwoWords() {
		// Lets check for several words...
		Lexer lexer = new Lexer("  Štefanija\r\n\t Automobil   ");

		// We expect the following stream of tokens
		Token correctData[] = {
			new Token(TokenType.WORD, "Štefanija"),
			new Token(TokenType.WORD, "Automobil"),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testWordStartingWithEscape() {
		Lexer lexer = new Lexer("  \\1st  \r\n\t   ");

		// We expect the following stream of tokens
		Token correctData[] = {
			new Token(TokenType.WORD, "1st"),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testInvalidEscapeEnding() {
		Lexer lexer = new Lexer("   \\");  // this is three spaces and a single backslash -- 4 letters string

		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	public void testInvalidEscape() {
		Lexer lexer = new Lexer("   \\a    ");

		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	public void testSingleEscapedDigit() {
		Lexer lexer = new Lexer("  \\1  ");

		// We expect the following stream of tokens
		Token correctData[] = {
			new Token(TokenType.WORD, "1"),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testWordWithManyEscapes() {
		// Lets check for several words...
		Lexer lexer = new Lexer("  ab\\1\\2cd\\3 ab\\2\\1cd\\4\\\\ \r\n\t   ");

		// We expect the following stream of tokens
		Token correctData[] = {
			new Token(TokenType.WORD, "ab12cd3"),
			new Token(TokenType.WORD, "ab21cd4\\"), // this is 8-letter long, not nine! Only single backslash!
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testTwoNumbers() {
		// Lets check for several numbers...
		Lexer lexer = new Lexer("  1234\r\n\t 5678   ");

		Token correctData[] = {
			new Token(TokenType.NUMBER, Long.valueOf(1234)),
			new Token(TokenType.NUMBER, Long.valueOf(5678)),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testTooBigNumber() {
		Lexer lexer = new Lexer("  12345678912123123432123   ");
		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	@Test
	public void testWordWithManyEscapesAndNumbers() {
		// Lets check for several words...
		Lexer lexer = new Lexer("  ab\\123cd ab\\2\\1cd\\4\\\\ \r\n\t   ");

		// We expect following stream of tokens
		Token correctData[] = {
			new Token(TokenType.WORD, "ab1"),
			new Token(TokenType.NUMBER, Long.valueOf(23)),
			new Token(TokenType.WORD, "cd"),
			new Token(TokenType.WORD, "ab21cd4\\"), // this is 8-letter long, not nine! Only single backslash!
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testSomeSymbols() {
		// Lets check for several symbols...
		Lexer lexer = new Lexer("  -.? \r\n\t ##   ");

		Token correctData[] = {
			new Token(TokenType.SYMBOL, Character.valueOf('-')),
			new Token(TokenType.SYMBOL, Character.valueOf('.')),
			new Token(TokenType.SYMBOL, Character.valueOf('?')),
			new Token(TokenType.SYMBOL, Character.valueOf('#')),
			new Token(TokenType.SYMBOL, Character.valueOf('#')),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testCombinedInput() {
		// Lets check for several symbols...
		Lexer lexer = new Lexer("Janko 3! Jasmina 5; -24");

		Token correctData[] = {
			new Token(TokenType.WORD, "Janko"),
			new Token(TokenType.NUMBER, Long.valueOf(3)),
			new Token(TokenType.SYMBOL, Character.valueOf('!')),
			new Token(TokenType.WORD, "Jasmina"),
			new Token(TokenType.NUMBER, Long.valueOf(5)),
			new Token(TokenType.SYMBOL, Character.valueOf(';')),
			new Token(TokenType.SYMBOL, Character.valueOf('-')),
			new Token(TokenType.NUMBER, Long.valueOf(24)),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	// Helper method for checking if lexer generates the same stream of tokens
	// as the given stream.
	private void checkTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}

	// ----------------------------------------------------------------------------------------------------------
	// --------------------- Second part of tests; uncomment when everything above works ------------------------
	// ----------------------------------------------------------------------------------------------------------


	@Test
	public void testNullState() {
		assertThrows(NullPointerException.class, () -> new Lexer("").setState(null));
	}
	
	@Test
	public void testNotNullInExtended() {
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.EXTENDED);
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testEmptyInExtended() {
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.EXTENDED);
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}

	@Test
	public void testGetReturnsLastNextInExtended() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.EXTENDED);
		
		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	@Test
	public void testRadAfterEOFInExtended() {
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.EXTENDED);

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void testNoActualContentInExtended() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("   \r\n\t    ");
		lexer.setState(LexerState.EXTENDED);
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had no content. Lexer should generated only EOF token.");
	}
	
	@Test
	public void testMultipartInput() {
		// Test input which has parts which are tokenized by different rules...
		Lexer lexer = new Lexer("Janko 3# Ivana26\\a 463abc#zzz");

		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Janko"));
		checkToken(lexer.nextToken(), new Token(TokenType.NUMBER, Long.valueOf(3)));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('#')));
		
		lexer.setState(LexerState.EXTENDED);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ivana26\\a"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "463abc"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('#')));
		
		lexer.setState(LexerState.BASIC);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "zzz"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
		
	}
	
	private void checkToken(Token actual, Token expected) {
			String msg = "Token are not equal.";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
	}

	
	// Dodano prema uputama iz extra.txt
	private String readExample(int n) {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
			if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
		    byte[] data = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt").readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		} catch(IOException ex) {
			throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		}
	}
	
	@Test
	public void testExample1() {
		String text = readExample(1);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "sve"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "jedan"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "text"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "node"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample2() {
		String text = readExample(2);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "sve"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "jedan"));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "text"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "node"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample3() {
		String text = readExample(3);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "sve"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "jedan"));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "text"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "node"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample4() {
		String text = readExample(4);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "se"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ruši"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "s"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "iznimkom"));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "n"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "jer"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "escape"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ilegalan"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ovdje"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('.')));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample5() {
		String text = readExample(5);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "se"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ruši"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "s"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "iznimkom"));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "n"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "jer"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "escape"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ilegalan"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ovdje"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('.')));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample6() {
		String text = readExample(6);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "OK"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('=')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "String"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ide"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "u"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "više"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "redaka"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "čak"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "tri"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('}')));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample7() {
		String text = readExample(7);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "je"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "isto"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "OK"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('=')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "String"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ide"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "u"));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "nredaka"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ovdje"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "a"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "stvarno"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "četiri"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('}')));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void testExample8() {
		String text = readExample(8);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "se"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ruši"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('=')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "String"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ide"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "u"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "više"));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "redaka"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "čak"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "tri"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('\"')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('}')));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	
	@Test
	public void testExample9() {
		String text = readExample(9);
		Lexer lexer = new Lexer(text);
		
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "Ovo"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "se"));
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "ruši"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('{')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('=')));
		assertThrows(LexerException.class, () -> lexer.nextToken());
		checkToken(lexer.nextToken(), new Token(TokenType.WORD, "n"));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('$')));
		checkToken(lexer.nextToken(), new Token(TokenType.SYMBOL, Character.valueOf('}')));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}

}
