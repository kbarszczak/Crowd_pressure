package utils;

import simulation.model.*;

import java.util.List;

public class MathUtil {

    private MathUtil(){}

    public static double calculateMutualAngle(Point point1, Point point2){ // TODO: write tests
        double tan = Math.tan(Math.abs(point1.getX() - point2.getX()) / (double)Math.abs(point1.getY() - point2.getY()));
        double angle = Math.atan(tan);
        double incrementor = 0;

        if(point1.getX() - point2.getX() >= 0 && point1.getY() - point2.getY() < 0){
            incrementor = Math.PI * 3/4;
        }else if(point1.getX() - point2.getX() < 0 && point1.getY() - point2.getY() >= 0){
            incrementor = Math.PI / 2;
        }else if(point1.getX() - point2.getX() < 0 && point1.getY() - point2.getY() < 0){
            incrementor = Math.PI;
        }

        return angle + incrementor;
    }

    public static double getDistance(Point point1, Point point2){
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }

    public static double calculateDistanceToCollision(double angleToCheck, Agent agent, List<Agent> agents, Board board){ // TODO: write tests
        // check obstacles
        double distanceToCollision = agent.getAgentMaxVisionDistance();
        for(Agent obstacle : agents){
            if(agent == obstacle) continue;
            double mutualAngle = MathUtil.calculateMutualAngle(agent.getPosition(), obstacle.getPosition());
            double distance = MathUtil.getDistance(agent.getPosition(), obstacle.getPosition()); // todo: calculate accurate distance
            double relaxationAngle = Math.atan(Math.tan(obstacle.getAgentRadius() / distance));

            if(angleToCheck >= mutualAngle - relaxationAngle && angleToCheck <= mutualAngle + relaxationAngle ){
                distanceToCollision = Math.min(distance, distanceToCollision);
            }
        }

        // check walls
        for(Wall wall : board.getWalls()){
            double mutualAngleStart = MathUtil.calculateMutualAngle(agent.getPosition(), wall.getStartPoint());
            double mutualAngleEnd = MathUtil.calculateMutualAngle(agent.getPosition(), wall.getEndPoint());

            if(angleToCheck >= Math.min(mutualAngleStart, mutualAngleEnd) && angleToCheck <= Math.max(mutualAngleStart, mutualAngleEnd)){
                double x0 = agent.getPosition().getX(), y0 = agent.getPosition().getY();
                double x1 = wall.getStartPoint().getX(), y1 = wall.getStartPoint().getY();
                double x2 = wall.getEndPoint().getX(), y2 = wall.getEndPoint().getY();
                double tga = Math.tan(angleToCheck);
                double tmp = (y1-y2) / (x1-x2);

                double x = (y1 - tmp*x1 - y0 + x0*tga) / (tga - tmp);
                double y = tga*x + y0 - tga*x0;

                Point crossingPoint = new Point((int)x, (int)y);
                double distance = MathUtil.getDistance(agent.getPosition(), crossingPoint);
                distanceToCollision = Math.min(distance, distanceToCollision);
            }
        }

        return distanceToCollision;
    }

    public static Vector

}
