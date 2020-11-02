package hr.fer.oprpp1.p06.tokovi.primjerTemplate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import hr.fer.oprpp1.p06.tokovi.model.Genre;

public class Movie implements Comparable<Movie> {

	private String name;
	private int year;
	private Set<Genre> genres;
	private double rating;
	
	public Movie(String name, int year, Set<Genre> genres, double rating) {
		super();
		this.name = Objects.requireNonNull(name);
		this.year = year;
		this.genres = Collections.unmodifiableSet(new HashSet<Genre>(Objects.requireNonNull(genres)));
		this.rating = rating;
	}

	public String getName() {
		return name;
	}
	
	public int getYear() {
		return year;
	}

	public Set<Genre> getGenres() {
		return genres;
	}
	
	public double getRating() {
		return rating;
	}
	
	public Movie duplicate() {
		return new Movie(name, year, genres, rating);
	}
	
	@Override
	public String toString() {
		return String.format("%s (%d; %s; %f)", name, year, genres, rating);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Movie))
			return false;
		Movie other = (Movie) obj;
		if (!this.name.equals(other.name))
			return false;
		if (this.year != other.year)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Movie o) {
		int r = this.name.compareTo(o.name);
		if(r != 0) return r;
		return Integer.compare(this.year, o.year);
	}
	
}
