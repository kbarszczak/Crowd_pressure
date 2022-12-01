package simulation.initializer.board;

import simulation.model.Board;

public class Map7BoardInitializer implements BoardInitializer{

    @Override
    public Board initialize(int width, int height) throws Exception {
        return new Map4BoardInitializer().initialize(width, height);
    }
}
