package simulation.initializer;

import simulation.model.Agent;

public interface AgentInitializer {

    /**
     * The method is responsible to initialize the agent
     * @param agent the agent that will be initialized
     * @throws Exception the exception is thrown whenever any error occurs
     */
    void initialize(Agent agent) throws Exception;

}
