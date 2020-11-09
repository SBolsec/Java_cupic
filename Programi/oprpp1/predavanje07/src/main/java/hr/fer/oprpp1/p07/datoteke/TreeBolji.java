package hr.fer.oprpp1.p07.datoteke;

import java.io.File;

public class TreeBolji {

	static class IspisStabla implements Posao {
		int razina;
		
		@Override
		public void nasaoDatoteku(File staza) {
			ispisi(staza);
		}

		@Override
		public void ulazimUDirektorij(File staza) {
			ispisi(staza);
			razina++;
		}

		@Override
		public void izlazimIzDirektorija(File staza) {
			razina--;			
		}
		
		private void ispisi(File staza) {
			System.out.printf("%s%s%n", " ".repeat(razina*3), staza.getName());
		}
		
	}
	
	
	
	
	public static void main(String[] args) {
		File staza = new File(args.length == 0 ? "." : args[0]);
		
		ObilazakStabla.obiđi(staza, new IspisStabla());
	}
}
