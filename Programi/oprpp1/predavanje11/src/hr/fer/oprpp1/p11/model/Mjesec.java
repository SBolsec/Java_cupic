package hr.fer.oprpp1.p11.model;

public class Mjesec {
	private int redniBrojUGodini;
	private String naziv;
	private int brojDana;
	private boolean pozeljanZaGodisnji;
	public Mjesec(int redniBrojUGodini, String naziv, int brojDana, boolean pozeljanZaGodisnji) {
		super();
		this.redniBrojUGodini = redniBrojUGodini;
		this.naziv = naziv;
		this.brojDana = brojDana;
		this.pozeljanZaGodisnji = pozeljanZaGodisnji;
	}
	
	public int getRedniBrojUGodini() {
		return redniBrojUGodini;
	}
	public String getNaziv() {
		return naziv;
	}
	public int getBrojDana() {
		return brojDana;
	}
	public boolean isPozeljanZaGodisnji() {
		return pozeljanZaGodisnji;
	}
	public void setRedniBrojUGodini(int redniBrojUGodini) {
		this.redniBrojUGodini = redniBrojUGodini;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public void setBrojDana(int brojDana) {
		this.brojDana = brojDana;
	}

	public void setPozeljanZaGodisnji(boolean pozeljanZaGodisnji) {
		this.pozeljanZaGodisnji = pozeljanZaGodisnji;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mjesec other = (Mjesec) obj;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return naziv;
	}
}
