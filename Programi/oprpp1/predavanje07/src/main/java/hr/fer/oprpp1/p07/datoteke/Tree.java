package hr.fer.oprpp1.p07.datoteke;

import java.io.File;

public class Tree {

	public static void main(String[] args) {
		File staza = new File(args.length == 0 ? "." : args[0]);
		
		ispis(staza, 0);
		
		System.out.println();
		
		ispisx(staza);
	}

	private static void ispis(File staza, int razina) {
		System.out.printf("%s%s%n", " ".repeat(razina*3), staza.getName());
		if (staza.isDirectory()) {
			File[] djeca = staza.listFiles();
			if (djeca == null) return;
			for (File d : djeca) {
				ispis(d, razina+1);
			}
		}
	}
	
	static int razinax = 0;
	private static void ispisx(File staza) {
		System.out.printf("%s%s%n", " ".repeat(razinax*3), staza.getName());
		if (staza.isDirectory()) {
			File[] djeca = staza.listFiles();
			if (djeca == null) return;
			for (File d : djeca) {
				razinax++;
				ispisx(d);
				razinax--;
			}
		}
	}
}
