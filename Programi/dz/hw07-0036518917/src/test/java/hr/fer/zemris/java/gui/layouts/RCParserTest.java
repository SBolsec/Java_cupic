package hr.fer.zemris.java.gui.layouts;

import hr.fer.zemris.java.gui.layouts.calcLayoutException.CalcLayoutException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class RCParserTest {

    @Test
    public void ValidParseTest1() {
        Assertions.assertEquals(new RCPosition(1,1), RCPosition.parse("1,1"));
    }

    @Test
    public void ValidParseTest2() {
        Assertions.assertEquals(new RCPosition(-1,11), RCPosition.parse("-1,11"));
    }

    @Test
    public void ValidParseTest3() {
        Assertions.assertEquals(new RCPosition(-1,11), RCPosition.parse("-1 ,       11"));
    }

    @Test
    public void ValidParseTest4() {
        Assertions.assertEquals(new RCPosition(-1,-11), RCPosition.parse("-1 ,       -11"));
    }

    @Test
    public void InvalidArgumentsStringTest2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse(null));
    }

    @Test
    public void InvalidArgumentsStringTest3() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse(""));
    }

    @Test
    public void InvalidArgumentsStringTest4() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("3 3"));
    }

    @Test
    public void InvalidArgumentsStringTest5() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("33"));
    }

    @Test
    public void ElementInvalidStringTest1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("a,3"));
    }

    @Test
    public void ElementElementInvalidStringTest2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("a "));
    }

    @Test
    public void ElementElementInvalidStringTest3() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("a a"));
    }


}
