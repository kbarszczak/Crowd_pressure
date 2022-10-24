package simulation.model;

public class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
