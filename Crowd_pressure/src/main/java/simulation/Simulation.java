package simulation;

import simulation.computation.ComputingEngine;
import simulation.heuristic.Heuristic;
import simulation.initializer.AgentInitializer;
import simulation.initializer.BoardInitializer;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Closeable {

    private List<Agent> agents;
    private Board board;
    private PhysicalModel physicalModel;
    private List<Heuristic> heuristics;
    private ComputingEngine engine;

    public Simulation(PhysicalModel physicalModel, List<Heuristic> heuristics, ComputingEngine engine, BoardInitializer boardInitializer, int agentCount, AgentInitializer agentInitializer){
        this.physicalModel = physicalModel;
        this.heuristics = heuristics;
        this.engine = engine;

        try{
            this.board = boardInitializer.initialize();
        }catch (Exception exception){
            System.out.println("Exception during board initialization. Empty board was created. Details: " + exception.getMessage());
            this.board = new Board();
        }

        try{
            this.agents = new ArrayList<>();
            for(int i=0; i<agentCount; ++i) this.agents.add(agentInitializer.initialize());
        }catch (Exception exception){
            System.out.println("Exception during agents initialization. Empty agent list was created. Details: " + exception.getMessage());
        }
    }

    public void setPhysicalModel(PhysicalModel physicalModel) {
        this.physicalModel = physicalModel;
    }

    public void setHeuristics(List<Heuristic> heuristics) {
        this.heuristics = heuristics;
    }

    public void setEngine(ComputingEngine engine) {
        try{
            this.engine.close();
            this.engine = engine;
        }catch (Exception exception){
            System.out.println("Could not change the computing engine");
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Board getBoard() {
        return board;
    }

    public void step(){
        try{
            engine.compute(agents, board, physicalModel, heuristics);
        }catch (Exception exception){
            System.out.println("Computation error. Details: " + exception.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        engine.close();
    }
}
