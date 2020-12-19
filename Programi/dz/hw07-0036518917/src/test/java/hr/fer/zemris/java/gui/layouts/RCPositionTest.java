package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class RCPositionTest {

    JPanel p;

    @BeforeEach
    public void setup() {
        p = new JPanel(new CalcLayout(3));
    }

    @Test
    public void InvalidArgumentsTest1() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(0,1)));
    }

    @Test
    public void InvalidArgumentsTest2() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(-2,1)));
    }

    @Test
    public void InvalidArgumentsTest3() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(3,9)));
    }

    @Test
    public void InvalidArgumentsTest4() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(3,-2)));
    }

    @Test
    public void InvalidArgumentsTest5() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), new RCPosition(1,4)));
    }

    @Test
    public void AddedElementInvalidTest() {
        p.add(new JLabel("x"), new RCPosition(3,3));
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("z"), new RCPosition(3,3)));
    }

    @Test
    public void InvalidArgumentsStringTest1() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "0,1"));
    }

    @Test
    public void InvalidArgumentsStringTest2() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "-2,1"));
    }

    @Test
    public void InvalidArgumentsStringTest3() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "3,9"));
    }

    @Test
    public void InvalidArgumentsStringTest4() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "3,-2"));
    }

    @Test
    public void InvalidArgumentsStringTest5() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "1,4"));
    }

    @Test
    public void InvalidArgumentsStringTest6() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "12,4"));
    }

    @Test
    public void InvalidArgumentsStringTest7() {
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("x"), "12a,4"));
    }

    @Test
    public void AddedElementInvalidStringTest() {
        p.add(new JLabel("x"), "3,3");
        Assertions.assertThrows(CalcLayoutException.class, () -> p.add(new JLabel("z"), "3,3"));
    }


}
