package hr.fer.oprpp1.p04.kolekcije;

public class Demo2 {

	public static void main(String[] args) {
		
		Collection<String> kolekcija = new FixedCollection<String>("Ante", "Jasmina", "Zagreb");
		
		// Ovo dodjeljivanje će puknuti - kolekcija vraća Object[] a ne String[]
		String[] polje = kolekcija.toArray();
		
		for(Object element : polje) {
			System.out.println(element);
		}
		
	}

	public static void mainx(String[] args) {
		
		Collection<Object> kolekcija = new FixedCollection<>(Integer.valueOf(42), "Jasmina", Float.valueOf(2.71f));
		Object[] polje = kolekcija.toArray();
		
		for(Object element : polje) {
			System.out.println(element);
		}
		
	}

}
