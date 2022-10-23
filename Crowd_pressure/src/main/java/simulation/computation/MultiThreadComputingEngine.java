package simulation.computation;

import simulation.computation.task.SimulationTask;
import simulation.computation.thread.WorkerThread;
import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadComputingEngine implements ComputingEngine {

    private final ExecutorService executor;
    private final int threadCount;

    public MultiThreadComputingEngine() {
        this(10);
    }

    public MultiThreadComputingEngine(int threadCount) {
        if(threadCount <= 0) throw new IllegalStateException("Thread count has to be positive");
        this.executor = Executors.newFixedThreadPool(threadCount);
        this.threadCount = threadCount;
    }

    @Override
    public void compute(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) throws Exception {
        CountDownLatch cdl = new CountDownLatch(threadCount);
        int groupSize = (int)Math.ceil(agents.size() / (double)threadCount);
        for(int i=0; i<threadCount; ++i) {
            executor.execute(new WorkerThread(
                    new SimulationTask(
                            physicalModel,
                            heuristics,
                            agents,
                            agents.subList(i*groupSize, Math.min(i * groupSize + groupSize, agents.size() + 1)),
                            board
                    ),
                    cdl
            ));
        }
        cdl.await();
        for(Agent agent : agents) agent.prepareToNextStep();
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
    }
}
