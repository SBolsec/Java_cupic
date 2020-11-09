---------------------------------------------------
"Ručna" konstrukcija tokova:
---------------------------------------------------
Stream.of(...)
Arrays.stream(...)

Iz kolekcija tok možemo dobiti pozivom metode .stream()

IntStream ima zgodne metode za konstrukciju tokova!
  IntStream.range(from, to)
  IntStream.rangeClosed(from, to)
  IntStream.generate(...)
  IntStream.iterate(...)

Zadatci na temeljnu baze filmova, za rješavanje tokovima:

1. Ispisati sve filmove.
2. Ispisati sve filmove iz 2009.
3. Odrediti broj filmova iz 2009.
4. Napraviti skup svih filmova iz 2009.
5. Napraviti skup svih godina u kojima su snimani filmovi.
6. Napraviti sortiranu listu (u rikverc) svih godina u kojima su snimani filmovi.
7. Ispisati sortirani popis godina u kojima su snimani filmovi, ali bez ponavljanja godina.
8. Napraviti listu imena filmova koji su žanra SCIFI.
9. Ispisati sve žanrove (za koje postoji barem jedan snimljeni film).
10. Napraviti skup svih žanrova.
11. Odrediti prosječnu ocjenu filmova snimljenih u ili nakon 2000. godine.
12. Grupirati filmove po godinama snimanja.
13. Particionirati filmove na one koji jesu SCIFI i one koji nisu.

