package oprpp1.p03.primjer2;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class GeometrijskiLik {

    public double opseg() {
        return 0;
    }

    public double povrsina() {
        return 0;
    }

    public void popuniLik(Slika s) {
        for (int y = 0; y < s.getVisina(); y++) {
            for (int x = 0; x < s.getSirina(); x++) {
                if (sadrziTocku(x, y))
                    s.upaliTocku(x, y);
            }
        }
    }

    public boolean sadrziTocku(int x, int y) {
        return false;
    }
}
