package simulation.computation.thread;

import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.List;

public class ComputationThread extends Thread{

    private final PhysicalModel physicalModel;
    private final List<Heuristic> heuristics;
    private final List<Agent> allAgents;
    private final Board board;

    private List<Agent> computationAgents;
    private boolean isReady;

    public ComputationThread(PhysicalModel physicalModel, List<Heuristic> heuristics, List<Agent> allAgents, Board board) {
        this.physicalModel = physicalModel;
        this.heuristics = heuristics;
        this.allAgents = allAgents;
        this.board = board;
        this.computationAgents = null;
        this.isReady = true;
    }

    public void setComputationAgents(List<Agent> agents) {
        this.computationAgents = agents;
    }

    public List<Agent> getComputationAgents() {
        return computationAgents;
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public void run() {
        isReady = false;
        try{
            // todo: write computation thread logic here
        }catch (Exception exception){
            System.out.println("Computation thread \"" + Thread.currentThread().getName() + "\" is dead. Details: " + exception.getMessage());
        }
        isReady = true;
    }
}
