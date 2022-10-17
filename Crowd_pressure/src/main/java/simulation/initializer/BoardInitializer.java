package simulation.initializer;

import simulation.model.Board;

public interface BoardInitializer {

    /**
     * The method is responsible to initialize the board
     * @param board the board that will be initialized
     * @throws Exception the exception is thrown whenever any error occurs
     */
    void initialize(Board board) throws Exception;

}
