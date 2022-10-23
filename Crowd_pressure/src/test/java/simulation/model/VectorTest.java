package simulation.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VectorTest {

    private Vector vector1;
    private Vector vector2;
    private Vector vector3;
    private Vector vector4;
    private Vector vector5;
    private Vector vector6;

    @BeforeEach
    void setUp() {
        vector1 = new Vector(1, Math.PI/4); // (sqrt(2)/2, sqrt(2)/2)
        vector2 = new Vector(5, Math.atan(4/(double)3)); // (3, 4)
        vector3 = new Vector(4, 5*Math.PI/6); // (-2sqrt(3), 2)
        vector4 = new Vector(Math.sqrt(2), Math.PI * 5/4); // (-1, -1)
        vector5 = new Vector(2, Math.PI * 10/6); // (1, sqrt(3))
        vector6 = new Vector((10*Math.sqrt(3))/3.0, Math.PI * 11/6); // (5, (5/3)*sqrt(3) )
    }

    @Test
    void add() {
        // vector 1 + vector 2
        Vector vector12 = vector1.add(vector2);
        Assertions.assertEquals(vector12.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector12.getAngle(), 0, 10^-6);

        // vector 2 + vector 4
        Vector vector24 = vector1.add(vector2);
        Assertions.assertEquals(vector24.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector24.getAngle(), 0, 10^-6);

        // vector 2 + vector 5
        Vector vector25 = vector1.add(vector2);
        Assertions.assertEquals(vector25.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector25.getAngle(), 0, 10^-6);

        // vector 1 + vector 6
        Vector vector16 = vector1.add(vector2);
        Assertions.assertEquals(vector16.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector16.getAngle(), 0, 10^-6);

        // vector 3 + vector 1
        Vector vector31 = vector1.add(vector2);
        Assertions.assertEquals(vector31.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector31.getAngle(), 0, 10^-6);

        // vector 3 + vector 5
        Vector vector35 = vector1.add(vector2);
        Assertions.assertEquals(vector35.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector35.getAngle(), 0, 10^-6);

        // vector 2 + vector 3
        Vector vector23 = vector1.add(vector2);
        Assertions.assertEquals(vector23.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector23.getAngle(), 0, 10^-6);

        // vector 3 + vector 3
        Vector vector33 = vector1.add(vector2);
        Assertions.assertEquals(vector33.getValue(), 0, 10^-6);
        Assertions.assertEquals(vector33.getAngle(), 0, 10^-6);
    }

    @Test
    void subtract() {
        // vector2 - vector4
        Vector result1 = vector2.subtract(vector4);
        Assertions.assertEquals(result1.getValue(), Math.sqrt(41), 10^-6);
        Assertions.assertEquals(result1.getAngle(), Math.atan(5/(double)4));
    }

    @Test
    void multiplyByConstantCopy() {

    }

    @Test
    void multiplyByConstant() {

    }

    @Test
    void toPoint() {

    }

    @Test
    void testEquals() {

    }
}