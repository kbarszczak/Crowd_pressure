package simulation.computation;

import simulation.computation.thread.ComputationThread;
import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.List;

public class SingleThreadComputingEngine implements ComputingEngine{

    private final ComputationThread thread;

    public SingleThreadComputingEngine(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) {
        this.thread = new ComputationThread(physicalModel, heuristics, agents, board);
        this.thread.setComputationAgents(agents);
    }

    @Override
    public void compute() throws Exception {
        thread.start();
        thread.join();
    }

}
