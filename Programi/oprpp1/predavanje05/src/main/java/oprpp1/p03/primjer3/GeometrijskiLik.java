package oprpp1.p03.primjer3;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public abstract class GeometrijskiLik {

	public abstract double opseg();
	
	public abstract double povrsina();
	
	public void popuniLik(Slika s) {
		for(int y = 0; y < s.getVisina(); y++) {
			for(int x = 0; x < s.getSirina(); x++) {
				if(this.sadrziTocku(x, y)) {  // ovo je zapravo: this->tablica[3](this,x,y) pa će završiti na implementaciji koja odgovara stvarnoj vrsti objekta nad kojim se poziva 
					s.upaliTocku(x, y);
				}
			}
		}
	}
	
	public abstract boolean sadrziTocku(int x, int y);

}
