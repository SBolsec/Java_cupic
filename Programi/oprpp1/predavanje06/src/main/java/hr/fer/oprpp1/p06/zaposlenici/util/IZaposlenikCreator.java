package hr.fer.oprpp1.p06.zaposlenici.util;

public interface IZaposlenikCreator<T> {
	T create(String sifra, String prezime, String ime);
}
