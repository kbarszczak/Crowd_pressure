package utils;

import simulation.model.*;

import java.util.List;

public class MathUtil {

    private MathUtil(){}

    public static double calculateLineCoefficient(Point point1, Point point2){
        return (point1.getY() - point2.getY()) / (double)(point1.getX() - point2.getX());
    }

    // powinien być wektor skierowany z p1 do p2
    public static double calculateMutualAngle(Point point1, Point point2){
        double angle = Math.atan2(point2.getY() - point1.getY(), point2.getX() - point1.getX());
        if(angle < 0){
            angle += 2*Math.PI;
        }
        return angle;
    }

    public static double calculateDistanceBetweenPoints(Point point1, Point point2){
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }

    public static Point getCrossingPoint(Point sourcePoint, double sourceAngle, Point straightStart, Point straightEnd){
//        double x0 = sourcePoint.getX(), y0 = sourcePoint.getY();
//        double x1 = straightStart.getX(), y1 = straightStart.getY();
//        double tga = Math.tan(sourceAngle);
//        double lineCoefficient = calculateLineCoefficient(straightStart, straightEnd);
//
//        double x = (y1 - lineCoefficient*x1 - y0 + x0*tga) / (tga - lineCoefficient);
//        double y = tga*x + y0 - tga*x0;
//
//        return new Point((int)x, (int)y);
        Point sourcePoint2 = new Vector(1, sourceAngle).toPoint();
        double x1 = sourcePoint.getX();
        double y1 = sourcePoint.getY();
        double x2 = sourcePoint2.getX();
        double y2 = sourcePoint2.getY();
        double x3 = straightStart.getX();
        double y3 = straightStart.getY();
        double x4 = straightEnd.getX();
        double y4 = straightEnd.getY();

        double tmp = ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
        double x = ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4))/tmp;
        double y = ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4))/tmp;

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
