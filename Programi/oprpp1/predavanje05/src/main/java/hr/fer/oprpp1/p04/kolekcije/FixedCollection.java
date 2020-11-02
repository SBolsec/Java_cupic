package hr.fer.oprpp1.p04.kolekcije;

import java.lang.reflect.Array;

public class FixedCollection<T> extends Collection<T> {

	private T[] elements;
	
	@SuppressWarnings("unchecked")
	public FixedCollection(T ... elems) {
		Object[] novoPolje = new Object[elems.length];
		for(int i = 0; i < elems.length; i++) {
			novoPolje[i] = elems[i];
		}
		
		elements = (T[])novoPolje;
	}

	public T[] toArray(T[] polje) {  // R[] polje = {new P(), new R(),     };
		// Ovo je naša loša implementacija:
		return elements;
		
		// Konceptualno, mogli bismo napraviti polje tipa koji odgovara prvom elementu predanog polja:
		// T[] newArray = (T[])Array.newInstance(polje[0].getClass(), size());
		// Ali to je loše iz razloga koje smo također prodiskutirali na predavanju

		// Stvarna implementacija bi pitala samo polje da javi kojeg su deklariranog tipa njegove komponente:
		// T[] newArray = (T[])Array.newInstance(polje.getClass().getComponentType(), size());
		
		// U oba slučaja zatim bismo iskopirali upravo size() referenci u to polje:
		// System.arraycopy(elements, 0, newArray, 0, size());
		
		// I novo polje vratili pozivatelju:
		//return newArray;
	}

	public T[] toArray() {
		return elements;
	}

	@Override
	public void forEach(Processor processor) {
		for(Object o : elements) {
			processor.process(o);
		}
	}
	
	@Override
	public void add(Object value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addAll(Collection other) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean contains(Object value) {
		for(Object elem : elements) {
			if(elem.equals(value)) return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public boolean remove(Object value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int size() {
		return elements.length;
	}
	
}
