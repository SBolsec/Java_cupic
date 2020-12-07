package dretve;

public class Program3 {
	
	// mutex.lock()
	// kod koji je Kriticni Odsjecak (K.O.)
	// kod koji je Kriticni Odsjecak (K.O.)
	// kod koji je Kriticni Odsjecak (K.O.)
	// mutex.unlock()

	private volatile static long brojac = 0L;
	
	// "Model" posla:
	static class Posao implements Runnable {
		private long kolikoPuta;
		private Object vratar;
		
		public Posao(long kolikoPuta, Object vratar) {
			super();
			this.kolikoPuta = kolikoPuta;
			this.vratar = vratar;
		}
		
		@Override
		public void run() {
			for (long i = 0; i < kolikoPuta; i++) {
				synchronized (vratar) {
					brojac++;
				}
			}
		}
		
		// for:
		// move reg1, [brojac]
		// inc reg1
		// move [brojac], reg1
	}
	
	public static void main(String[] args) {
		
		brojac = 0;
		
		// Zelim stvoriti N dretvi gdje svaka uvecava za odreden broj puta!
		final int BROJ_DRETVI = 5;
		
		// mutex:
		Object vratar = new Object();
		// Posao:
		Runnable posao = new Posao(100_000, vratar);
		
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
		
		System.out.println("Rezultat uvecavanja je: " + brojac);
		
		System.out.println("Metoda main zavrsava.");
	}
}
