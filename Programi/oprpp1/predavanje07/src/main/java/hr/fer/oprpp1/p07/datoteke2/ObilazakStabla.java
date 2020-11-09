package hr.fer.oprpp1.p07.datoteke2;

import java.io.File;

public abstract class ObilazakStabla {

	// Serija "primitiva"
	// 1:
	protected abstract void izlazimIzDirektorija(File d);
	// 2:
	protected abstract void ulazimUDirektorij(File d);
	// 3:
	protected abstract void nasaoDatoteku(File staza);
	
	// "Okvirna metoda":
	// Metoda koja daje algoritam, ne moze se naslijediti
	public final void obiđi(File staza) {
		if (staza.isFile()) {
			nasaoDatoteku(staza);
			return;
		}
		
		if (!staza.isDirectory()) return;
		
		File[] djeca = staza.listFiles();
		if (djeca == null) return;
		
		for (File d : djeca) {
			ulazimUDirektorij(d);
			obiđi(d);
			izlazimIzDirektorija(d);
		}
	}
}
