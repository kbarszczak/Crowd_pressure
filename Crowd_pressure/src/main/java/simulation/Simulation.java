package simulation;

import simulation.computation.ComputingEngine;
import simulation.heuristic.Heuristic;
import simulation.initializer.AgentInitializer;
import simulation.initializer.BoardInitializer;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final List<Agent> agents;
    private final Board board;
    private PhysicalModel physicalModel;
    private List<Heuristic> heuristics;
    private ComputingEngine engine;

    public Simulation(PhysicalModel physicalModel, List<Heuristic> heuristics, ComputingEngine engine, BoardInitializer boardInitializer, int agentCount, AgentInitializer agentInitializer){
        this.physicalModel = physicalModel;
        this.heuristics = heuristics;
        this.engine = engine;
        this.agents = new ArrayList<>();
        this.board = new Board();

        try{
            boardInitializer.initialize(board);
            for(int i=0; i<agentCount; ++i){
                Agent agent = new Agent();
                agentInitializer.initialize(agent);
                agents.add(agent);
            }
        }catch (Exception exception){
            System.out.println("Exception during initialization. Details: " + exception.getMessage());
        }
    }

    public void setPhysicalModel(PhysicalModel physicalModel) {
        this.physicalModel = physicalModel;
    }

    public void setHeuristics(List<Heuristic> heuristics) {
        this.heuristics = heuristics;
    }

    public void setEngine(ComputingEngine engine) {
        this.engine = engine;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Board getBoard() {
        return board;
    }

    public void run(){
        // todo: write run method
    }
}
