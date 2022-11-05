package simulation.heuristic;

import simulation.model.Agent;
import simulation.model.Board;
import utils.MathUtil;

import java.util.List;

public class DirectionHeuristic implements Heuristic{

    private static String buf;
    private static String bufBest;

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        double step = 0.0174533; // 1 degree
        Double bestDistance = null;  // todo fix this shit
        double bestAngle = agent.getVelocity().getAngle();
        // todo: below for loop is slow - try to improve this
        for(double angle = (agent.getVelocity().getAngle()-agent.getAgentVisionAngle()); angle <= (agent.getVelocity().getAngle()+agent.getAgentVisionAngle()); angle += step){
            double angleNormalized = angle;
            while (angleNormalized < 0) angleNormalized += (Math.PI*2);
            while (angleNormalized >= 2*Math.PI) angleNormalized -= (Math.PI*2);

            double distance = calculateDistance(angleNormalized, agent, allAgents, board);
            if(bestDistance==null || distance < bestDistance){
                bestDistance = distance;
                bestAngle = angleNormalized;
                bufBest = buf;
            }
        }
//        System.out.println();
//        System.out.println(bufBest);
//        System.out.println();
        agent.getDesiredVelocity().setAngle(bestAngle);
    }

    private double calculateDistance(double angleToCheck, Agent agent, List<Agent> allAgents, Board board){
        double distanceToCollision = MathUtil.calculateDistanceToCollision(angleToCheck, agent, allAgents, board);

        buf = Double.toString(agent.getAgentMaxVisionDistance()) + "^2  +  " + Double.toString(distanceToCollision) + "^2  -  " + "2*" + agent.getAgentMaxVisionDistance() + "*" + distanceToCollision + "*" +
                "cos(" + ((MathUtil.calculateMutualAngle(agent.getPosition(), agent.getAgentDesiredPosition()) - angleToCheck)*180/Math.PI) + ")";

        double v = Math.pow(agent.getAgentMaxVisionDistance(), 2) +
                Math.pow(distanceToCollision, 2) -
                2*agent.getAgentMaxVisionDistance()*distanceToCollision *
                        Math.cos(MathUtil.calculateMutualAngle(agent.getPosition(), agent.getAgentDesiredPosition()) - angleToCheck);
        buf += " = " + v;

//        System.out.println(buf);

        return v;
    }

}
