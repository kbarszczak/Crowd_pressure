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

    public Vector add(Vector vector){
        return new Vector(
                Math.sqrt(Math.pow(value, 2) + Math.pow(vector.value, 2) + 2*value*vector.value*Math.cos(vector.angle - angle)),
                Math.atan2(vector.value*Math.sin(vector.angle - angle), value + vector.value*Math.cos(vector.angle - angle))
        );
    }

    public Vector subtract(Vector vector){
        return new Vector(
                Math.sqrt(Math.pow(value, 2) + Math.pow(vector.value, 2) - 2*value*vector.value*Math.cos(angle - vector.angle)),
                Math.atan((value*Math.sin(angle) - vector.value*Math.sin(vector.angle)) / (value*Math.cos(angle) - vector.value*Math.cos(vector.angle)))
        );
    }

    public Vector multiplyByConstantCopy(double constant){
        return new Vector(value*constant, angle);
    }

    public void multiplyByConstant(double constant){
        value *= constant;
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