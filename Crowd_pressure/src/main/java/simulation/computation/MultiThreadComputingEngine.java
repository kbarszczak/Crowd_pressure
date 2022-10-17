package simulation.computation;

import simulation.computation.thread.ComputationThread;
import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadComputingEngine implements ComputingEngine{

    private final int groupSize;

    public MultiThreadComputingEngine(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    public void compute(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) throws Exception {
        int groupCount = (int)Math.ceil(agents.size() / (double)groupSize);
        List<ComputationThread> threads = new ArrayList<>();
        for(int i=0; i<groupCount; ++i) {
            ComputationThread ct = new ComputationThread(physicalModel, heuristics, agents, board);
            ct.setComputationAgents(agents.subList(i*groupSize, Math.min(i * groupSize + groupSize, agents.size() + 1)));
            threads.add(ct);
        }

        for(ComputationThread thread : threads) thread.start();
        for(ComputationThread thread : threads) thread.join();
    }
}
