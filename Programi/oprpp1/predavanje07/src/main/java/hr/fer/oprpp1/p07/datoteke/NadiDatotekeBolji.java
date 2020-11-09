package hr.fer.oprpp1.p07.datoteke;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// U stablu direktorija treba pronaci najvecu datoteku
// s ekstenzijom *.java, te sve *.class datoteke
public class NadiDatotekeBolji {
	
	static class Podatci {
		File najveca;
		long velicina;
		List<File> classDatoteke = new ArrayList<>();
	}
	
	public static void main(String[] args) {
		File staza = new File(args.length == 0 ? "." : args[0]);
		
		Podatci podatci = new Podatci();
		trazi(staza, podatci);
		
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
	
	public static void trazi(File staza, Podatci podatci) {
		if (staza.isFile()) {
			String ekst = izvadiEkstenciju(staza.getName());
			if (ekst.equalsIgnoreCase("class")) {
				podatci.classDatoteke.add(staza);
			} else if (ekst.equalsIgnoreCase("java")) {
				long velicina = staza.length();
				if (podatci.najveca == null || podatci.velicina < velicina) {
					podatci.najveca = staza;
					podatci.velicina = velicina;
				}
			}
		}
		
		File[] djeca = staza.listFiles();
		if (djeca == null) return;
		
		for (File d : djeca) {
			trazi(d, podatci);
		}
	}

	private static String izvadiEkstenciju(String name) {
		int index = name.lastIndexOf('.');
		if (index < 1) return "";
		return name.substring(index + 1);
	}
}
