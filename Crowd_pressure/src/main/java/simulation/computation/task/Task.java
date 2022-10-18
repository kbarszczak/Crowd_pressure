package simulation.computation.task;

public interface Task {

    /**
     * The method is responsible for executing the given task related with the computations in the simulation
     * @throws Exception the exception is thrown whenever any error occurrs
     */
    void execute() throws Exception;

}
