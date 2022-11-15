package simulation.initializer.board;

import simulation.model.Board;
import simulation.model.Point;
import simulation.model.Wall;

import java.util.ArrayList;

public class Map4BoardInitializer implements BoardInitializer{

    public static final int TUNNEL_WIDTH = 12;

    @Override
    public Board initialize(int width, int height) throws Exception {
        Board result;
        try {
            result = new EmptyBoardInitializer().initialize(width, height);
        } catch (Exception exception) {
            result = new Board(width, height, new ArrayList<>());
        }

        result.getWalls().add(new Wall(
                new Point(0, height / 2.0 - TUNNEL_WIDTH),
                new Point(width / 2.0 - TUNNEL_WIDTH, height / 2.0 - TUNNEL_WIDTH))
        );

        result.getWalls().add(new Wall(
                new Point(0, height / 2.0 + TUNNEL_WIDTH),
                new Point(width / 2.0 - TUNNEL_WIDTH, height / 2.0 + TUNNEL_WIDTH))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + TUNNEL_WIDTH, height / 2.0 - TUNNEL_WIDTH),
                new Point(width - 1, height / 2.0 - TUNNEL_WIDTH))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + TUNNEL_WIDTH, height / 2.0 + TUNNEL_WIDTH),
                new Point(width - 1, height / 2.0 + TUNNEL_WIDTH))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - TUNNEL_WIDTH, 0),
                new Point(width / 2.0 - TUNNEL_WIDTH, height / 2.0 - TUNNEL_WIDTH))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + TUNNEL_WIDTH, 0),
                new Point(width / 2.0 + TUNNEL_WIDTH, height / 2.0 - TUNNEL_WIDTH))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - TUNNEL_WIDTH, height / 2.0 + TUNNEL_WIDTH),
                new Point(width / 2.0 - TUNNEL_WIDTH, height - 1))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + TUNNEL_WIDTH, height / 2.0 + TUNNEL_WIDTH),
                new Point(width / 2.0 + TUNNEL_WIDTH, height - 1))
        );

        return result;
    }
}
