package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.collections.ElementsGetter;
import hr.fer.oprpp1.custom.collections.LinkedListIndexedCollection;
import hr.fer.oprpp1.custom.collections.List;

public class Demo1 {
	public static void main(String[] args) {
		Collection<String> col1 = new ArrayIndexedCollection<>();
		col1.add("Ana");
		col1.add("Jasna");
		col1.forEach(e -> System.out.println(e));
		
		List<String> list1 = new ArrayIndexedCollection<>(col1);
		list1.insert("Marko", 1);
		list1.add("Pero");
		list1.forEach(e -> System.out.println(e));
		
		List<Integer> list2 = new LinkedListIndexedCollection<>();
		list2.add(Integer.valueOf(2));
		list2.add(Integer.valueOf(8));
		
		List<Double> list3 = new ArrayIndexedCollection<>();
		list3.add(Double.valueOf(3.1415926));
		list3.add(Double.valueOf(1.41));
		
		List<Number> list4 = new ArrayIndexedCollection<>(list2);
		list4.addAll(list3);
		ElementsGetter<Number> getter = list4.createElementsGetter();
		System.out.println(getter.getNextElement());
		System.out.println(getter.getNextElement());
		getter.processRemaining(e -> System.out.println(e));
	}
}
