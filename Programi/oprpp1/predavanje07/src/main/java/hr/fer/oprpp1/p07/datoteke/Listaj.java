package hr.fer.oprpp1.p07.datoteke;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Listaj {
	
	public static void main(String[] args) {
		if (args.length > 1) {
			System.out.println("Program ocekuje pokretanje ili bez argumenata, ili uz navodenje staze.");
			return;
		}
		
		File directory = new File(args.length == 0 ? "." : args[0]);
		
		if (!directory.exists()) {
			System.out.println("Path " + directory + " does not exist!");
			return;
		}
		
		if (!directory.isDirectory()) {
			System.out.println("Not a directory!");
			return;
		}
		
		if (!directory.canRead()) {
			System.out.println("Can not read directory!");
			System.exit(1);
		}
		
		File[] children = directory.listFiles();
		
		if (children == null) {
			System.out.println("Directory has no children!");
			System.exit(1);
		}
		
		for (File child : children) {
			System.out.printf("[%s] %12d %s%n", child.isDirectory() ? "DIR" : "FIL", child.length(), child.getName());
			
			// Moj nacin
//			StringBuilder sb = new StringBuilder();
//			if (child.isDirectory()) {
//				sb.append("[DIR] ");
//			} else {
//				sb.append("[FIL] ");
//			}
//			String length = Long.toString(child.length());
//			sb.append(" ".repeat(12 - length.length()));
//			sb.append(child.length());
//			sb.append(" ").append(child.getName());
//			System.out.println(sb.toString());
		}
		
		System.out.println();
		
		// Moj kod
		Arrays.stream(children)
			.sorted((o1, o2) -> {
					if (o1.isDirectory() && o2.isDirectory()) {
						return o1.getName().compareTo(o2.getName());
					}
					if (o1.isDirectory() && !o2.isDirectory()) {
						return -1;
					}
					if (!o1.isDirectory() && o2.isDirectory()) {
						return 1;
					}
					return o1.getName().compareTo(o2.getName());
				}
			)
			.forEach(child -> System.out.printf("[%s] %12d %s%n", child.isDirectory() ? "DIR" : "FIL", child.length(), child.getName()));
		System.out.println();
		
		// Cupicev kod
		final Comparator<File> PO_TIPU = (f1, f2) -> {
			boolean d1 = f1.isDirectory();
			boolean d2 = f2.isDirectory();
			if (d1) {
				if (!d2) return -1;
				return 0;
			}
			if (d2) return 1;
			return 0;
		};
		final Comparator<File> PO_IMENU = (f1, f2) -> f1.getName().compareTo(f2.getName());
		final Comparator<File> PO_TIPU_PA_IMENU = PO_TIPU.thenComparing(PO_IMENU);
		
		Comparator<File> PO_TIPU_2 = Comparator.comparing(f -> !f.isDirectory());
		Comparator<File> PO_TIPU_3 = Comparator.comparing(File::isDirectory, Comparator.reverseOrder());
		// komparator fajlova po imenu u rikverc
		Comparator<File> PO_IMENU_2 = Comparator.comparing(File::getName, Comparator.reverseOrder());
		
		Arrays.sort(children, PO_TIPU_PA_IMENU);
		Arrays.asList(children).sort(PO_TIPU_PA_IMENU);
		
		for (File child : children) {
			System.out.printf("[%s] %12d %s%n", child.isDirectory() ? "DIR" : "FIL", child.length(), child.getName());
		}
		
		Arrays.stream(children).sorted(PO_TIPU_PA_IMENU).forEach(child
				-> System.out.printf("[%s] %12d %s%n", child.isDirectory() ? "DIR" : "FIL", child.length(), child.getName())
		);
		
		List<File> sortitano = Arrays.stream(children).sorted(PO_TIPU_PA_IMENU).collect(Collectors.toList());
		for (File child : sortitano) {
			System.out.printf("[%s] %12d %s%n", child.isDirectory() ? "DIR" : "FIL", child.length(), child.getName());
		}
	}
	
	// program prima 1 argument: stazu do direktorija (ako se ne zada, koristi trenutni direktorij)
	// Lista sadrzaj po sljedecem formatu
	// [DIR]            50 dir1
	// [FIL]     234834763 datoteka1.txt
	// ...
}
