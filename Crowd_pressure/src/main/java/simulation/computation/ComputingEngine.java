package simulation.computation;

import simulation.heuristic.Heuristic;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.physics.PhysicalModel;

import java.io.Closeable;
import java.util.List;

public interface ComputingEngine extends Closeable {

    /**
     * The method that is responsible to do all the necessary calculations
     *
     * @param agents        the list of all agents
     * @param board         the simulation board
     * @param physicalModel the physical model that is used in the simulation
     * @param heuristics    the list of heuristics used in the simulation
     * @throws Exception the exception is thrown whenever any error occurs
     */
    void compute(List<Agent> agents, Board board, PhysicalModel physicalModel, List<Heuristic> heuristics) throws Exception;

}
