package simulation.computation.thread;

import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.ArrayList;
import java.util.List;

public class ComputationThreadController {

    private final List<ComputationThread> threads;

    /**
     * The constructor will create the proper amount of correctly set up threads. Each thread will have assigned the proper group of agents
     * @param threadCount - the amount of threads that will be created
     * @param physicalModel - the physical model object
     * @param heuristics - the list of heuristics that are used in the computations
     * @param agents - the list of agents that will be split into threads
     * @param board - the board of the simulation
     */
    public ComputationThreadController(int threadCount, PhysicalModel physicalModel, List<Heuristic> heuristics, List<Agent> agents, Board board) {
        this.threads = new ArrayList<>();
        int agentGroupSize = (int)Math.ceil(agents.size() / (double)threadCount);
        for(int i=0; i<threadCount; ++i) {
            ComputationThread ct = new ComputationThread(physicalModel, heuristics, agents, board);
            ct.setComputationAgents(agents.subList(i*agentGroupSize, Math.min(i * agentGroupSize + agentGroupSize, agents.size() + 1)));
            this.threads.add(ct);
        }
    }

    public List<ComputationThread> getThreads() {
        return threads;
    }
}
