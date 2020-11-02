package hr.fer.oprpp1.p06.tokovi.primjerTemplate;

import java.util.ArrayList;
import java.util.List;

import hr.fer.oprpp1.p06.tokovi.model.Util;

public class Demo {

	public static void main(String[] args) {
		List<Movie> movies = new ArrayList<>(Util.createCollection(Movie::new));
		movies.forEach(System.out::println);

		System.out.println(movies.contains(movies.get(0)));
	}

}
