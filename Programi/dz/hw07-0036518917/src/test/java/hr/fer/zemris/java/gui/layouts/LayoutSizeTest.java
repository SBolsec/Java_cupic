package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class LayoutSizeTest {

    @Test
    public void SizeTest1() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
        p.add(l1, new RCPosition(2,2));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();
        Assertions.assertEquals(152, dim.width);
        Assertions.assertEquals(158, dim.height);
    }

    @Test
    public void SizeTest2() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getPreferredSize();
        Assertions.assertEquals(152, dim.width);
        Assertions.assertEquals(158, dim.height);
    }

    @Test
    public void SizeMinTest3() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setMinimumSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setMinimumSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getMinimumSize();
        Assertions.assertEquals(152, dim.width);
        Assertions.assertEquals(158, dim.height);
    }

    @Test
    public void SizeMaxTest3() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel(""); l1.setMaximumSize(new Dimension(108,15));
        JLabel l2 = new JLabel(""); l2.setMaximumSize(new Dimension(16,30));
        p.add(l1, new RCPosition(1,1));
        p.add(l2, new RCPosition(3,3));
        Dimension dim = p.getMaximumSize();
        Assertions.assertEquals(152, dim.width);
        Assertions.assertEquals(158, dim.height);
    }
}
