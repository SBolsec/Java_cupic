package oprpp1.p03.primjer4;

import java.lang.reflect.Array;

public class Glavni {

	public interface Filter {
		boolean shouldProcess(double value);
	}
	
	public interface Transformer {
		double transformiraj(double value); // signatura: vraća double, zove se transformiraj, prima jedan double
	}

	@FunctionalInterface
	public interface Processor {
		abstract void process(double oldValue, double newValue);
	}
	
	public static class IspisivacProsireni implements Processor {
		@Override
		public void process(double oldValue, double newValue) {
			System.out.println(oldValue + " -> " + newValue);
		}
	}

	public static class IspisivacJednostavni implements Processor {
		@Override
		public void process(double oldValue, double newValue) {
			System.out.println(newValue);
		}
	}

	public static class Snimac implements Processor {
		@Override
		public void process(double oldValue, double newValue) {
			//System.out.println(value);
		}
	}

	public static class Mailer implements Processor {
		@Override
		public void process(double oldValue, double newValue) {
			//System.out.println(value);
		}
	}

	public static class UvecajZa3 implements Transformer {
		@Override
		public double transformiraj(double x) {
			return x+3;
		}
	}
	
	public static class Kvadriraj implements Transformer {
		@Override
		public double transformiraj(double value) {
			return value*value;
		}
	}
	
	public static class OdrediSinus implements Transformer {
		@Override
		public double transformiraj(double value) {
			return Math.sin(value);
		}
	}

	
	public static void main(String[] args) {
		double[] daniBrojevi = {1, -3.14, 5, 28, -34, 17};
		String prviArg = "kvadriraj";
		String drugiArg = "ispiši";
		
		Transformer t1 = new Transformer() {
			@Override
			public double transformiraj(double value) {
				return value+3;
			}
		};
		
		// najrječitija inačica lambda izraza
		Transformer t2 = (double value) -> {
				return value+3;
			};
		
		Transformer t3 = (value) -> {
			return value+3;
		};
		
		Transformer t4 = value -> {
			return value+3;
		};

		// Najjasniji zapis lambda izraza koji doslovno kaže kako se radi transformacija
		Transformer t5 = value -> value+3;
		
		Transformer t6 = (double x) -> {
				return Math.sin(x);
			};
		
		Transformer t7 = (value) -> {
			return Math.sin(value);
		};
		
		Transformer t8 = x -> {
			return Math.sin(x);
		};
		
		Transformer t8b = x -> Math.sin(x);
		
		
		Transformer t9 = Math::sin;   // R x; R::toString ==> R.toString(x), x.toString()
		
		Processor p1 = new Processor() {
			@Override
			public void process(double oldValue, double newValue) {
				System.out.println(oldValue + " -> " + newValue);
			}
		};
		
		Processor p2 = (double oldValue, double newValue) -> {
				System.out.println(oldValue + " -> " + newValue);
			};
		
		Processor p3 = (oldValue, newValue) -> {
			System.out.println(oldValue + " -> " + newValue);
		};
		
		Processor p4 = (oldValue, newValue) -> System.out.println(oldValue + " -> " + newValue);
		
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("Evo našeg posla...");
			}
		};
		
		Runnable r2 = () -> {
				System.out.println("Evo našeg posla...");
			};
		
		Runnable r3 = () -> System.out.println("Evo našeg posla...");

		Transformer t = null;
		if(prviArg.equals("uvecajZa3")) {
			t = new UvecajZa3();
		} else if(prviArg.equals("kvadriraj")) {
			t = new Kvadriraj();
		} else {
			System.out.println("Dragi korisniče, ...");
			return;
		}
		
		Processor p = null;
		if(drugiArg.equals("ispiši")) {
			p = new IspisivacProsireni();
		} else {
			System.out.println("Dragi korisniče, ...");
			return;
		}
		
		double[] noviBrojevi = transformirajIspisiIVrati(daniBrojevi, t, p, v -> v*v>7);
		
	}

	private static double[] transformirajIspisiIVrati(double[] izvorniBrojevi, Transformer t, Processor p, Filter f) {
		double[] rezultat = new double[izvorniBrojevi.length];
		for(int i = 0; i < izvorniBrojevi.length; i++) {
			rezultat[i] = t.transformiraj(izvorniBrojevi[i]);
			if(f.shouldProcess(rezultat[i])) {
				p.process(izvorniBrojevi[i], rezultat[i]);
			}
 		}
		return rezultat;
	}

}
