package hr.fer.oprpp1.p06.tokovi.primjer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import hr.fer.oprpp1.p06.tokovi.model.Genre;
import hr.fer.oprpp1.p06.tokovi.model.Util;

public class Demo {

	public static void main(String[] args) {
		List<Movie> movies = new ArrayList<>(Util.createCollection(Movie::new));
		
		// Zadatak 1.
		movies.stream()
				.forEach(System.out::println);
		
		System.out.println();
		
		// Zadatak 2.
		movies.stream()
				.filter(m -> m.getYear() == 2009)
				.forEach(System.out::println);
		
		System.out.println();
		
		// Zadatak 3
		long num = movies.stream()
				.filter(m -> m.getYear() == 2009)
				.count();
		System.out.println(num);
		
		System.out.println();
		
		// Zadatak 4
		Set<Movie> set = movies.stream()
				.filter(m -> m.getYear() == 2009)
				.collect(Collectors.toSet());
		set.forEach(System.out::println);
		
		System.out.println();
		
		// Zadatak 5
		Set<Integer> skupGodina = movies.stream()
				.map(Movie::getYear)
				.collect(Collectors.toSet());
		
		// Zadatak 6
		List<Integer> godina = movies.stream()
				.map(Movie::getYear)
				.distinct()
				.sorted(Comparator.<Integer>naturalOrder().reversed())
				.collect(Collectors.toList());
		
		System.out.println();
		
		// Zadatak 7
		movies.stream()
				.map(Movie::getYear)
				.distinct()
				.sorted()
				.forEach(System.out::println);
		
		// Zadatak 8
		List<String> imenaFilmova = movies.stream()
				.filter(m -> m.getGenres().contains(Genre.SCIFI))
				.map(Movie::getName)
				.collect(Collectors.toList());
		
		System.out.println();
		
		// Zadatak 9
		movies.stream()
				.flatMap(m -> m.getGenres().stream())
				.distinct()
				.forEach(System.out::println);
		
		System.out.println();
		
		// Zadatak 10
		List<Genre> ganrovi = movies.stream()
			.flatMap(m -> m.getGenres().stream())
			.distinct()
			.collect(Collectors.toList());
		
		// Zadatak 11
		OptionalDouble prosjek = movies.stream()
			.filter(m -> m.getYear() >= 2000)
			.mapToDouble(Movie::getRating)
			.average();
		
		// Zadatak 12
		Map<Integer, List<Movie>> filmoviPoGodinama = movies.stream()
			.collect(Collectors.groupingBy(Movie::getYear));
		
		// Zadatak 13
		Map<Boolean, List<Movie>> jesuNisuSCIFI = movies.stream()
			.collect(Collectors.groupingBy(m -> m.getGenres().contains(Genre.SCIFI)));
	
		// Zadatak 13
		Map<Boolean, List<Movie>> jesuNisuSCIFI2 = movies.stream()
			.collect(Collectors.partitioningBy(m -> m.getGenres().contains(Genre.SCIFI)));
		
		String[] imena = {"Ivana", "Jasna", "Vesna", "Kristina"};
		
		//String x = ???;		// {Ivana | Jasna | Vesna | Kristina}
		
		String x = Arrays.stream(imena)
				.collect(Collectors.joining(" | ", "{", "}"));
		System.out.println(x);
	}
}
