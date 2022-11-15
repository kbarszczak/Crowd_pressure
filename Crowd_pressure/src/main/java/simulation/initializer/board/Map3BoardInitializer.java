package simulation.initializer.board;

import simulation.model.Board;
import simulation.model.Point;
import simulation.model.Wall;

import java.util.ArrayList;

public class Map3BoardInitializer implements BoardInitializer {

    public static final int TUNNEL_HEIGHT = 10;
    public static final int CROSS_WIDTH = 10;
    public static final int CROSS_HEIGHT = 3;
    
    @Override
    public Board initialize(int width, int height) throws Exception {
        Board result;
        try {
            result = new EmptyBoardInitializer().initialize(width, height);
        } catch (Exception exception) {
            result = new Board(width, height, new ArrayList<>());
        }

        result.getWalls().add(new Wall(
                new Point(0, height / 2.0 - TUNNEL_HEIGHT),
                new Point(width / 2.0 - CROSS_WIDTH, height / 2.0 - TUNNEL_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(0, height / 2.0 + TUNNEL_HEIGHT),
                new Point(width / 2.0 - CROSS_WIDTH, height / 2.0 + TUNNEL_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(width-1, height / 2.0 - TUNNEL_HEIGHT),
                new Point(width / 2.0 + CROSS_WIDTH, height / 2.0 - TUNNEL_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(width-1, height / 2.0 + TUNNEL_HEIGHT),
                new Point(width / 2.0 + CROSS_WIDTH, height / 2.0 + TUNNEL_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - CROSS_WIDTH, height / 2.0 - TUNNEL_HEIGHT),
                new Point(width / 2.0, height / 2.0 - CROSS_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 - CROSS_WIDTH, height / 2.0 + TUNNEL_HEIGHT),
                new Point(width / 2.0, height / 2.0 + CROSS_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + CROSS_WIDTH, height / 2.0 - TUNNEL_HEIGHT),
                new Point(width / 2.0, height / 2.0 - CROSS_HEIGHT))
        );

        result.getWalls().add(new Wall(
                new Point(width / 2.0 + CROSS_WIDTH, height / 2.0 + TUNNEL_HEIGHT),
                new Point(width / 2.0, height / 2.0 + CROSS_HEIGHT))
        );

        return result;
    }
}
