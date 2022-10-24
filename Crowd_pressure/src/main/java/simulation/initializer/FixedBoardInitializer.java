package simulation.initializer;

import simulation.model.Board;
import simulation.model.Wall;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;

public class FixedBoardInitializer implements BoardInitializer{

    private final int width;
    private final int height;

    public FixedBoardInitializer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Board initialize() throws Exception {
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(new Point(width/3.0, 100), new Point(width-30, 200)));
        return new Board(width, height, walls);
    }
}
