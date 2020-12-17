package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class PrimListModelTest {

	private PrimListModel model;
	
	private static PrimListModel newPrimListModel() {
		return new PrimListModel();
	}
	
	@BeforeEach
	public void setup() {
		this.model = newPrimListModel();
	}
	
	@Test
	public void testNewModel() {
		assertEquals(1, model.getSize());
		assertEquals(1, model.getElementAt(0));
	}
	
	@Test
	public void testNext() {
		model.next();
		assertEquals(2, model.getElementAt(1));
		model.next();
		assertEquals(3, model.getElementAt(2));
		model.next();
		assertEquals(5, model.getElementAt(3));
		model.next();
		assertEquals(7, model.getElementAt(4));
		model.next();
		assertEquals(11, model.getElementAt(5));
		model.next();
		assertEquals(13, model.getElementAt(6));
	}
}
