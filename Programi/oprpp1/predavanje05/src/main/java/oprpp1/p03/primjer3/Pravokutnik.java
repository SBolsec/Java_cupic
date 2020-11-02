package oprpp1.p03.primjer3;

import hr.fer.zemris.java.tecaj_3.prikaz.Slika;

public class Pravokutnik extends GeometrijskiLik {
	private int lijeviX;
	private int gornjiY;
	private int sirina;
	private int visina;
	
	public Pravokutnik(int lijeviX, int gornjiY, int sirina, int visina) {
		super();
		this.lijeviX = lijeviX;
		this.gornjiY = gornjiY;
		this.sirina = sirina;
		this.visina = visina;
	}
	
	@Override
	public double opseg() {
		return 2*(sirina+visina);
	}
	
	@Override
	public double povrsina() {
		return sirina * visina;
	}
	
	@Override
	public void popuniLik(Slika s) {
		for(int y = gornjiY; y < gornjiY+visina; y++) {
			for(int x = lijeviX; x < lijeviX+sirina; x++) {
				s.upaliTocku(x, y);
			}
		}
	}
	
	@Override
	public boolean sadrziTocku(int x, int y) {
		if(x < lijeviX) return false;
		if(y < gornjiY) return false;
		if(x >= lijeviX+sirina) return false;
		if(y >= gornjiY+visina) return false;
		
		return true;
	}
	
}
