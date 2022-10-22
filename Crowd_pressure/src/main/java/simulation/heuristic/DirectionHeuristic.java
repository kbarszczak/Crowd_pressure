package simulation.heuristic;

import simulation.model.Agent;
import simulation.model.Board;
import utils.MathUtil;

import java.util.List;

public class DirectionHeuristic implements Heuristic{

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        double step = 1;
        double bestDistance = agent.getAgentMaxVisionDistance();
        double bestAngle = agent.getVelocity().getAngle();
        for(double angle = (agent.getVelocity().getAngle()-agent.getAgentVisionAngle()); angle <=  (agent.getVelocity().getAngle()+agent.getAgentVisionAngle()); angle += step){
            if(angle < 0) angle += (2 * Math.PI);

            double distance = calculateDistance(angle, agent, allAgents, board);
            if(distance < bestDistance){
                bestDistance = distance;
                bestAngle = angle;
            }
        }
        agent.getDesiredVelocity().setAngle(bestAngle);
    }

    private double calculateDistance(double angleToCheck, Agent agent, List<Agent> allAgents, Board board){
        double distanceToCollision = MathUtil.calculateDistanceToCollision(angleToCheck, agent, allAgents, board);
        return Math.pow(agent.getAgentMaxVisionDistance(), 2) +
                Math.pow(distanceToCollision, 2) -
                2*agent.getAgentMaxVisionDistance()*distanceToCollision *
                        Math.cos(MathUtil.calculateMutualAngle(agent.getPosition(), agent.getAgentDesiredPosition()) - angleToCheck);
    }

}
