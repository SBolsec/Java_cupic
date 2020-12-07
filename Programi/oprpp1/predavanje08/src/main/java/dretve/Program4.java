package dretve;

import java.util.concurrent.atomic.AtomicLong;

public class Program4 {
	
	// mutex.lock()
	// kod koji je Kriticni Odsjecak (K.O.)
	// kod koji je Kriticni Odsjecak (K.O.)
	// kod koji je Kriticni Odsjecak (K.O.)
	// mutex.unlock()

	private static AtomicLong brojac = new AtomicLong(0);
	
	// "Model" posla:
	static class Posao implements Runnable {
		private long kolikoPuta;
		
		public Posao(long kolikoPuta) {
			super();
			this.kolikoPuta = kolikoPuta;
		}
		
		@Override
		public void run() {
			for (long i = 0; i < kolikoPuta; i++) {
				brojac.incrementAndGet();
			}
		}
		
		// for: (CAS = Compare & Set)
		// CAS $54356, ocekivanaVrijednostVarijable, novaVrijednostVarijable
		
		// 1) Procesor otide na memorijsku lokaciju $54356 i dohvati podatak koji je tamo zapisan
		// 2) Procesor ga usporedi s ocekivanaVrijednostVarijable
		// 3) Ako je to jednako, tada na memorijsku lokaciju $54356 zapise novaVrije<nostVarijable
		//    inace u statusni registar postavi informaciju da instrukcija nije uspjela<
	}
	
	public static void main(String[] args) {
		
		// Zelim stvoriti N dretvi gdje svaka uvecava za odreden broj puta!
		final int BROJ_DRETVI = 5;
		
		// Posao:
		Runnable posao = new Posao(100_000);
		
		// "Opisnik dretve"
		Thread[] opisnici = new Thread[BROJ_DRETVI];
		for (int i = 0; i < BROJ_DRETVI; i++) {
			opisnici[i] = new Thread(posao);
		}
		for (int i = 0; i < BROJ_DRETVI; i++) {
			opisnici[i].start();
		}
		
		// zamrznuti trenutnu dretvu tako dug dok nasa pomocna dretva na odradi posao!
		for (Thread dretva : opisnici)
		while (true) {
			try {
				dretva.join();
			} catch (InterruptedException e) {
				continue;
			}
			break;
		}
		
		System.out.println("Rezultat uvecavanja je: " + brojac.get());
		
		System.out.println("Metoda main zavrsava.");
	}
}
