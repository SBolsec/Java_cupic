package hr.fer.oprpp1.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

public class SmartScriptLexerTest {

	@Test
	public void testNotNull() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("");
		
		assertNotNull(SmartScriptLexer.nextToken(), "SmartScriptToken was expected but null was returned.");
	}

	@Test
	public void testNullInput() {
		// must throw!
		assertThrows(NullPointerException.class, () -> new SmartScriptLexer(null));
	}

	@Test
	public void testEmpty() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("");
		
		assertEquals(SmartScriptTokenType.EOF, SmartScriptLexer.nextToken().getType(), "Empty input must generate only EOF SmartScriptToken.");
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getSmartScriptToken once or several times after calling nextToken must return each time what nextToken returned...
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("");
		
		SmartScriptToken SmartScriptToken = SmartScriptLexer.nextToken();
		assertEquals(SmartScriptToken, SmartScriptLexer.getToken(), "getSmartScriptToken returned different SmartScriptToken than nextToken.");
		assertEquals(SmartScriptToken, SmartScriptLexer.getToken(), "getSmartScriptToken returned different SmartScriptToken than nextToken.");
	}

	@Test
	public void testRadAfterEOF() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("");

		// will obtain EOF
		SmartScriptLexer.nextToken();
		// will throw!
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testNoActualContent() {
		// When input is only of spaces, tabs, newlines, etc...
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("   \r\n\t    ");
		
		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("   \r\n\t    ")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}

	@Test
	public void testTwoWords() {
		// Lets check for several words...
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("  Štefanija\r\n\t Automobil   ");

		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("  Štefanija\r\n\t Automobil   ")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testTextStartingWithEscape() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("\\\\This is sample text. \r\n");

		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("\\This is sample text. \r\n")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}

	@Test
	public void testEscapeBracket() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("Example { bla } blu \\{$=1$}. Nothing interesting {=here}.");

		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Example { bla } blu {$=1$}. Nothing interesting {=here}.")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testInvalidEscape() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("   \\a    ");

		// will throw!
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testJohnLongSmith() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("\"John\\\"Long\\\" Smith\"");
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("John\"Long\" Smith"))
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testFoundTag() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("Example {$");

		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Example ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testForTag1() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$ FOR i -1 10 1 $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(-1)),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(10)),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(1)),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}

	@Test
	public void testForTag2() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$    FOR  sco_re    \"-1\"10 \"1\" $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("sco_re")),
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("-1")),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(10)),
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("1")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testForTag3() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$ FOR i-1.35bbb\"1\" $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
			new SmartScriptToken(SmartScriptTokenType.DOUBLE, new ElementConstantDouble(-1.35)),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementString("bbb")),
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("1")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testForTag4() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$ FOR i -1.35 bbb \"1\" $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
			new SmartScriptToken(SmartScriptTokenType.DOUBLE, new ElementConstantDouble(-1.35)),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementString("bbb")),
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("1")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testEndTag1() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$END $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("END")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testEndTag2() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$   END $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("END")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testEqualsTag1() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$= i $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementString("i")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testEqualsTag2() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$= i i * @sin \"0.000\" @decfmt $}");
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
					
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementString("i")),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementString("i")),
			new SmartScriptToken(SmartScriptTokenType.OPERATOR, new ElementOperator("*")),
			new SmartScriptToken(SmartScriptTokenType.FUNCTION, new ElementFunction("sin")),
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("0.000")),
			new SmartScriptToken(SmartScriptTokenType.FUNCTION, new ElementFunction("decfmt")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testInvalidTag() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$ WHILE $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};
				
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("WHILE")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null),
		};
			
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testTagShouldThrow1() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$ FOR \\i -1 10 1 $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};	
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR"))
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testTagShouldThrow2() {
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer("{$ FOR \"i -1 10 1 $}");
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null),
		};	
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR"))
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	// Helper method for checking if SmartScriptLexer generates the same stream of SmartScriptTokens
	// as the given stream.
	private void checkSmartScriptTokenStream(SmartScriptLexer SmartScriptLexer, SmartScriptToken[] correctData) {
		int counter = 0;
		for(SmartScriptToken expected : correctData) {
			SmartScriptToken actual = SmartScriptLexer.nextToken();
			String msg = "Checking SmartScriptToken " + counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}
	
	
	// Dodano prema uputama iz extra.txt
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
	
	@Test
	public void testExample1() {
		String text = readExample(1);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo je \nsve jedan text node\n")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testExample2() {
		String text = readExample(2);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo je \nsve jedan {$ text node\n")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testExample3() {
		String text = readExample(3);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		// We expect the following stream of SmartScriptTokens
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo je \nsve jedan \\{$text node\n")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testExample4() {
		String text = readExample(4);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testExample5() {
		String text = readExample(5);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testExample6() {
		String text = readExample(6);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo je OK ${ = \"String ide\nu više redaka\nčak tri\" $}\n")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testExample61() {
		String text = readExample(61);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo je OK ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("String ide\nu više redaka\nčak tri")),
			new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testExample7() {
		String text = readExample(7);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testExample7Tag() {
		String text = readExample(71);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo je isto OK ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("String ide\nu \"više\" \nredaka\novdje a stvarno četiri")),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null),
				new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
	}
	
	@Test
	public void testExample8() {
		String text = readExample(8);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo se ruši ${ = \"String ide\nu više {$ redaka\nčak tri\" $}\n")),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData);
	}
	
	@Test
	public void testExample81() {
		String text = readExample(81);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo se ruši ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS"))
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	
	@Test
	public void testExample9() {
		String text = readExample(9);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	@Test
	public void testExample91() {
		String text = readExample(91);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("Ovo se ruši ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData2[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS"))
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
		
		assertThrows(SmartScriptLexerException.class, () -> SmartScriptLexer.nextToken());
	}
	
	// This is the example from the pdf in hw02
	@Test
	public void testExample0() {
		String text = readExample(0);
		SmartScriptLexer SmartScriptLexer = new SmartScriptLexer(text);
		
		SmartScriptToken correctData1[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("This is sample text.\n")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData1);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		SmartScriptToken correctData2[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR")),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
				new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(1)),
				new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(10)),
				new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(1)),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData2);
		
		SmartScriptLexer.setState(SmartScriptLexerState.BASIC);
		SmartScriptToken correctData3[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("\n   This is ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData3);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		SmartScriptToken correctData4[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData4);
		
		SmartScriptLexer.setState(SmartScriptLexerState.BASIC);
		SmartScriptToken correctData5[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("-th time this message is generated.\n")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData5);
		
		
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		
		SmartScriptToken correctData6[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("END")),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData6);
		
		SmartScriptLexer.setState(SmartScriptLexerState.BASIC);
		SmartScriptToken correctData7[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("\n")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData7);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		SmartScriptToken correctData8[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("FOR")),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
				new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(0)),
				new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(10)),
				new SmartScriptToken(SmartScriptTokenType.INTEGER, new ElementConstantInteger(2)),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData8);
		
		SmartScriptLexer.setState(SmartScriptLexerState.BASIC);
		SmartScriptToken correctData9[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("\n  sin(")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData9);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		SmartScriptToken correctData10[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData10);
		
		SmartScriptLexer.setState(SmartScriptLexerState.BASIC);
		SmartScriptToken correctData11[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("^2) = ")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData11);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		SmartScriptToken correctData12[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("EQUALS")),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, new ElementVariable("i")),
				new SmartScriptToken(SmartScriptTokenType.OPERATOR, new ElementOperator("*")),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, new ElementFunction("sin")),
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementVariable("0.000")),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, new ElementFunction("decfmt")),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData12);
		
		SmartScriptLexer.setState(SmartScriptLexerState.BASIC);
		SmartScriptToken correctData13[] = {
			new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("\n")),
			new SmartScriptToken(SmartScriptTokenType.TAG_OPENED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData13);
		
		SmartScriptLexer.setState(SmartScriptLexerState.TAG);
		SmartScriptToken correctData14[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, new ElementString("END")),
				new SmartScriptToken(SmartScriptTokenType.TAG_CLOSED, null)
		};
		checkSmartScriptTokenStream(SmartScriptLexer, correctData14);
	}

}
