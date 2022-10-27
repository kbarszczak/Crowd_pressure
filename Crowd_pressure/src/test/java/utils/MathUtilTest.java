package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilTest {

    private final double delta = 0.001;
    private Point point1;
    private Point point2;
    private Point point3;
    private Point point4;

    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Point pointD;
    private Point pointP;

    @BeforeEach
    void setUp() {
        point1 = new Point(3, 4);
        point2 = new Point(4, 3);
        point3 = new Point(-3, 4);
        point4 = new Point(4, -3);

        pointA = new Point(-5, -2);
        pointB = new Point(-2, -4);
        pointC = new Point(-5, -6);
        pointD = new Point(-8, -4);
        pointP = new Point(-5, -4);
    }

    @Test
    void calculateLineCoefficient() {
        double result1 = MathUtil.calculateLineCoefficient(point1, point2);
        Assertions.assertEquals(-1, result1, delta);
        double result2 = MathUtil.calculateLineCoefficient(point3, point2);
        Assertions.assertEquals(-1/7.0, result2, delta);

        double result3 = MathUtil.calculateLineCoefficient(pointC, pointB);
        double result4 = MathUtil.calculateLineCoefficient(pointB, pointC);
        Assertions.assertEquals(2/3.0, result3, delta);
        Assertions.assertEquals(2/3.0, result4, delta);

        double result5 = MathUtil.calculateLineCoefficient(pointA, pointB);
        double result6 = MathUtil.calculateLineCoefficient(pointD, pointC);
        Assertions.assertEquals(-2/3.0, result5, delta);
        Assertions.assertEquals(-2/3.0, result6, delta);

        double result7 = MathUtil.calculateLineCoefficient(pointD, pointP);
        Assertions.assertEquals(0, result7, delta);

        double result8 = MathUtil.calculateLineCoefficient(pointC, pointP);
        Assertions.assertEquals(Double.NEGATIVE_INFINITY, result8, delta);
    }

    @Test
    void calculateMutualAngle() {
        double result1 = MathUtil.calculateMutualAngle(point1, point2);
        Assertions.assertEquals(7*Math.PI/4.0, result1, delta);

        double result2 = MathUtil.calculateMutualAngle(point3, point1);
        Assertions.assertEquals(0, result2, delta);

        double result3 = MathUtil.calculateMutualAngle(pointC, pointD);
        Assertions.assertEquals(-0.5880026+Math.PI, result3, delta);

        double result4 = MathUtil.calculateMutualAngle(pointC, pointB);
        Assertions.assertEquals(0.5880026, result4, delta);

        double result5 = MathUtil.calculateMutualAngle(pointB, pointC);
        Assertions.assertEquals(0.5880026+Math.PI, result5, delta);

        double result6 = MathUtil.calculateMutualAngle(pointA, pointP);
        Assertions.assertEquals(3*Math.PI/2.0, result6, delta);

        double result7 = MathUtil.calculateMutualAngle(pointP, pointB);
        Assertions.assertEquals(0, result7, delta);

    }

    @Test
    void calculateDistanceBetweenPoints() {
        double result1 = MathUtil.calculateDistanceBetweenPoints(point1, point3);
        Assertions.assertEquals(6, result1, delta);

        double result2 = MathUtil.calculateDistanceBetweenPoints(point1, point2);
        Assertions.assertEquals(Math.sqrt(2), result2, delta);

        double result3 = MathUtil.calculateDistanceBetweenPoints(pointP, pointC);
        Assertions.assertEquals(2, result3, delta);

        double result4 = MathUtil.calculateDistanceBetweenPoints(pointD, pointP);
        Assertions.assertEquals(3, result4, delta);

        double result5 = MathUtil.calculateDistanceBetweenPoints(pointA, pointB);
        Assertions.assertEquals(Math.sqrt(13), result5, delta);
    }

    @Test
    void getCrossingPoint() {
        Point result1 = MathUtil.getCrossingPoint(point2, 5/4.0*Math.PI, point3, point4);
        Assertions.assertEquals(new Point(1, 0), result1);

        Point result3 = MathUtil.getCrossingPoint(pointP, Math.PI/2.0, pointD, new Point(-13/2.0, -3));
        Assertions.assertEquals(pointA, result3);

        Point result4 = MathUtil.getCrossingPoint(pointP, Math.PI, pointC, new Point(-13/2.0, -5));
        Assertions.assertEquals(pointD, result4);
    }

    @Test
    void getCrossingPointInShortestPath() {
        Point result1 = MathUtil.getCrossingPointInShortestPath(point2, point3, point4);
        Assertions.assertEquals(new Point(1, 0), result1);

        Point result2 = MathUtil.getCrossingPointInShortestPath(point1, point3, point4);
        Assertions.assertEquals(new Point(0, 1), result2);
    }

    @Test
    void calculateDistanceToCollision() {
        Agent agent = new Agent(
                new Point(3, 1),
                60,
                1,
                6,
                150,
                450,
                0.5,
                new Point(100, 200)
        );
        List<Agent> agentList = List.of(agent, new Agent(new Point(2, 0), 60, 0.5, 6, 150, 450, 0.5, new Point(100, 200)));
        Wall wall1 = new Wall(new Point(4.95, 1.05), new Point(2, 4));
        Wall wall2 = new Wall(new Point(0, 0), new Point(3, 3));
        Board board = new Board(300, 300, List.of(wall1, wall2));

        double result1 = MathUtil.calculateDistanceToCollision(0, agent, agentList, board);
        Assertions.assertEquals(450, result1, delta);

        double result2 = MathUtil.calculateDistanceToCollision(Math.PI*1/4.0, agent, agentList, board);
        Assertions.assertEquals(Math.sqrt(2), result2, delta);

        double result3 = MathUtil.calculateDistanceToCollision(Math.PI*3/4.0, agent, agentList, board);
        Assertions.assertEquals(Math.sqrt(2), result3, delta);

        double result4 = MathUtil.calculateDistanceToCollision(Math.PI, agent, agentList, board);
        Assertions.assertEquals(2, result4, delta);

        double result5 = MathUtil.calculateDistanceToCollision(Math.PI*5/4.0, agent, agentList, board);
        Assertions.assertEquals(Math.sqrt(2), result5, delta);

        double result6 = MathUtil.calculateDistanceToCollision(Math.PI*5/4.0 + Math.PI/180.0*3, agent, agentList, board);
        Assertions.assertEquals(Math.sqrt(2), result6, delta);
    }
}