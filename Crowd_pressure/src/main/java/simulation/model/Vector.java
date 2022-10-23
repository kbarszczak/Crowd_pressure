package simulation.model;

public class Vector {

    private double value;
    private double angle;

    public Vector(double value, double angle) {
        this.value = value;
        this.angle = angle;
    }

    public double getValue() {
        return value;
    }

    public double getAngle() {
        return angle;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Point toPoint() {
        return new Point((int)(Math.cos(angle) * value), (int)(Math.sin(angle) * value));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector vector){
            return Double.compare(vector.value, value) == 0 && Double.compare(vector.angle, angle) == 0;
        }
        return false;
    }
}
