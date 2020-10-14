package hr.fer.oprpp1.hw01.demo;

import hr.fer.oprpp1.hw01.ComplexNumber;

public class ComplexDemo {
    public static void main(String[] args) {
        ComplexNumber c1 = new ComplexNumber(2, 3);
        ComplexNumber c2 = ComplexNumber.parse("2.5-3i");
        ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57));
        ComplexNumber c4 = c3.div(c2);
        ComplexNumber c5 = c4.power(3);
        ComplexNumber c6 = c5.root(2)[1];
        System.out.println(c6);
    }
}
