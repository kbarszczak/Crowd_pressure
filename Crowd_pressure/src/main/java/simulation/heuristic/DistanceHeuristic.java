package simulation.heuristic;

import simulation.model.Agent;
import simulation.model.Board;
import utils.MathUtil;

import java.util.List;

public class DistanceHeuristic implements Heuristic{

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        agent.getDesiredVelocity().setValue(
                Math.min(
                        agent.getAgentComfortableSpeed(),
                        MathUtil.calculateDistanceToCollision(agent.getVelocity().getAngle(), agent, allAgents, board) / agent.getAgentRelaxationTime()
                )
        );
    }
}
