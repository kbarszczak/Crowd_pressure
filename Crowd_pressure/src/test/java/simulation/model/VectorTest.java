package simulation.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VectorTest {

    private final double delta = 0.001;
    private Vector vector1;
    private Vector vector2;
    private Vector vector3;
    private Vector vector4;
    private Vector vector5;
    private Vector vector6;

    private Vector uVector1;
    private Vector uVector2;
    private Vector uVector3;
    private Vector uVector4;
    private Vector uVector5;

    @BeforeEach
    void setUp() {
        vector1 = new Vector(1, Math.PI/4); // (sqrt(2)/2, sqrt(2)/2)
        vector2 = new Vector(5, Math.atan(4/3.0)); // (3, 4)
        vector3 = new Vector(4, 5*Math.PI/6); // (-2sqrt(3), 2)
        vector4 = new Vector(Math.sqrt(2), Math.PI * 5/4); // (-1, -1)
        vector5 = new Vector(2, Math.PI * 10/6); // (1, -sqrt(3))
        vector6 = new Vector((10*Math.sqrt(3))/3.0, Math.PI * 11/6); // (5, -(5/3)*sqrt(3) )

        uVector1 = new Vector(Math.sqrt(2), Math.PI/4.0); // (1, 1)
        uVector2 = new Vector(2*Math.sqrt(2), Math.PI* 3.0/ 4.0); // (-2, 2)
        uVector3 = new Vector(3*Math.sqrt(2), Math.PI*5.0/4.0); // (-3, -3)
        uVector4 = new Vector(4*Math.sqrt(2), Math.PI*7.0/4.0); // (4, -4)
        uVector5 = new Vector(4*Math.sqrt(2), Math.PI*7.0/4.0); // (4, -4)
    }

    @Test
    void add() {
        // vector 1 + vector 2
        Vector vector12 = vector1.add(vector2);
        Assertions.assertEquals(5.99162, vector12.getValue(), delta);
        Assertions.assertEquals(0.9036895988, vector12.getAngle(), delta);

        // vector 2 + vector 6
        Vector vector26 = vector2.add(vector6);
        Assertions.assertEquals(8.07709, vector26.getValue(),delta);
        Assertions.assertEquals(0.1382681249, vector26.getAngle(), delta);

        // vector 2 + vector 3
        Vector vector23 = vector2.add(vector3);
        Assertions.assertEquals(6.01792, vector23.getValue(), delta);
        Assertions.assertEquals(1.64799224, vector23.getAngle(), delta);

        // vector 3 + vector 4
        Vector vector34 = vector3.add(vector4);
        Assertions.assertEquals(4.57474, vector34.getValue(), delta);
        Assertions.assertEquals(2.921227382, vector34.getAngle(), delta);

        // uVector1 + uVector3
        Vector uVector13 = uVector1.add(uVector3);
        Assertions.assertEquals(2*Math.sqrt(2), uVector13.getValue(), delta);
        Assertions.assertEquals(Math.PI*5.0/4.0, uVector13.getAngle(), delta);

        // uVector4 + uVector3
        Vector uVector43 = uVector4.add(uVector3);
        Assertions.assertEquals(7.07107, uVector43.getValue(), delta);
        Assertions.assertEquals(4.8542859939, uVector43.getAngle(), delta);
    }

    @Test
    void subtract() {
        // vector2 - vector4
        Vector result1 = vector2.subtract(vector4);
        Assertions.assertEquals(Math.sqrt(41), result1.getValue(), delta);
        Assertions.assertEquals(Math.atan(5/(double)4), result1.getAngle(), delta);

        // vector6 - vector1
        Vector result2 = vector6.subtract(vector1);
        Assertions.assertEquals(5.59864, result2.getValue(), delta);
        Assertions.assertEquals(5.5861898157, result2.getAngle(), delta);

        // vector3 - vector5
        Vector result3 = vector3.subtract(vector5);
        Assertions.assertEquals(5.81863, result3.getValue(), delta);
        Assertions.assertEquals(2.445276095, result3.getAngle(), delta);

        // uVector2 - uVector1
        Vector result4 = uVector2.subtract(uVector1);
        Assertions.assertEquals(Math.sqrt(10), result4.getValue(), delta);
        Assertions.assertEquals(2.8198, result4.getAngle(), delta);
    }

    @Test
    void multiplyByConstantCopy() {
        Vector vector = uVector1.multiplyByConstantCopy(3);
        Assertions.assertNotEquals(vector, uVector1);
        Assertions.assertEquals(3*Math.sqrt(2), vector.getValue(), delta);
        Assertions.assertEquals(vector.getAngle(), uVector1.getAngle(), delta);
    }

    @Test
    void multiplyByConstant() {
        uVector5.multiplyByConstant(2);
        Assertions.assertEquals(8*Math.sqrt(2), uVector5.getValue(), delta);
    }

    @Test
    void toPoint() {
        Point point1 = uVector1.toPoint();
        Assertions.assertEquals(1, point1.getX(), delta);
        Assertions.assertEquals(1, point1.getY(), delta);

        Point point2 = uVector2.toPoint();
        Assertions.assertEquals(-2, point2.getX(), delta);
        Assertions.assertEquals(2, point2.getY(), delta);

    }

    @Test
    void testEquals(){
        Vector v1 = null;
        Vector v2 = new Vector(1, 23);
        Vector v3 = new Vector(1, 23-Math.PI*2);
        Vector v4 = new Vector(1.2, 23-Math.PI*2);

        Assertions.assertEquals(v2, v3);
        Assertions.assertNotEquals(v2, v4);
        Assertions.assertNotEquals(v1, v4);
    }
}