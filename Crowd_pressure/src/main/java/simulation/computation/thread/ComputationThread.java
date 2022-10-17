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

    public ComputationThread(PhysicalModel physicalModel, List<Heuristic> heuristics, List<Agent> allAgents, Board board) {
        this.physicalModel = physicalModel;
        this.heuristics = heuristics;
        this.allAgents = allAgents;
        this.board = board;
        this.computationAgents = null;
    }

    public void setComputationAgents(List<Agent> agents) {
        this.computationAgents = agents;
    }

    public List<Agent> getComputationAgents() {
        return computationAgents;
    }

    @Override
    public void run() {
        try{
            for(Agent agent : computationAgents){
                physicalModel.apply(agent, allAgents, board);
                for(Heuristic heuristic : heuristics){
                    heuristic.apply(agent, allAgents, board);
                }
            }
        }catch (Exception exception){
            System.out.println("Computation thread \"" + Thread.currentThread().getName() + "\" is dead. Details: " + exception.getMessage());
        }
    }
}
