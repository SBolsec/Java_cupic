package hr.fer.oprpp1.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import org.junit.jupiter.api.Test;

public class SmartScriptParserTest {

    @Test
    public void testEmpty() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(""));
    }

    @Test
    public void testNullInput() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(null));
    }

    @Test
    public void testBlankTextNode() {
        String text = "  \r  \n  ";
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected = new TextNode("  \r  \n  ");
        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected, documentNode.getChild(0));
    }

    @Test
    public void testTextNode() {
        String text = " This is an example. \\{$ This is fine \\\\";
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected = new TextNode(" This is an example. \\{$ This is fine \\\\");

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected, documentNode.getChild(0));
    }

    @Test
    public void testTextNodeInvalid() {
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(" \\a "));
    }

    @Test
    public void testForNodeNoChildren() {
        String text = "{$ FOR i -1 10 1 $}{$ END $}";
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected1 = new ForLoopNode(
                new ElementVariable("i"),
                new ElementConstantInteger(-1),
                new ElementConstantInteger(10),
                new ElementConstantInteger(1));

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected1, documentNode.getChild(0));
        assertEquals(0, documentNode.getChild(0).numberOfChildren());
    }

    @Test
    public void testForNode1() {
        String text = "{$ FOR i -1 10 1 $} {$ END $}";
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected1 = new ForLoopNode(
                new ElementVariable("i"),
                new ElementConstantInteger(-1),
                new ElementConstantInteger(10),
                new ElementConstantInteger(1));
        Node expected2 = new TextNode(" ");
        expected1.addChildNode(expected2);

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected1, documentNode.getChild(0));
        assertEquals(1, documentNode.getChild(0).numberOfChildren());
        assertEquals(expected2, documentNode.getChild(0).getChild(0));
    }

    @Test
    public void testForNode2() {
        String text = "{$    FOR  sco_re   \"-1\"10 \"1\" $} {$ END $}";
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected1 = new ForLoopNode(
                new ElementVariable("sco_re"),
                new ElementConstantInteger(-1),
                new ElementConstantInteger(10),
                new ElementConstantInteger(1));
        Node expected2 = new TextNode(" ");
        expected1.addChildNode(expected2);

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected1, documentNode.getChild(0));
        assertEquals(1, documentNode.getChild(0).numberOfChildren());
        assertEquals(expected2, documentNode.getChild(0).getChild(0));
    }

    @Test
    public void testForNode3() {
        String text = "{$ FOR year 1 last_year $} {$ END $}";
        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected1 = new ForLoopNode(
                new ElementVariable("year"),
                new ElementConstantInteger(1),
                new ElementVariable("last_year"),
                null);
        Node expected2 = new TextNode(" ");
        expected1.addChildNode(expected2);

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected1, documentNode.getChild(0));
        assertEquals(1, documentNode.getChild(0).numberOfChildren());
        assertEquals(expected2, documentNode.getChild(0).getChild(0));
    }

    @Test
    public void testForNodeInvalidVarialbeName1() {
        String text = "{$ FOR 3 1 10 1 $} {$ END $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testForNodeInvalidVariableName2() {
        String text = "{$ FOR * \"1\" -10 \"1\" $} {$ END $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testForNodeFunctionArgument() {
        String text = "{$ FOR year @sin 10 $} {$ END $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testForNodeTooManyArguments() {
        String text = "{$ FOR year 1 10 \"1\" 10 $} {$ END $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testForNodeTooFewArguments() {
        String text = "{$ FOR year $} {$ END $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testForTagShouldBeTheSame() {
        String text1 = "{$ foR i-1.35bbb\"1\" $} {$ EnD $}";
        String text2 = "{$ For i -1.35 bbb \"1\" $} {$ end $}";

        SmartScriptParser parser1 = new SmartScriptParser(text1);
        SmartScriptParser parser2 = new SmartScriptParser(text2);

        DocumentNode documentNode1 = parser1.getDocumentNode();
        DocumentNode documentNode2 = parser2.getDocumentNode();

        assertEquals(documentNode1.getChild(0), documentNode2.getChild(0));
    }

    @Test
    public void testTooManyEndTags() {
        String text = "{$ FOR year 1 last_year $} {$ END $} {$ END $}";

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testNotEnoughEndTags() {
        String text = "{$ FOR year 1 last_year $}";

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testEchoTag() {
        String text = "{$= i i * @sin word \"Test \\\\ \" 10 1.35 $}";

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Element[] elements = {
                new ElementVariable("i"),
                new ElementVariable("i"),
                new ElementOperator("*"),
                new ElementFunction("sin"),
                new ElementVariable("word"),
                new ElementString("\"Test \\\\ \""),
                new ElementConstantInteger(10),
                new ElementConstantDouble(1.35)
        };
        Node expected = new EchoNode(elements);
        assertEquals(expected, documentNode.getChild(0));
    }

    @Test void testEchoTagFailString() {
        String text = "{$ = \" a \\a \" $}";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test void testEchoTagFailNeverClosed() {
        String text = "{$ = i * ";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testExampleFromHomework() {
        String text = readExample(0);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        assertEquals(4, documentNode.numberOfChildren());
        assertEquals(TextNode.class, documentNode.getChild(0).getClass());
        assertEquals(ForLoopNode.class, documentNode.getChild(1).getClass());
        assertEquals(TextNode.class, documentNode.getChild(2).getClass());
        assertEquals(ForLoopNode.class, documentNode.getChild(3).getClass());

        String expected = "This is sample text.\n" +
                "{$ FOR i 1 10 1 $}\n" +
                "   This is {$= i $}-th time this message is generated.\n" +
                "{$ END $}\n" +
                "{$ FOR i 0 10 2 $}\n" +
                "  sin({$= i $}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" +
                "{$ END $}";
        assertEquals(expected, documentNode.toString());
    }

    @Test
    public void testExampleFromHomeworkRunAgain() {
        String text = readExample(0);

        SmartScriptParser parser1 = new SmartScriptParser(text);
        DocumentNode documentNode1 = parser1.getDocumentNode();

        SmartScriptParser parser2 = new SmartScriptParser(documentNode1.toString());
        DocumentNode documentNode2 = parser2.getDocumentNode();

        boolean same = documentNode1.equals(documentNode2);
        if (!same)
            fail();
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

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected = new TextNode("Ovo je \nsve jedan text node\n");

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected, documentNode.getChild(0));
    }

    @Test
    public void testExample2() {
        String text = readExample(2);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected = new TextNode("Ovo je \nsve jedan \\{$ text node\n");

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected, documentNode.getChild(0));
    }

    @Test
    public void testExample3() {
        String text = readExample(3);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected = new TextNode("Ovo je \nsve jedan \\\\\\{$text node\n");

        assertEquals(1, documentNode.numberOfChildren());
        assertEquals(expected, documentNode.getChild(0));
    }

    @Test
    public void testExample4() {
        String text = readExample(4);

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testExample5() {
        String text = readExample(5);

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testExample6() {
        String text = readExample(6);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected1 = new TextNode("Ovo je OK ");
        Element[] elements = {
            new ElementString("\"String ide\nu više redaka\nčak tri\"")
        };
        Node expected2 = new EchoNode(elements);
        Node expected3 = new TextNode("\n");

        assertEquals(3, documentNode.numberOfChildren());
        assertEquals(expected1, documentNode.getChild(0));
        assertEquals(expected2, documentNode.getChild(1));
        assertEquals(expected3, documentNode.getChild(2));
    }

    @Test
    public void testExample7() {
        String text = readExample(7);

        SmartScriptParser parser = new SmartScriptParser(text);
        DocumentNode documentNode = parser.getDocumentNode();

        Node expected1 = new TextNode("Ovo je isto OK ");
        Element[] elements = {
                new ElementString("\"String ide\n" +
                        "u \\\"više\\\" \nredaka\n" +
                        "ovdje a stvarno četiri\"")
        };
        Node expected2 = new EchoNode(elements);
        Node expected3 = new TextNode("\n");

        assertEquals(3, documentNode.numberOfChildren());
        assertEquals(expected1, documentNode.getChild(0));
        assertEquals(expected2, documentNode.getChild(1));
        assertEquals(expected3, documentNode.getChild(2));
    }

    @Test
    public void testExample8() {
        String text = readExample(8);

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }

    @Test
    public void testExample9() {
        String text = readExample(9);

        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
    }
}
