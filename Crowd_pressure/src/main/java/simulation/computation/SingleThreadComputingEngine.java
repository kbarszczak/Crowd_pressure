package simulation.computation;

import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.List;

public class SingleThreadComputingEngine implements ComputingEngine{

    @Override
    public void compute(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) throws Exception {
        for(Agent agent : agents){
            physicalModel.apply(agent, agents, board);
            for(Heuristic heuristic : heuristics){
                heuristic.apply(agent, agents, board);
            }
        }
    }
}
