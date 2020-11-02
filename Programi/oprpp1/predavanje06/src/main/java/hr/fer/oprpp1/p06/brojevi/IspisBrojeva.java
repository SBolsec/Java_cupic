package hr.fer.oprpp1.p06.brojevi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.function.Consumer;

// Preko tipkovnice cita niz decimalnih brojeva
// ako nije broj, ispise neku poruku i nastavi dalje.
// Na kraju na zaslon ispisuje sve brojeve vece od
// prosjeka unesenih brojeva, i to sortirano (od manjih prema vecim)
public class IspisBrojeva {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		List<Double> brojevi = new ArrayList<>();
		while (true) {
			if (sc.hasNextDouble()) {
				double broj = sc.nextDouble();
				brojevi.add(broj);
			} else {
				String sljedeci = sc.next();
				if (sljedeci.equals("kraj")) break;
				System.out.println("Dragi korisnice, " + sljedeci + " nije broj i nije naredba kraj.");
			}
		}
		sc.close();
		
//		for (int i = 0; i < brojevi.size(); i++) {
//			System.out.println(brojevi.get(i));
//		}
//		
//		for (Double broj : brojevi) {
//			System.out.println(broj);
//		}
//		
//		Consumer<Double> c = System.out::println;
//		brojevi.forEach(c);
//		
//		brojevi.forEach(System.out::println);
		
		OptionalDouble prosjek = racunajProsjek(brojevi);
		if (prosjek.isEmpty()) {
			System.out.println("Dragi korisnice, nema elemenata pa nema niti prosjeka.");
			return;
		}
		
		double p = prosjek.getAsDouble();
		List<Double> kolekcija = new ArrayList<>(brojevi);
		kolekcija.removeIf(d -> d<=p);
		kolekcija.sort(null);
		kolekcija.forEach(System.out::println);
	}
	
	private static OptionalDouble racunajProsjek(Collection<Double> brojevi) {
		if (brojevi.isEmpty()) return OptionalDouble.empty();
		
		double sum = 0;
		
		for (Double d : brojevi) {
			sum += d;
		}
		
		return OptionalDouble.of(sum / brojevi.size());
	}
}
