package hr.fer.oprpp1.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ComplexNumberTest {
	
	private static final double EPSILON = 10E-6; 

    @Test
    public void testConstructor() {
        ComplexNumber a = new ComplexNumber(1,2);
        assertEquals(1, a.getReal(), EPSILON);
        assertEquals(2, a.getImaginary(), EPSILON);
    }

    @Test
    public void testGetReal() {
        ComplexNumber a = new ComplexNumber(5.2,3);
        assertEquals(5.2, a.getReal(), EPSILON);
    }

    @Test
    public void testGetImaginary() {
        ComplexNumber a = new ComplexNumber(5.2,3);
        assertEquals(3, a.getImaginary(), EPSILON);
    }

    @Test
    public void testGetMagnitude() {
        ComplexNumber a = new ComplexNumber(6,8);
        assertEquals(10, a.getMagnitude(), EPSILON);
    }

    @Test
    public void testGetAngleCase1() {
        ComplexNumber a = new ComplexNumber(1,1);
        assertEquals(0.785398163, a.getAngle(), EPSILON);
    }

    @Test
    public void testGetAngleCase2() {
        ComplexNumber a = new ComplexNumber(-1,1);
        assertEquals(2.35619449, a.getAngle(), EPSILON);
    }

    @Test
    public void testGetAngleCase3() {
        ComplexNumber a = new ComplexNumber(-1,-1);
        assertEquals(-2.356194490192345, a.getAngle(), EPSILON);
    }

    @Test
    public void testGetAngleCase4() {
        ComplexNumber a = new ComplexNumber(1,-1);
        assertEquals(-0.7853981633974483, a.getAngle(), EPSILON);
    }

    @Test
    public void testFromReal() {
        ComplexNumber a = ComplexNumber.fromReal(1);
        assertEquals(1, a.getReal(), EPSILON);
        assertEquals(0, a.getImaginary(), EPSILON);
    }

    @Test
    public void testFromImaginary() {
        ComplexNumber a = ComplexNumber.fromImaginary(1);
        assertEquals(0, a.getReal(), EPSILON);
        assertEquals(1, a.getImaginary(), EPSILON);
    }

    @Test
    public void testFromMagnitudeAndAngle() {
        ComplexNumber a = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2),0.785398163);
        assertEquals(1, a.getReal(), EPSILON);
        assertEquals(1, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase1() {
        ComplexNumber a = ComplexNumber.parse("3.51");
        assertEquals(3.51, a.getReal(), EPSILON);
        assertEquals(0, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase2() {
        ComplexNumber a = ComplexNumber.parse("-3.17");
        assertEquals(-3.17, a.getReal(), EPSILON);
        assertEquals(0, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase3() {
        ComplexNumber a = ComplexNumber.parse("-2.71i");
        assertEquals(0, a.getReal(), EPSILON);
    }

    @Test
    public void testParseCase4() {
        ComplexNumber a = ComplexNumber.parse("i");
        assertEquals(0, a.getReal(), EPSILON);
        assertEquals(1, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase5() {
        ComplexNumber a = ComplexNumber.parse("-2.71-3.15i");
        assertEquals(-2.71, a.getReal(), EPSILON);
        assertEquals(-3.15, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase6() {
        ComplexNumber a = ComplexNumber.parse("+3.51i-2.17");
        assertEquals(-2.17, a.getReal(), EPSILON);
        assertEquals(3.51, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase7() {
        ComplexNumber a = ComplexNumber.parse("-1-i");
        assertEquals(-1, a.getReal(), EPSILON);
        assertEquals(-1, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase8() {
        ComplexNumber a = ComplexNumber.parse("-i+1");
        assertEquals(1, a.getReal(), EPSILON);
        assertEquals(-1, a.getImaginary(), EPSILON);
    }

    @Test
    public void testParseCase9() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-1+i+"));
    }

    @Test
    public void testParseCase10() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("i2"));
    }

    @Test
    public void testParseCase11() {
        assertThrows(IllegalArgumentException.class, () -> ComplexNumber.parse("-2.71+-3.15i"));
    }


    @Test
    public void testAdd() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.add(b);

        assertEquals(6, c.getReal(), EPSILON);
        assertEquals(-2, c.getImaginary(), EPSILON);
    }

    @Test
    public void testSub() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.sub(b);

        assertEquals(-4, c.getReal(), EPSILON);
        assertEquals(4, c.getImaginary(), EPSILON);
    }

    @Test
    public void testMul() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.mul(b);

        assertEquals(8, c.getReal(), EPSILON);
        assertEquals(2, c.getImaginary(), EPSILON);
    }

    @Test
    public void testDiv() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.div(b);

        assertEquals(0.0588235, c.getReal(), EPSILON);
        assertEquals(0.2352941, c.getImaginary(), EPSILON);
    }

    @Test
    public void testPower() {
        ComplexNumber a = new ComplexNumber(5,-3);
        ComplexNumber b = a.power(3);

        assertEquals(-10, b.getReal(), EPSILON);
        assertEquals(-198, b.getImaginary(), EPSILON);
    }

    @Test
    public void testPowerLessThanZero() {
        ComplexNumber a = new ComplexNumber(5,-3);

        assertThrows(IllegalArgumentException.class, () -> a.power(-2));
    }

    @Test
    public void testRoot() {
        ComplexNumber a = new ComplexNumber(5,-3);
        ComplexNumber[] roots = a.root(2);

        assertEquals(2.327117519, roots[0].getReal(), EPSILON);
        assertEquals(-0.6445742372, roots[0].getImaginary(), EPSILON);
        assertEquals(-2.327117519, roots[1].getReal(), EPSILON);
        assertEquals(0.6445742372, roots[1].getImaginary(), EPSILON);
    }

    @Test
    public void testRootLessThanZero() {
        ComplexNumber a = new ComplexNumber(5,-3);

        assertThrows(IllegalArgumentException.class, () -> a.root(-2));
    }

    @Test
    public void testToString() {
        ComplexNumber a = new ComplexNumber(5,-3);

        assertEquals("(5.000000 - 3.000000i)", a.toString());
    }
}
