public class Vector2D {
    double x;
    double y;
    Vector2D (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Vector2D v) {
        double dx = x - v.x;
        double dy = y - v.y;

        double d = java.lang.Math.hypot(dx,dy);
        //System.out.println(d);
        return d;
    }

    public Vector2D add(Vector2D v) {
        double ax = x + v.x;
        double ay = y + v.y;
        Vector2D sum = new Vector2D(ax,ay);
        //System.out.println(sum.x + " " + sum.y);
        return sum;
    }

    public Vector2D scale (double f) {
        double sx = f*x;
        double sy = f*y;
        Vector2D scaled = new Vector2D(sx,sy);
        //System.out.println(scaled.x + " " + scaled.y);
        return scaled;
    }

    public String toString() {
        String s = ("(" + x + "," + y + ")");
        System.out.println(s);
        return s;
    }


}
