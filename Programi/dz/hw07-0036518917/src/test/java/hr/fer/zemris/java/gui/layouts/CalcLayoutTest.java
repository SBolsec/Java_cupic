package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

public class CalcLayoutTest {
	
	private static final double EPSILON = 10E-6;

	@Test
	public void test1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(10,30));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(20,15));
		p.add(l1, new RCPosition(2, 2));
		p.add(l2, new RCPosition(3, 3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.getWidth(), EPSILON);
		assertEquals(158, dim.getHeight(), EPSILON);
	}
	
	@Test
	public void test2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(108,15));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(16,30));
		p.add(l1, new RCPosition(1,1));
		p.add(l2, new RCPosition(3, 3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.getWidth(), EPSILON);
		assertEquals(158, dim.getHeight(), EPSILON);
	}
}
