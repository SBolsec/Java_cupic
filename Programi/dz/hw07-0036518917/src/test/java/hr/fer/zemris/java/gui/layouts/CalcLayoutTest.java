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
	
	@Test
	public void testRowTooSmall1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(-2, 2)));
	}
	
	@Test
	public void testRowTooSmall2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(0, 2)));
	}
	
	@Test
	public void testRowTooBig1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(6, 2)));
	}
	
	@Test
	public void testRowTooBig2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(9, 1)));
	}
	
	@Test
	public void testColumnTooSmall1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(1, -2)));
	}
	
	@Test
	public void testColumnTooSmall2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(5, 0)));
	}
	
	@Test
	public void testColumnTooBig1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(2, 8)));
	}
	
	@Test
	public void testColumnTooBig2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(4, 15)));
	}
	
	@Test
	public void testFirstRow() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l = new JLabel("");
		
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(1, 2)));
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(1, 3)));
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(1, 4)));
		assertThrows(CalcLayoutException.class, () -> p.add(l, new RCPosition(1, 5)));
	}
	
	@Test
	public void testAddSecondConstraintToComponent() {
		JPanel p = new JPanel(new CalcLayout(2));
		RCPosition pos = new RCPosition(2, 2);
		JLabel l1 = new JLabel("1");
		JLabel l2 = new JLabel("2");
		
		p.add(l1, pos);
		
		assertThrows(CalcLayoutException.class, () -> p.add(l2, pos));
	}
}
