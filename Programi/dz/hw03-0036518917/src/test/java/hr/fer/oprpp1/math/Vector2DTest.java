package hr.fer.oprpp1.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Vector2DTest {

	@Test
	public void testConstructor() {
		Vector2D vector = new Vector2D(1, 2);
		
		assertEquals(1, vector.getX());
		assertEquals(2, vector.getY());
	}
	
	@Test
	public void testGetX() {
		Vector2D vector = new Vector2D(1, 2);
		
		assertEquals(1, vector.getX());
	}
	
	@Test
	public void testGetY() {
		Vector2D vector = new Vector2D(1, 2);
		
		assertEquals(2, vector.getY());
	}
	
	@Test
	public void testAdd() {
		Vector2D vector1 = new Vector2D(1, 2);
		Vector2D vector2 = new Vector2D(5, 1);
		
		vector1.add(vector2);
		
		assertEquals(6, vector1.getX());
		assertEquals(3, vector1.getY());
	}
	
	@Test
	public void testAddNull() {
		Vector2D vector1 = new Vector2D(1, 2);
		
		assertThrows(NullPointerException.class, () -> vector1.add(null));
	}
	
	@Test
	public void testAdded() {
		Vector2D vector1 = new Vector2D(1, 2);
		Vector2D vector2 = new Vector2D(5, 1);
		
		Vector2D vector3 = vector1.added(vector2);
		
		assertEquals(6, vector3.getX());
		assertEquals(3, vector3.getY());
		
		// vector1 can not change
		assertEquals(1, vector1.getX());
		assertEquals(2, vector1.getY());
	}
	
	@Test
	public void testAddedNull() {
		Vector2D vector1 = new Vector2D(1, 2);
		
		assertThrows(NullPointerException.class, () -> vector1.added(null));
	}
	
	@Test
	public void testRotate() {
		Vector2D vector = new Vector2D(1, 2);
		
		vector.rotate(1.4);
		
		assertEquals(true, Math.abs(vector.getX() + 1.8009323170766793) < 10E-6);
		assertEquals(true, Math.abs(vector.getY() - 1.3253840157889423) < 10E-6);
	}
	
	@Test
	public void testRotated() {
		Vector2D vector = new Vector2D(1, 2);
		
		Vector2D res = vector.rotated(1.4);
		
		assertEquals(true, Math.abs(res.getX() + 1.8009323170766793) < 10E-6);
		assertEquals(true, Math.abs(res.getY() - 1.3253840157889423) < 10E-6);
	}
	
	@Test
	public void testScale() {
		Vector2D vector = new Vector2D(1, 2);
		
		vector.scale(2);
		
		assertEquals(2, vector.getX());
		assertEquals(4, vector.getY());
	}
	
	@Test
	public void testScaled() {
		Vector2D vector = new Vector2D(1, 2);
		
		Vector2D res = vector.scaled(2);
		
		assertEquals(2, res.getX());
		assertEquals(4, res.getY());
	}
	
	@Test
	public void testCopy() {
		Vector2D vector = new Vector2D(1, 2);
		Vector2D copy = vector.copy();
		
		assertEquals(1, copy.getX());
		assertEquals(2, copy.getY());
	}
}
