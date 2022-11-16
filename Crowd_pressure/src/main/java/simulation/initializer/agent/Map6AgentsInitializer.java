package simulation.initializer.agent;

import simulation.model.Agent;
import simulation.model.Board;

import java.util.List;

public class Map6AgentsInitializer implements AgentsInitializer{

    @Override
    public List<Agent> initialize(int agentCount, Board board) throws Exception {
        return new Map1AgentsInitializer().initialize(agentCount, board);
    }
}
