package oprpp1.p03.primjer2;

import hr.fer.zemris.java.tecaj_3.prikaz.PrikaznikSlike;
import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Demo {

    public static void main(String[] args) {
        GeometrijskiLik[] likovi = {
                new Pravokutnik(10, 10, 100, 50),
                new Pravokutnik(10, 70, 100, 50)
        };

        Slika s = new Slika(150, 150);

        for (GeometrijskiLik lik : likovi) {
            lik.popuniLik(s);
            // (l->tablica[2])(l,s)
        }

        PrikaznikSlike.prikaziSliku(s, 4);
    }
}
