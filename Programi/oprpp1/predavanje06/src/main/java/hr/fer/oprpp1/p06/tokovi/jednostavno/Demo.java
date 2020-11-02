package hr.fer.oprpp1.p06.tokovi.jednostavno;

import java.util.Arrays;
import java.util.stream.Stream;

public class Demo {
	public static void main(String[] args) {
		Stream<String> tokImena1 = Stream.of("Iva", "Jasna", "Vesna", "Karla");
		
		String[] imena = {"Iva", "Jasna", "Vesna", "Karla"};
		Stream<String> tokImena2 = Arrays.stream(imena);
		
		tokImena1.forEach(System.out::println);
	}
}
