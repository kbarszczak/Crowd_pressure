package simulation.model;

public class Vector {

    private double value; // non negative
    private double angle; // in range from 0 to 2PI

    public Vector(double value, double angle) {
        setValue(value);
        setAngle(angle);
    }

    public double getValue() {
        return value;
    }

    public double getAngle() {
        return angle;
    }

    public void setValue(double value) {
        if(value < 0) throw new IllegalStateException("Value cannot be negative");
        this.value = value;
    }

    public void setAngle(double angle) {
        while (angle < 0){
            angle += 2*Math.PI;
        }
        while (angle > 2*Math.PI){
            angle -= 2*Math.PI;
        }
        this.angle = angle;
    }

    public Vector add(Vector vector){
        return new Vector(
                Math.sqrt(Math.pow(value, 2) + Math.pow(vector.value, 2) + 2*value*vector.value*Math.cos(vector.angle - angle)),
                angle + Math.atan2(vector.value*Math.sin(vector.angle - angle), value + vector.value*Math.cos(vector.angle - angle))
        );
    }

    public Vector subtract(Vector vector){
        return new Vector(
                Math.sqrt(Math.pow(value, 2) + Math.pow(vector.value, 2) - 2*value*vector.value*Math.cos(angle - vector.angle)),
                angle - Math.atan2(vector.value*Math.sin(vector.angle - angle), value - vector.value*Math.cos(vector.angle - angle))
        );
    }

    public Vector multiplyByConstantCopy(double constant){
        return new Vector(value*constant, angle);
    }

    public void multiplyByConstant(double constant){
        value *= constant;
    }

    public Point toPoint() {
        return new Point((Math.cos(angle) * value), (Math.sin(angle) * value));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vector vector){
            return Double.compare(vector.value, value) == 0 && Double.compare(vector.angle, angle) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Vector(" + value + ", " + angle + ')';
    }
}