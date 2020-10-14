package oprpp1.p03.primjer1;

import static oprpp1.p03.primjer1.Vector2D.of;

public class Demo {
    public static void main(String[] args ){
        System.out.println("Delaracija 1");
        Vector2D v1 = new Vector2D(3,4);

        System.out.println("Delaracija 2");
        Vector2D v2 = of(-4,2);

        System.out.println("Operacija 1");
        Vector2D v3 = v1.add(v2); // v3 = v1+v2

        System.out.println("Operacija 2");
        var v4 = v1.sub(v2);
    }
}
