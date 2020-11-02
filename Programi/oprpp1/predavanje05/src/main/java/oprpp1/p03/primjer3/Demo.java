package oprpp1.p03.primjer3;

import hr.fer.zemris.java.tecaj_3.prikaz.PrikaznikSlike;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Demo {

	public static void main(String[] args) {
		GeometrijskiLik[] likovi = {
				//new GeometrijskiLik(),
				new Pravokutnik(10, 10, 200, 50),
				new Pravokutnik(10, 70, 100, 50),
				new ObojaniPravokutnik(10, 130, 100, 10, 2)
		};
		
		Slika s = new Slika(150, 150);
		
		for(GeometrijskiLik l : likovi) {
			l.popuniLik(s);
			// (l->tablica[2])(l,s)
		}
		
		PrikaznikSlike.prikaziSliku(s, 4);
	}
}
