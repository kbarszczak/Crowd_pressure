package simulation.initializer;

import simulation.model.Agent;
import simulation.model.Board;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAgentsInitializer implements AgentsInitializer {

    private final Random random;
    private final int count;

    public RandomAgentsInitializer(int count) {
        this.random = new Random();
        this.count = count;
    }

    @Override
    public List<Agent> initialize(Board board) throws Exception {
        List<Agent> agents = new ArrayList<>();

        for(int i=0; i<count; ++i){
            int x = random.nextInt(1, board.getWidth()-1);
            int y = random.nextInt(1, board.getHeight()-1);
            // todo: verify position
            agents.add(new Agent(
                    new Point(x, y),
                    random.nextDouble(40, 120),
                    random.nextDouble(1, 3),
                    random.nextDouble(150, 170),
                    random.nextDouble(50, 200),
                    random.nextDouble(0.4, 0.6)
            ));
        }
        return agents;
    }

}
