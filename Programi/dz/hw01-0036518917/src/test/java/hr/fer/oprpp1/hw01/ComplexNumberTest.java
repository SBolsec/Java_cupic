package hr.fer.oprpp1.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ComplexNumberTest {

    @Test
    public void testConstructor() {
        ComplexNumber a = new ComplexNumber(1,2);
        if (!(Math.abs(a.getReal()-1) < 10E-6)) fail("Test failed");
        if (!(Math.abs(a.getImaginary()-2) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testGetReal() {
        ComplexNumber a = new ComplexNumber(5.2,3);
        if (!(Math.abs(a.getReal()-5.2) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testGetImaginary() {
        ComplexNumber a = new ComplexNumber(5.2,3);
        if (!(Math.abs(a.getImaginary()-3) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testGetMagnitude() {
        ComplexNumber a = new ComplexNumber(6,8);
        if (!(Math.abs(a.getMagnitude()-10) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testGetAngleCase1() {
        ComplexNumber a = new ComplexNumber(1,1);
        if (!(Math.abs(a.getAngle()-0.785398163) < 10E-6)) fail("Test failed for input '1+i'");
    }

    @Test
    public void testGetAngleCase2() {
        ComplexNumber a = new ComplexNumber(-1,1);
        if (!(Math.abs(a.getAngle()-2.35619449) < 10E-6)) fail("Test failed for input '-1+i'");
    }

    @Test
    public void testGetAngleCase3() {
        ComplexNumber a = new ComplexNumber(-1,-1);
        if (!(Math.abs(a.getAngle()-3.926990817) < 10E-6)) fail("Test failed for input '-1-i'");
    }

    @Test
    public void testGetAngleCase4() {
        ComplexNumber a = new ComplexNumber(1,-1);
        if (!(Math.abs(a.getAngle()-5.497787144) < 10E-6)) fail("Test failed for input '1-i'");
    }

    @Test
    public void testFromReal() {
        ComplexNumber a = ComplexNumber.fromReal(1);
        if (!(Math.abs(a.getReal()-1) < 10E-6)) fail("Test failed");
        if (!(Math.abs(a.getImaginary()-0) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testFromImaginary() {
        ComplexNumber a = ComplexNumber.fromImaginary(1);
        if (!(Math.abs(a.getReal()-0) < 10E-6)) fail("Test failed");
        if (!(Math.abs(a.getImaginary()-1) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testFromMagnitudeAndAngle() {
        ComplexNumber a = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2),0.785398163);
        if (!(Math.abs(a.getReal()-1) < 10E-6)) fail("Test failed");
        if (!(Math.abs(a.getImaginary()-1) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testParseCase1() {
        ComplexNumber a = ComplexNumber.parse("3.51");
        if (!(Math.abs(a.getReal()-3.51) < 10E-6)) fail("Test failed for input '3.51'");
        if (!(Math.abs(a.getImaginary()-0) < 10E-6)) fail("Test failed for input '3.51'");
    }

    @Test
    public void testParseCase2() {
        ComplexNumber a = ComplexNumber.parse("-3.17");
        if (!(Math.abs(a.getReal()+3.17) < 10E-6)) fail("Test failed for input '-3.17'");
        if (!(Math.abs(a.getImaginary()-0) < 10E-6)) fail("Test failed for input '-3.17'");
    }

    @Test
    public void testParseCase3() {
        ComplexNumber a = ComplexNumber.parse("-2.71i");
        if (!(Math.abs(a.getReal()-0) < 10E-6)) fail("Test failed for input '-2.71i'");
        if (!(Math.abs(a.getImaginary()+2.71) < 10E-6)) fail("Test failed for input '-2.71i'");
    }

    @Test
    public void testParseCase4() {
        ComplexNumber a = ComplexNumber.parse("i");
        if (!(Math.abs(a.getReal()-0) < 10E-6)) fail("Test failed for input 'i'");
        if (!(Math.abs(a.getImaginary()-1) < 10E-6)) fail("Test failed for input 'i'");
    }

    @Test
    public void testParseCase5() {
        ComplexNumber a = ComplexNumber.parse("-2.71-3.15i");
        if (!(Math.abs(a.getReal()+2.71) < 10E-6)) fail("Test failed for input '-2.71-3.15i'");
        if (!(Math.abs(a.getImaginary()+3.15) < 10E-6)) fail("Test failed for input '-2.71-3.15i'");
    }

    @Test
    public void testParseCase6() {
        ComplexNumber a = ComplexNumber.parse("+3.51i-2.17");
        if (!(Math.abs(a.getReal()+2.17) < 10E-6)) fail("Test failed for input '+3.51i-2.17'");
        if (!(Math.abs(a.getImaginary()-3.51) < 10E-6)) fail("Test failed for input '+3.51i-2.17'");
    }

    @Test
    public void testParseCase7() {
        ComplexNumber a = ComplexNumber.parse("-1-i");
        if (!(Math.abs(a.getReal()+1) < 10E-6)) fail("Test failed for input '-1-i'");
        if (!(Math.abs(a.getImaginary()+1) < 10E-6)) fail("Test failed for input '-1-i'");
    }

    @Test
    public void testParseCase8() {
        ComplexNumber a = ComplexNumber.parse("-i+1");
        if (!(Math.abs(a.getReal()-1) < 10E-6)) fail("Test failed for input '-i+1'");
        if (!(Math.abs(a.getImaginary()+1) < 10E-6)) fail("Test failed for input '-i+1'");
    }

    @Test
    public void testAdd() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.add(b);

        if (!(Math.abs(c.getReal()-6) < 10E-6)) fail("Test failed");
        if (!(Math.abs(c.getImaginary()+2) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testSub() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.sub(b);

        if (!(Math.abs(c.getReal()+4) < 10E-6)) fail("Test failed");
        if (!(Math.abs(c.getImaginary()-4) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testMul() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.mul(b);

        if (!(Math.abs(c.getReal()-8) < 10E-6)) fail("Test failed");
        if (!(Math.abs(c.getImaginary()-2) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testDiv() {
        ComplexNumber a = new ComplexNumber(1,1);
        ComplexNumber b = new ComplexNumber(5,-3);
        ComplexNumber c = a.div(b);

        if (!(Math.abs(c.getReal()-0.0588235) < 10E-6)) fail("Test failed");
        if (!(Math.abs(c.getImaginary()-0.2352941) < 10E-6)) fail("Test failed");
    }

    @Test
    public void testPower() {
        ComplexNumber a = new ComplexNumber(5,-3);
        ComplexNumber b = a.power(3);

        if (!(Math.abs(b.getReal()+10) < 10E-6)) fail("Test failed");
        if (!(Math.abs(b.getImaginary()+198) < 10E-6)) fail("Test failed");
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

        if (!(Math.abs(roots[0].getReal()+2.327117519) < 10E-4)) fail("Test failed for input '5-3i'");
        if (!(Math.abs(roots[0].getImaginary()-0.6445742372) < 10E-4)) fail("Test failed for input '5-3i'");
        if (!(Math.abs(roots[1].getReal()-2.327117519) < 10E-4)) fail("Test failed for input '5-3i'");
        if (!(Math.abs(roots[1].getImaginary()+0.6445742372) < 10E-4)) fail("Test failed for input '5-3i'");
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
