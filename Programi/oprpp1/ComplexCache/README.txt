-----------------------------------------------------------
Primjer funkcije koja računa (z+1)^2 + (z+2)^3:
-----------------------------------------------------------
public Complex func(Complex z) {
  IComplexCache cache = ComplexCache.getCache();

  Complex square1 = cache.get(z);
  Complex one = cache.get(1, 0);
  square1.addMutable(one);
  square1.powerMutable(2);
  cache.release(one);
  
  Complex square2 = cache.get(z);
  Complex two = cache.get(2, 0);
  square2.addMutable(two);
  square2.powerMutable(3);
  cache.release(two);
  
  square1.addMutable(square2);
  cache.release(square2);

  return square1;
}

Pojašnjenje: pretpostavka je da je razred Complex prerađen tako da nudi i settere odnosno mutable operacije koje mjenjaju broj nad kojim su pozvane. U gornjem primjeru to su operacije XXXMutable. Kad god kod treba neki kompleksni broj, umjesto da ga stvara s new, traži ga iz keša. Kad je kod gotov s uporabom dobivenog objekta, obavezno ga vraća u pool pozivom metode release kako bi keš taj isti objekt kasnije mogao ponovno reciklirati.

Uočite, u prethodnoj metodi od pozivatelja metode se očekuje da će na kraju objekt koji je dobio kao rezultat vratiti u keš.

Programski kod koji je dan u ovoj ZIP arhivi pretpostavlja da će se kod izvoditi iz dretve koja implementira sučelje IThreadBoundComplexCache. Evo jednog primjera takve dretve:

class CustomThread extends Thread implements IThreadBoundComplexCache {
	private IComplexCache cache = new ComplexCacheImpl();
	public CustomThread(Runnable r) {
		super(r);
	}
	@Override
	public IComplexCache getComplexCache() {
		return cache;
	}
};


Sada se bilo koji kod koji koristi keširanje može pozvati na sljedeći način:

new CustomThread(()->{
  // Ovdje ide kod koji koristi:
  IComplexCache cache = ComplexCache.getCache();
  // jer je ComplexCache dan u primjeru napisan da očekuje da ga se izvodi iz ovakve dretve
  // ...
}).start();

Alternativa, ako se kod pokreće iz thread poolova, njima je moguće zadati da koriste ThreadFactory poput:

ThreadFactory tf = r -> new CustomThread(r);

-----------------------------------------------------------
Želite li isprobati ovaj pristup s Newtonovim fraktalom:
-----------------------------------------------------------
1) Dodajte si u Complex statički instanceCounter koji broji koliko puta je razred Complex stvoren.
2) Iako radite u višedretvenom okruženju, fiksirajte broj radnika na 1 da inicijalno nemate problema s višedretvenošću.
3) Doradite Complex tako da dodate i mutable operacije
4) Preradite implementacije polinoma tako da metoda apply(z) koristi cache i vraća rezultat u objektu dobivenog iz cachea.
5) Preradite način na koji stvarate ExecutorService tako da koristi ThreadFactory koji vraća IThreadBoundComplexCache dretve
6) U metodi produce(...) presložite kod na način:

long instancesBefore = Complex.instanceCounter;
... pokreni izračune i čekaj rezultat...
long instancesAfter = Complex.instanceCounter;
System.out.printf("Drawing created %d instances of Complex class; total count is %d.%n", instancesAfter-instancesBefore, Complex.instanceCounter);

7) Pokrenite crtanje; ideja je osigurati da je ispisani broj stvorenih primjeraka malen (tada znate da ste dobro presreli sve izravne pozive stvaranja kompleksnih brojeva s new i da korektno vraćate nekorištene primjerke natrag u pool pa pool ne mora stalno stvarati nove). Nakon prvog crtanja, pritisnite par puta ESC koja u programu radi ponovno crtanje. Ako ste sve dobro napravili, posljednje-ispisani Complex.instanceCounter se nakon prvog crtanja više neće mijenjati. Ako se mijenja, negdje Vam cure objekti.

VAŽNO: prilikom prerade koda polinoma te koda koji računa iteracije izračuna fraktala nemojte odjednom mijenjati sav kod jer je to recept za glavobolju! Mijenjajte dio po dio koda i povremeno pokrećite program - uvjerite se da uz napravljene izmjene kod i dalje korektno crta fraktal.

