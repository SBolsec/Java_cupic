package oprpp1.p03.primjer1;

import java.util.Objects;

import static java.lang.Math.hypot;

/**
 * Razred predstavlja model nepromjenjivog 2D-vektora.
 *
 * @author sbolsec
 */
public class Vector2D {

    public static int counter = 0;

    public static final Vector2D I = new Vector2D(1,0);
    public static final Vector2D J = new Vector2D(0,1);

    private final double x;
    private final double y;

    /**
     * Stvara novi opceniti 2D vektor cije su komponente x i y.
     *
     * @param x komponenta uz jedinicni vektor "i"
     * @param y komponenta uz jedinicni vektor "j"
     */
    public Vector2D(double x, double y) {
        super();
        this.x = Objects.requireNonNull(x);
        this.y = Objects.requireNonNull(y);

        System.out.println("Stvaram vektor " + this + ", counter = " + ++counter);
    }

    /**
     * Stvara novi vektor koji lezi na apscisi, odnoso je oblika (x, 0).
     *
     * @param x komponenta uz jedinicni vektor "i"
     */
    public Vector2D(double x) {
        this(x, 0); // ovaj konstruktor delegira izvodenje konstruktoru Vector2D(double,double)
    }

    /**
     * Racuna sumu trenutnog vektora i predanog vektora: this+other i vraca rezultat kao novi vektor.
     *
     * @param other drugi vektor koji nadodajemo
     * @return novi vektor koji je jednak sumi trenutnog i drugog
     * @throws NullPointerException ako je other <code>null</code>
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    /**
     * Racuna razliku trenutnog vektora i predanog vektora: this-other i vraca rezultat kao novi vektor.
     *
     * @param other drugi vektor koji nadodajemo
     * @return novi vektor koji je jednak razlici trenutnog i drugog
     * @throws NullPointerException ako je other <code>null</code>
     */
    public Vector2D sub(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public double dotProduct(Vector2D other) {
        return this.x * other.x + this.y + other.y;
    }

    public double norm() {
        // return sqrt(x*x + y*y);
        return hypot(x, y);
    }

    public Vector2D normalized() {
        double n = this.norm();
        return new Vector2D(x / n, y / n);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Vector2D))
            return false;
        Vector2D other = (Vector2D) o;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
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
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public static Vector2D of(double x, double y) {
        // Cache-iramo objekte
        if (x==1 && y==0) return I;
        if (x==0 && y==1) return J;
        return new Vector2D(x, y);
    }

    public static Vector2D average(Vector2D... vectors) {
        if (vectors.length == 0)
            throw new IllegalArgumentException("Morate predati barem jedan vektor!");

        double sx = 0, sy = 0;
        for (Vector2D vector : vectors) {
            sx += vector.x;
            sy += vector.y;
        }

        return new Vector2D(sx / vectors.length, sy / vectors.length);
    }
}
