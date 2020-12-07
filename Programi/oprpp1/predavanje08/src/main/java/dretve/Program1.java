package dretve;

public class Program1 {

	private static long brojac = 0L;
	
	// "Model" posla:
	static class Posao implements Runnable {
		private long kolikoPuta;
		
		public Posao(long kolikoPuta) {
			super();
			this.kolikoPuta = kolikoPuta;
		}
		
		@Override
		public void run() {
			Thread trenutna = Thread.currentThread();
			System.out.println("Moje ime je (a ja sam u run()): " + trenutna.getName());
			
			for (long i = 0; i < kolikoPuta; i++) {
				brojac++;
			}
			
			System.out.println("Jedan posao je gotov");
		}
	}
	
	public static void main(String[] args) {
		
		Thread trenutna = Thread.currentThread();
		System.out.println("Moje ime je: " + trenutna.getName());
		
		// Posao:
		Runnable posao = new Posao(100_000);
		
		// Stvaranje nove dretve?
		// "Opisnik dretve"
		Thread dretva = new Thread(posao);
		dretva.setName("MojaDretvaZaIzvodenjePosla");
		dretva.start();
		
		// zamrznuti trenutnu dretvu tako dug dok nasa pomocna dretva na odradi posao!
		while (true) {
			try {
				dretva.join();
			} catch (InterruptedException e) {
				continue;
			}
			break;
		}
		
		System.out.println("Rezultat uvecavanja je: " + brojac);
		
		System.out.println("Metoda main zavrsava.");
	}
}
