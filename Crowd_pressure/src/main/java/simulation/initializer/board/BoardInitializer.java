package simulation.initializer.board;

import simulation.model.Board;

public interface BoardInitializer {

    /**
     * The method is responsible to initialize the board
     *
     * @param height the height of the created board
     * @param width  the width of the created board
     * @return the board that will be initialized
     * @throws Exception the exception is thrown whenever any error occurs
     */
    Board initialize(int width, int height) throws Exception;

}
