package simulation.computation.task;

public interface Task {

    /**
     * The method is responsible for executing the given task related with the computations in the simulation
     * @throws Exception the exception is thrown whenever any error occurs
     */
    void execute() throws Exception;

    /**
     * The method is responsible to submit/revert all changes made by the task
     */
    void cleanUp();

}
