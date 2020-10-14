package oprpp1.p02.demo;

public class Sorter {
	
	/**
     * Metoda prima polje od dva elementa i vraca novo
     * sortirano polje.
     * 
     * @param polje polje koje treba sortirati
     * @return novo sortirano polje
     * @throws NullPointerException ako se kao polje posalje <code>null</code> referenca
     * @throws IllegalArgumentException ako je predano polje ciji je broj elemenata razlicit od 2
     */
	public static int[] sortirajDvaElementa(int[] polje) {
		if (polje == null) throw new NullPointerException();
		if (polje.length != 2) throw new IllegalArgumentException("Polje je bilo duljine " + polje.length + ".");
		
		if (polje[0] > polje[1]) {
			return new int[] {polje[1], polje[0]};
		} else {
			return new int[] {polje[0], polje[1]};
		}
	}
}