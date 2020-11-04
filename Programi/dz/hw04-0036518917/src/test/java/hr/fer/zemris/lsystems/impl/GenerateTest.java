package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;


public class GenerateTest {
	
	@Test
	public void testGenerateLevel0() {
		LSystemBuilder builder = new LSystemBuilderImpl();
		builder.setAxiom("F");
		builder.registerProduction('F', "F+F--F+F");
		LSystem system = builder.build();
		
		String expected = "F";
		String actual = system.generate(0);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGenerateLevel1() {
		LSystemBuilder builder = new LSystemBuilderImpl();
		builder.setAxiom("F");
		builder.registerProduction('F', "F+F--F+F");
		LSystem system = builder.build();
		
		String expected = "F+F--F+F";
		String actual = system.generate(1);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGenerateLevel2() {
		LSystemBuilder builder = new LSystemBuilderImpl();
		builder.setAxiom("F");
		builder.registerProduction('F', "F+F--F+F");
		LSystem system = builder.build();
		
		String expected = "F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F";
		String actual = system.generate(2);
		
		assertEquals(expected, actual);
	}
}
