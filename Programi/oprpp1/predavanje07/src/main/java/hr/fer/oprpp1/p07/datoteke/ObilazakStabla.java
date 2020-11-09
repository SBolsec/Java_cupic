package hr.fer.oprpp1.p07.datoteke;

import java.io.File;

public class ObilazakStabla {

	public static void obiđi(File staza, Posao posao) {
		if (staza.isFile()) {
			// ????
			posao.nasaoDatoteku(staza);
			return;
		}
		
		if (!staza.isDirectory()) return;
		
		File[] djeca = staza.listFiles();
		if (djeca == null) return;
		
		for (File d : djeca) {
			// ????
			posao.ulazimUDirektorij(d);
			obiđi(d, posao);
			// ????
			posao.izlazimIzDirektorija(d);
		}
	}
}
