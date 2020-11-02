package hr.fer.oprpp1.p04.kolekcije;

// Važno: parametrizaciju nismo proveli do kraja po svim metodama, a na predavanju smo detaljnije pričali i o dodatnim metodama.
public class Collection<T> {

	public boolean isEmpty() {
		return false;
	}
	
	public int size() {
		return 0;
	}
	
	public void add(T value) {
	}
	
	public boolean contains(Object value) {
		return false;
	}
	
	public boolean remove(Object value) {
		return false;
	}
	
	public T[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	public void forEach(Processor processor) {
	}

	public void addAll(Collection<T> other) {
	}
	
	public void clear() {
	}
}
