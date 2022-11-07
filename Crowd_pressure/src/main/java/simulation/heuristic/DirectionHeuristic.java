package simulation.heuristic;

import simulation.model.Agent;
import simulation.model.Board;
import utils.MathUtil;

import java.util.List;

public class DirectionHeuristic implements Heuristic {

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        double bestAngle = agent.getVelocity().getAngle(), step = 0.0174533; // 1 degree
        Double bestDistance = null;

        for (double angle = (agent.getVelocity().getAngle() - agent.getAgentVisionAngle()); angle <= (agent.getVelocity().getAngle() + agent.getAgentVisionAngle()); angle += step) {
            double angleNormalized = angle;
            while (angleNormalized < 0) angleNormalized += (Math.PI * 2);
            while (angleNormalized >= 2 * Math.PI) angleNormalized -= (Math.PI * 2);

            double distance = calculateDistance(angleNormalized, agent, allAgents, board);
            if (bestDistance == null || distance < bestDistance) {
                bestDistance = distance;
                bestAngle = angleNormalized;
            }
        }
        agent.getDesiredVelocity().setAngle(bestAngle);
    }


    private double calculateDistance(double angleToCheck, Agent agent, List<Agent> allAgents, Board board) {
        double distanceToCollision = MathUtil.calculateDistanceToCollision(angleToCheck, agent, allAgents, board);
        return Math.pow(agent.getAgentMaxVisionDistance(), 2) +
                Math.pow(distanceToCollision, 2) -
                2 * agent.getAgentMaxVisionDistance() * distanceToCollision *
                        Math.cos(MathUtil.calculateMutualAngle(agent.getPosition(), agent.getAgentDesiredPosition()) - angleToCheck);
    }

}
