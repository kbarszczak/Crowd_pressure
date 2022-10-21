package simulation.initializer;

import simulation.model.Board;

public interface BoardInitializer {

    /**
     * The method is responsible to initialize the board
     * @return the board that will be initialized
     * @throws Exception the exception is thrown whenever any error occurs
     */
    Board initialize() throws Exception;

}
