package simulation.initializer.board;

import simulation.model.Board;

public class Map2BoardInitializer implements BoardInitializer {

    @Override
    public Board initialize(int width, int height) throws Exception {
        return new Map1BoardInitializer().initialize(width, height);
    }
}
