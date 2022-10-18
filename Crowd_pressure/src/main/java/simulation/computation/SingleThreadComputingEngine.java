package simulation.computation;

import simulation.computation.task.SimulationTask;
import simulation.computation.task.Task;
import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.List;

public class SingleThreadComputingEngine implements ComputingEngine{

    @Override
    public void compute(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) throws Exception {
        Task task = new SimulationTask(physicalModel, heuristics, agents, agents, board);
        task.execute();
    }
}
