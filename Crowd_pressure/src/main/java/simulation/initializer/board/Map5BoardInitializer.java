package simulation.initializer.board;

import simulation.model.Board;
import simulation.model.Point;
import simulation.model.Wall;

public class Map5BoardInitializer implements BoardInitializer{

    private static final int COLUMN_WIDTH = 4;

    @Override
    public Board initialize(int width, int height) throws Exception {
        Board result = new Map4BoardInitializer().initialize(width, height);

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + COLUMN_WIDTH, height / 2.0 + COLUMN_WIDTH),
                new Point(width / 2.0 + COLUMN_WIDTH, height / 2.0 - COLUMN_WIDTH)
        ));

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - COLUMN_WIDTH, height / 2.0 + COLUMN_WIDTH),
                new Point(width / 2.0 - COLUMN_WIDTH, height / 2.0 - COLUMN_WIDTH)
        ));

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - COLUMN_WIDTH, height / 2.0 + COLUMN_WIDTH),
                new Point(width / 2.0 + COLUMN_WIDTH, height / 2.0 + COLUMN_WIDTH)
        ));

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - COLUMN_WIDTH, height / 2.0 - COLUMN_WIDTH),
                new Point(width / 2.0 + COLUMN_WIDTH, height / 2.0 - COLUMN_WIDTH)
        ));

        return result;
    }
}
