package hr.fer.oprpp1.p04.kolekcije;

public class Demo1 {

	public static void main(String[] args) {
		Collection col = new FixedCollection("Ivo", "Jasna", "Ante", "Marija");
		
		col.forEach(new Processor() {
			@Override
			public void process(Object element) {
				System.out.println(element);
			}
		});
	}

//	public static void main(String[] args) {
//		Collection col = new FixedCollection("Ivo", "Jasna", "Ante", "Marija");
//		
//		Processor isp = new Processor() {
//			@Override
//			public void process(Object element) {
//				System.out.println(element);
//			}
//		};
//		col.forEach(isp);
//	}
//
//	public static void main(String[] args) {
//		Collection col = new FixedCollection("Ivo", "Jasna", "Ante", "Marija");
//		
//		class Ispisivac implements Processor {
//			@Override
//			public void process(Object element) {
//				System.out.println(element);
//			}
//		}
//		
//		Processor isp = new Ispisivac();
//		col.forEach(isp);
//	}

}
