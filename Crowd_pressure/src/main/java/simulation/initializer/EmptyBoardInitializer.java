package simulation.initializer;

import simulation.model.Board;
import simulation.model.Point;
import simulation.model.Wall;

import java.util.ArrayList;
import java.util.List;

public class EmptyBoardInitializer implements BoardInitializer{

    @Override
    public Board initialize(int width, int height) throws Exception{
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(new Point(0, 0), new Point(0, height)));
        walls.add(new Wall(new Point(0, height), new Point(width, height)));
        walls.add(new Wall(new Point(0, 0), new Point(width, 0)));
        walls.add(new Wall(new Point(width, 0), new Point(width, height)));
        return new Board(width, height, walls);
    }
}
