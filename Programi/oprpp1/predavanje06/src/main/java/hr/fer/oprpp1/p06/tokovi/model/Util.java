package hr.fer.oprpp1.p06.tokovi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Util {

	private Util() {
	}

	public interface MovieCreator<T> {
		T create(String name, int year, Set<Genre> genres, double rating);
	}
	
	public static <T> Collection<T> createCollection(MovieCreator<T> creator) {
		List<T> list = new ArrayList<>();

		list.add(creator.create("Star Wars Episode IV - A New Hope", 1977, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 8.6));
		list.add(creator.create("Star Wars Episode V - The Empire Strikes Back", 1980, Set.of( Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 8.7));
		list.add(creator.create("Star Wars Episode VI - Return of the Jedi", 1983, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 8.3));
		list.add(creator.create("Star Wars Episode I - The Phantom Menace", 1999, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 6.5));
		list.add(creator.create("Star Wars Episode II - Attack of the Clones", 2002, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 6.6));
		list.add(creator.create("Star Wars Episode III - Revenge of the Sith", 2005, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 7.5));
		list.add(creator.create("Star Wars Episode VII - The Force Awakens", 2015, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 7.9));
		list.add(creator.create("Star Wars Episode VIII - The Last Jedi", 2017, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 7.1));
		list.add(creator.create("Star Wars Episode IX - The Rise of Skywalker", 2019, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI), 6.9));
		list.add(creator.create("Star Wars Rogue One", 2016, Set.of( Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 7.8));
		list.add(creator.create("Star Wars Solo: A Star Wars Story", 2018, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 6.9));

		list.add(creator.create("Star Trek: The Motion Picture", 1979, Set.of(Genre.ADVENTURE, Genre.MYSTERY, Genre.SCIFI), 6.4));
		list.add(creator.create("Star Trek II: The Wrath of Khan", 1982, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 7.7));
		list.add(creator.create("Star Trek III: The Search for Spock", 1984, Set.of( Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 6.7));
		list.add(creator.create("Star Trek IV: The Voyage Home", 1986, Set.of(Genre.ADVENTURE, Genre.COMEDY, Genre.SCIFI), 7.3));
		list.add(creator.create("Star Trek V: The Final Frontier", 1989, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FANTASY, Genre.SCIFI, Genre.THRILLER), 5.5));
		list.add(creator.create("Star Trek VI: The Undiscovered Country", 1991, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI, Genre.THRILLER), 7.2));
		list.add(creator.create("Star Trek Generations", 1994, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.MYSTERY, Genre.SCIFI), 8.6));
		list.add(creator.create("Star Trek: First Contact", 1996, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.DRAMA, Genre.SCIFI, Genre.THRILLER), 7.6));
		list.add(creator.create("Star Trek: Insurrection", 1998, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI, Genre.THRILLER), 6.4));
		list.add(creator.create("Star Trek: Nemesis", 2002, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI, Genre.THRILLER), 6.4));
		list.add(creator.create("Star Trek", 2009, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 7.9));
		list.add(creator.create("Star Trek Into Darkness", 2013, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI), 7.7));
		list.add(creator.create("Star Trek Beyond", 2016, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.SCIFI, Genre.THRILLER), 7.1));

		list.add(creator.create("The Lord of the Rings: The Fellowship of the Ring", 2001, Set.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY), 8.8));
		list.add(creator.create("The Lord of the Rings: The Two Towers", 2002, Set.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY), 8.7));
		list.add(creator.create("The Lord of the Rings: The Return of the King", 2003, Set.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY), 8.9));

		list.add(creator.create("The Hobbit: An Unexpected Journey", 2012, Set.of(Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY), 7.8));
		list.add(creator.create("The Hobbit: The Desolation of Smaug", 2013, Set.of(Genre.ADVENTURE, Genre.FANTASY), 7.8));
		list.add(creator.create("The Hobbit: The Battle of the Five Armies", 2014, Set.of(Genre.ADVENTURE, Genre.FANTASY), 7.4));

		list.add(creator.create("The Twilight Saga: Twilight", 2008, Set.of(Genre.DRAMA, Genre.FANTASY, Genre.ROMANCE), 5.2));
		list.add(creator.create("The Twilight Saga: New Moon", 2009, Set.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY, Genre.MYSTERY, Genre.ROMANCE), 4.7));
		list.add(creator.create("The Twilight Saga: Eclipse", 2010, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.CRIME, Genre.DRAMA, Genre.FANTASY, Genre.MYSTERY, Genre.ROMANCE, Genre.SCIFI, Genre.THRILLER), 5.0));
		list.add(creator.create("The Twilight Saga: Breaking Dawn - Part 1", 2011, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY, Genre.ROMANCE, Genre.THRILLER), 4.9));
		list.add(creator.create("The Twilight Saga: Breaking Dawn - Part 2", 2012, Set.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY, Genre.ROMANCE), 5.5));

		list.add(creator.create("Harry Potter and the Philosopher's Stone", 2001, Set.of(Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY), 7.6));
		list.add(creator.create("Harry Potter and the Chamber of Secrets", 2002, Set.of(Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY, Genre.MYSTERY), 7.4));
		list.add(creator.create("Harry Potter and the Prisoner of Azkaban", 2004, Set.of( Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY, Genre.MYSTERY), 7.9));
		list.add(creator.create("Harry Potter and the Goblet of Fire", 2005, Set.of(Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY, Genre.MYSTERY), 7.7));
		list.add(creator.create("Harry Potter and the Order of the Phoenix", 2007, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY, Genre.MYSTERY), 7.5));
		list.add(creator.create("Harry Potter and the Half-Blood Prince", 2009, Set.of(Genre.ACTION, Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY, Genre.MYSTERY), 7.6));
		list.add(creator.create("Harry Potter and the Deathly Hallows - Part 1", 2010, Set.of(Genre.ADVENTURE, Genre.FAMILY, Genre.FANTASY, Genre.MYSTERY), 7.7));
		list.add(creator.create("Harry Potter and the Deathly Hallows - Part 2", 2011, Set.of(Genre.ADVENTURE, Genre.DRAMA, Genre.FANTASY, Genre.MYSTERY), 8.1));

		return list;
	}
	
}
