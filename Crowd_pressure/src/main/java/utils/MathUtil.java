package utils;

import simulation.model.*;

import java.util.List;

// TODO: write tests for all static functions

public class MathUtil {

    private MathUtil(){}

    public static double calculateLineCoefficient(Point point1, Point point2){
        return (point1.getY() - point2.getY()) / (double)(point1.getX() - point2.getX());
    }

    public static double calculateMutualAngle(Point point1, Point point2){
        return Math.atan(calculateLineCoefficient(point1, point2));
    }

    public static double calculateDistanceBetweenPoints(Point point1, Point point2){
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }

    public static Point getCrossingPoint(Point sourcePoint, double sourceAngle, Point straightStart, Point straightEnd){
        double x0 = sourcePoint.getX(), y0 = sourcePoint.getY();
        double x1 = straightStart.getX(), y1 = straightStart.getY();
        double tga = Math.tan(sourceAngle);
        double lineCoefficient = calculateLineCoefficient(straightStart, straightEnd);

        double x = (y1 - lineCoefficient*x1 - y0 + x0*tga) / (tga - lineCoefficient);
        double y = tga*x + y0 - tga*x0;

        return new Point((int)x, (int)y);
    }

    public static Point getCrossingPointInShortestPath(Point sourcePoint, Point straightStart, Point straightEnd){
        double lineCoefficient = calculateLineCoefficient(straightStart, straightEnd);
        return getCrossingPoint(sourcePoint, -1/lineCoefficient, straightStart, straightEnd);
    }
    
    public static double calculateDistanceToCollision(double angleToCheck, Agent agent, List<Agent> agents, Board board){
        // check obstacles
        double distanceToCollision = agent.getAgentMaxVisionDistance();
        for(Agent obstacle : agents){
            if(agent == obstacle) continue;
            double mutualAngle = MathUtil.calculateMutualAngle(agent.getPosition(), obstacle.getPosition());
            double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), obstacle.getPosition()); // todo: calculate accurate distance
            double relaxationAngle = Math.atan(obstacle.getAgentRadius() / distance);

            if(angleToCheck >= mutualAngle - relaxationAngle && angleToCheck <= mutualAngle + relaxationAngle ){
                distanceToCollision = Math.min(distance, distanceToCollision);
            }
        }

        // check walls
        for(Wall wall : board.getWalls()){
            double mutualAngleStart = MathUtil.calculateMutualAngle(agent.getPosition(), wall.getStartPoint());
            double mutualAngleEnd = MathUtil.calculateMutualAngle(agent.getPosition(), wall.getEndPoint());

            if(angleToCheck >= Math.min(mutualAngleStart, mutualAngleEnd) && angleToCheck <= Math.max(mutualAngleStart, mutualAngleEnd)){
                Point crossingPoint = MathUtil.getCrossingPoint(agent.getPosition(), angleToCheck, wall.getStartPoint(), wall.getEndPoint());
                double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), crossingPoint);
                distanceToCollision = Math.min(distance, distanceToCollision);
            }
        }

        return distanceToCollision;
    }

}
