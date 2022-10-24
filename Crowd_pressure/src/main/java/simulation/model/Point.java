package simulation.model;

public class Point {

    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point add(Point point){
        return new Point(x+point.x, y+point.y);
    }

    public Point subtract(Point point){
        return new Point(x-point.x, y-point.y);
    }

    public Vector toVector(){
        return new Vector(Math.sqrt(x*x + y*y), Math.atan2(y, x));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point point){
            return point.x == x && point.y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Point("+ x + ", " + y + ')';
    }
}
