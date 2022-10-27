package simulation.computation;

import simulation.computation.task.SimulationTask;
import simulation.computation.task.Task;
import simulation.computation.thread.WorkerThread;
import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.io.IOException;
import java.util.ArrayList;
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
        // initial calculations
        int groupSize = Math.max(4, (int)Math.ceil(agents.size() / (double)threadCount));
        CountDownLatch cdl = new CountDownLatch((int)Math.ceil(agents.size() / (double)groupSize));
        List<Task> tasks = new ArrayList<>();

        // divide tasks equally
        for(int i=0; i<threadCount; ++i) {
            // create new task
            SimulationTask task = new SimulationTask(
                    physicalModel,
                    heuristics,
                    agents,
                    agents.subList(i*groupSize, Math.min(i * groupSize + groupSize, agents.size())),
                    board
            );

            // execute that task on separate thread
            executor.execute(new WorkerThread(task, cdl));

            // remember the task
            tasks.add(task);

            // break the loop when tasks are divided
            if((i+1)*groupSize >= agents.size()) break;
        }

        cdl.await(); // wait for all threads to finish its tasks
        tasks.forEach(Task::cleanUp); // prepare all the agents for the next step
    }

    @Override
    public void close() throws IOException {
        executor.shutdown();
    }
}
