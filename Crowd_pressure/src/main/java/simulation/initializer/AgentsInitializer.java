package simulation.initializer;

import simulation.model.Agent;
import simulation.model.Board;

import java.util.List;

public interface AgentsInitializer {

    /**
     * The method is responsible to initialize the agent
     * @param board the board that the agents belong to
     * @return the list of initialized agents
     * @throws Exception the exception is thrown whenever any error occurs
     */
    List<Agent> initialize(Board board) throws Exception;
}
