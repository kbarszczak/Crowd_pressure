package simulation.computation;

import simulation.computation.thread.ComputationThread;
import simulation.computation.thread.ComputationThreadController;
import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.List;

public class MultiThreadComputingEngine implements ComputingEngine{

    private final ComputationThreadController threadController;

    public MultiThreadComputingEngine(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) {
        this.threadController = new ComputationThreadController((int)Math.ceil(agents.size() / 10.0), physicalModel, heuristics, agents, board); // create thread pool with appropriate amount of threads // todo: pick the proper amount of threads
    }

    @Override
    public void compute() throws Exception {
        for(ComputationThread thread : threadController.getThreads()){
            thread.start();
        }
        for(ComputationThread thread : threadController.getThreads()){
            thread.join();
        }
    }
}
