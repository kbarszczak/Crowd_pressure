package simulation.computation.task;

import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.List;

public class SimulationTask implements Task {

    private final PhysicalModel physicalModel;
    private final List<Heuristic> heuristics;
    private final List<Agent> allAgents;
    private final List<Agent> computationAgents;
    private final Board board;

    public SimulationTask(PhysicalModel physicalModel, List<Heuristic> heuristics, List<Agent> allAgents, List<Agent> computationAgents, Board board) {
        this.physicalModel = physicalModel;
        this.heuristics = heuristics;
        this.allAgents = allAgents;
        this.computationAgents = computationAgents;
        this.board = board;
    }

    @Override
    public void execute() throws Exception {
        for (Agent agent : computationAgents) {
            if (agent.isStopped()) continue;
            for (Heuristic heuristic : heuristics) {
                heuristic.apply(agent, allAgents.stream().filter(p -> !p.isStopped() && p != agent).toList(), board);
            }
            physicalModel.apply(agent, allAgents.stream().filter(p -> !p.isStopped() && p != agent).toList(), board);
        }
    }

    @Override
    public void cleanUp() {
        for (Agent agent : computationAgents) {
            if (agent.isStopped()) continue;
            agent.prepareToNextStep();
        }
    }
}
