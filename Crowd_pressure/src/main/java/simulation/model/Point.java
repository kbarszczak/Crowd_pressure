package simulation.model;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vector toVector(){
        return new Vector(Math.sqrt(x*x + y*y), Math.atan(y/(double)x));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point point){
            return point.x == x && point.y == y;
        }
        return false;
    }
}
