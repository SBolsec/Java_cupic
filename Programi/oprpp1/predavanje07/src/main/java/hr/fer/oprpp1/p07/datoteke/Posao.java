package hr.fer.oprpp1.p07.datoteke;

import java.io.File;

public interface Posao {

	void nasaoDatoteku(File staza);
	
	void ulazimUDirektorij(File staza);
	
	void izlazimIzDirektorija(File staza);
}
