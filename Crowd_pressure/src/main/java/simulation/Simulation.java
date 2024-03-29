package simulation;

import simulation.computation.ComputingEngine;
import simulation.heuristic.Heuristic;
import simulation.initializer.agent.AgentsInitializer;
import simulation.initializer.board.BoardInitializer;
import simulation.initializer.board.EmptyBoardInitializer;
import simulation.initializer.agent.Map1AgentsInitializer;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Closeable {

    private final List<Agent> initAgents;
    private List<Agent> agents;
    private Board board;
    private PhysicalModel physicalModel;
    private List<Heuristic> heuristics;
    private ComputingEngine engine;

    public Simulation(int width, int height, int agentCount, PhysicalModel physicalModel, List<Heuristic> heuristics, ComputingEngine engine, BoardInitializer boardInitializer, AgentsInitializer agentInitializer) {
        this.physicalModel = physicalModel;
        this.heuristics = heuristics;
        this.engine = engine;

        try {
            this.board = boardInitializer.initialize(width, height);
        } catch (Exception exception) {
            System.out.println("Exception during board initialization. Empty board was created. Details: " + exception.getMessage());
            try {
                this.board = new EmptyBoardInitializer().initialize(width, height);
            } catch (Exception ignore) {
            }
        }

        try {
            this.agents = agentInitializer.initialize(agentCount, this.board);
        } catch (Exception exception) {
            System.out.println("Exception during agents initialization. Empty agent list was created. Details: " + exception.getMessage());
            try {
                this.agents = new Map1AgentsInitializer().initialize(agentCount, this.board);
            } catch (Exception ignore) {
            }
        }
        this.initAgents = new ArrayList<>();
        for(Agent agent : agents) initAgents.add(new Agent(agent));
    }

    public void setPhysicalModel(PhysicalModel physicalModel) {
        this.physicalModel = physicalModel;
    }

    public void setHeuristics(List<Heuristic> heuristics) {
        this.heuristics = heuristics;
    }

    public void setEngine(ComputingEngine engine) {
        try {
            this.engine.close();
            this.engine = engine;
        } catch (Exception exception) {
            System.out.println("Could not change the computing engine");
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public Board getBoard() {
        return board;
    }

    public boolean step() {
        try {
            engine.compute(agents, board, physicalModel, heuristics);
            return agents.stream().allMatch(Agent::isStopped);
        } catch (Exception exception) {
            System.out.println("Computation error. Details: " + exception.getMessage());
            return false;
        }
    }

    public void restoreInitState() throws Exception {
        agents.clear();
        for(Agent agent : initAgents) agents.add(new Agent(agent));
    }

    @Override
    public void close() throws IOException {
        engine.close();
    }
}
