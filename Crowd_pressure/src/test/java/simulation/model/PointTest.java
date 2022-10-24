package simulation.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    private final double delta = 0.001;
    private Point point1;
    private Point point2;
    private Point point3;
    private Point point4;

    @BeforeEach
    void setUp() {
        point1 = new Point(1, 1);
        point2 = new Point(-2, 2);
        point3 = new Point(-3, -3);
        point4 = new Point(4, -4);
    }

    @Test
    void add() {
        Point result1 = point1.add(point2);
        assertEquals(-1, result1.getX());
        assertEquals(3, result1.getY());

        Point result2 = point3.add(point4);
        assertEquals(1, result2.getX());
        assertEquals(-7, result2.getY());

        Point result3 = point2.add(point4);
        assertEquals(2, result3.getX());
        assertEquals(-2, result3.getY());

        Point result4 = point1.add(point4);
        assertEquals(5, result4.getX());
        assertEquals(-3, result4.getY());
    }

    @Test
    void subtract() {
        Point result1 = point1.subtract(point2);
        assertEquals(3, result1.getX(), delta);
        assertEquals(-1, result1.getY(), delta);

        Point result2 = point4.subtract(point3);
        assertEquals(7, result2.getX(), delta);
        assertEquals(-1, result2.getY(), delta);
    }

    @Test
    void toVector() {
        Vector result1 = point1.toVector();
        Assertions.assertEquals(Math.sqrt(2), result1.getValue(), delta);
        Assertions.assertEquals(Math.PI/4.0, result1.getAngle(), delta);

        Vector result2 = point2.toVector();
        Assertions.assertEquals(2*Math.sqrt(2), result2.getValue(), delta);
        Assertions.assertEquals(Math.PI* 3.0/ 4.0, result2.getAngle(), delta);

        Vector result3 = point3.toVector();
        Assertions.assertEquals(3*Math.sqrt(2), result3.getValue(), delta);
        Assertions.assertEquals(Math.PI*5.0/4.0, result3.getAngle(), delta);

        Vector result4 = point4.toVector();
        Assertions.assertEquals(4*Math.sqrt(2), result4.getValue(), delta);
        Assertions.assertEquals(Math.PI*7.0/4.0, result4.getAngle(), delta);

    }

    @Test
    void testEquals() {
        Point p1 = null;
        Point p2 = new Point(1, 1);
        Point p3 = new Point(1, 1);
        Point p4 = new Point(4, 1);

        Assertions.assertEquals(p2, p3);
        Assertions.assertNotEquals(p2, p4);
        Assertions.assertNotEquals(p1, p4);
    }
}