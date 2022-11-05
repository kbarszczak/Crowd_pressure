package utils;

import simulation.model.*;

import java.util.List;

public class MathUtil {

    private MathUtil(){}

    public static double calculateLineCoefficient(Point point1, Point point2){
        return (point1.getY() - point2.getY()) / (point1.getX() - point2.getX());
    }

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
        double x1 = sourcePoint.getX(), y1 = sourcePoint.getY();
        double x2 = -1, y2 = Math.tan(sourceAngle) * (-1) + (y1 - Math.tan(sourceAngle)*x1);
        double x3 = straightStart.getX(), y3 = straightStart.getY();
        double x4 = straightEnd.getX(), y4 = straightEnd.getY();

        double tmp = ((x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4));
        double x = ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4))/tmp;
        double y = ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4))/tmp;

        return new Point(x, y);
    }

    public static Point getCrossingPointInShortestPath(Point sourcePoint, Point straightStart, Point straightEnd){
        double lineCoefficient = calculateLineCoefficient(straightStart, straightEnd);
        return getCrossingPoint(sourcePoint, Math.atan(-1/lineCoefficient), straightStart, straightEnd);
    }

    public static boolean isAngleBetween(double angleToVerify, double angle1, double angle2){
        double a = Math.min(angle1, angle2);
        double b = Math.max(angle1, angle2);
        if((b-a) > Math.PI) return ((angleToVerify > b) || (angleToVerify < a));
        return ((angleToVerify > a) && (angleToVerify < b));
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

//        System.out.printf("\nCheck walls for angle [%f] from (%d, %d):\n", angleToCheck*180/Math.PI, (int)agent.getPosition().getX(), (int)agent.getPosition().getY());

        // check walls
        for(Wall wall : board.getWalls()){
            double mutualAngleStart = MathUtil.calculateMutualAngle(agent.getPosition(), wall.getStartPoint());
            double mutualAngleEnd = MathUtil.calculateMutualAngle(agent.getPosition(), wall.getEndPoint());


//            System.out.printf("Start (%f) End (%f)\n", mutualAngleStart*180/Math.PI, mutualAngleEnd*180/Math.PI);

            if(isAngleBetween(angleToCheck, mutualAngleStart, mutualAngleEnd)){
//                System.out.printf("Crossing wall: (%d, %d) --- (%d, %d)\n", (int)wall.getStartPoint().getX(), (int)wall.getStartPoint().getY(), (int)wall.getEndPoint().getX(), (int)wall.getEndPoint().getY());

                Point crossingPoint = MathUtil.getCrossingPoint(agent.getPosition(), angleToCheck, wall.getStartPoint(), wall.getEndPoint());
                double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), crossingPoint);
                distanceToCollision = Math.min(distance, distanceToCollision);
            }else{
//                System.out.printf("Not crossing wall: (%d, %d) --- (%d, %d)\n", (int)wall.getStartPoint().getX(), (int)wall.getStartPoint().getY(), (int)wall.getEndPoint().getX(), (int)wall.getEndPoint().getY());
            }
        }

//        System.out.println("-------------------------------------");

        return distanceToCollision;
    }
}