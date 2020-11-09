package hr.fer.oprpp1.p07.datoteke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// U stablu direktorija treba pronaci najvecu datoteku
// s ekstenzijom *.java, te sve *.class datoteke
public class NadiDatoteke {
	
	static class Podatci implements Posao {
		File najveca;
		long velicina;
		List<File> classDatoteke = new ArrayList<>();
		
		@Override
		public void nasaoDatoteku(File staza) {
			String ekst = izvadiEkstenciju(staza.getName());
			if (ekst.equalsIgnoreCase("class")) {
				classDatoteke.add(staza);
			} else if (ekst.equalsIgnoreCase("java")) {
				long velicina = staza.length();
				if (najveca == null || this.velicina < velicina) {
					najveca = staza;
					this.velicina = velicina;
				}
			}
		}
		@Override
		public void ulazimUDirektorij(File staza) {
		}
		@Override
		public void izlazimIzDirektorija(File staza) {
		}
		
		private static String izvadiEkstenciju(String name) {
			int index = name.lastIndexOf('.');
			if (index < 1) return "";
			return name.substring(index + 1);
		}
	}
	
	public static void main(String[] args) {
		File staza = new File(args.length == 0 ? "." : args[0]);
		
		Podatci podatci = new Podatci();
		ObilazakStabla.obiđi(staza, podatci);
		
		if (podatci.najveca == null) {
			System.out.println("Nema najvece java datoteke.");
		} else {
			System.out.println("Najveca je: " + podatci.najveca + " i velicina joj je: " + podatci.velicina);
		}
		
		System.out.println("Class datoteke su:");
		for (File f : podatci.classDatoteke) {
			System.out.println(f);
		}
	}
}
