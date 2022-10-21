package simulation.physics;

import simulation.model.Agent;
import simulation.model.Board;

import java.util.List;

public interface PhysicalModel {

    /**
     * The method is responsible to apply physics on the given agent
     * @param agent the agent that the physics will be applied on
     * @param allAgents all the other agents that exists in the simulation (possibly the given agent is also in that list)
     * @param board the simulation board
     * @throws Exception the exception is thrown whenever any error occurs
     */
    void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception;

}
