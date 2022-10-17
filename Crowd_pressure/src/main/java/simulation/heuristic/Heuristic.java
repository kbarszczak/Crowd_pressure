package simulation.heuristic;

import simulation.model.Agent;
import simulation.model.Board;

import java.util.List;

public interface Heuristic {

    void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception;

}
