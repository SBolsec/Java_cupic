package oprpp1.p03.primjer1;

import static java.lang.Math.sqrt;
import static java.lang.Math.hypot;

/**
 * Razred predstavlja model nepromjenjivog 2D-vektora.
 * 
 * @author marcupic
 */
public class Vector2D {

	public static int counter = 0;
	public static int addcounter = 0;

	public static final Vector2D I = new Vector2D(1,0);
	public static final Vector2D J = new Vector2D(0,1);
	
	private final double x;
	private final double y;
	
	/**
	 * Stvara novi općeniti 2D vektor čije su komponente x i y.
	 * @param x komponenta uz jedinični vektor "i"
	 * @param y komponenta uz jedinični vektor "j"
	 */
	public Vector2D(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		Vector2D.counter++;
		System.out.println("Stvaram vektor "+this+", counter = " + counter);
	}
	
	/**
	 * Stvara novi vektor koji leži na apscisi, odnosno je oblika (x,0).
	 * 
	 * @param x komponenta uz jedinični vektor "i"
	 */
	public Vector2D(double x) {
		this(x,0);  // ovaj konstruktor delegira izvođenje konstruktoru Vector2D(int,int)
	}
	
	/**
	 * Računa sumu trenutnog vektora i predanog vektora: this+other i vraća rezultat kao novi vektor.
	 * 
	 * @param other drugi vektor koji nadodajemo
	 * @return novi vektor koji je jednak sumi trenutnog i drugog
	 * @throws NullPointerException ako je other <code>null</code>
	 */
	public Vector2D add(Vector2D other) {
		Vector2D.addcounter++;
		return new Vector2D(this.x + other.x, this.y + other.y);
	}
	
	/**
	 * Računa razliku trenutnog vektora i predanog vektora: this-other i vraća rezultat kao novi vektor.
	 * 
	 * @param other drugi vektor koji oduzimamo
	 * @return novi vektor koji je jednak razlici trenutnog i drugog
	 */
	public Vector2D sub(Vector2D other) {
		return new Vector2D(this.x - other.x, this.y - other.y);
	}

	public double dotProduct(Vector2D other) {
		return this.x*other.x + this.y*other.y;
	}
	
	public double norm() {
		//return sqrt(x*x+y*y);
		return hypot(x,y);
	}
	
	public Vector2D normalized() {
		double n = this.norm();
		return new Vector2D(x/n, y/n);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Vector2D))
			return false;
		
		Vector2D other = (Vector2D) obj;

		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	public String toString() { // (3.0, 5.1)
		return String.format("(%f, %f)", x, y);
	}
	
	public static Vector2D of(double x, double y) {
		if(x==1.0 && y==0.0) return I;
		if(x==0.0 && y==1.0) return J;
		return new Vector2D(x, y);
	}

	public static Vector2D average(Vector2D ... vectors) {
		if(vectors.length == 0) {
			throw new IllegalArgumentException("Morate predati barem jedan vektor!");
		}
		
		double sx = 0, sy = 0;
		for(Vector2D v : vectors) {
			sx += v.x;
			sy += v.y; 
		}
		
		return new Vector2D(sx/vectors.length, sy/vectors.length);
	}
}
