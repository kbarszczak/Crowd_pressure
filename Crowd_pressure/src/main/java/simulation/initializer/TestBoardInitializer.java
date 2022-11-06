package simulation.initializer;

import simulation.model.Board;
import simulation.model.Wall;
import simulation.model.Point;

import java.util.ArrayList;

public class TestBoardInitializer implements BoardInitializer{

    @Override
    public Board initialize(int width, int height) throws Exception {
        Board result;
        try{
            result = new EmptyBoardInitializer().initialize(width, height);
        }catch (Exception exception){
            result = new Board(width, height, new ArrayList<>());
        }

        result.getWalls().add(new Wall(
                new Point(width/4.0, height-1),
                new Point(width/4.0, (height-1)/2.0 + 3))
        );

        result.getWalls().add(new Wall(
                new Point(width/4.0, 0),
                new Point(width/4.0, (height-1)/2.0 - 3))
        );

        result.getWalls().add(new Wall(
                new Point(width/4.0+50, height-1),
                new Point(width/4.0+50, (height-1)/2.0 + 20))
        );

        result.getWalls().add(new Wall(
                new Point(width/4.0+50, 0),
                new Point(width/4.0+50, (height-1)/2.0 + 14))
        );

        result.getWalls().add(new Wall(
                new Point(width/4.0+100, height-1),
                new Point(width/4.0+100, (height-1)/2.0 - 4))
        );

        result.getWalls().add(new Wall(
                new Point(width/4.0+100, 0),
                new Point(width/4.0+100, (height-1)/2.0 - 10))
        );

        return result;
    }
}
