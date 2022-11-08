package simulation.initializer;

import simulation.model.Agent;
import simulation.model.Board;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map2AgentsInitializer implements AgentsInitializer {

    private final Random random;

    public Map2AgentsInitializer() {
        this.random = new Random();
    }

    @Override
    public List<Agent> initialize(int agentCount, Board board) throws Exception {
        List<Agent> agents = new ArrayList<>();

        int width = board.getWidth();
        int height = board.getHeight();
        for (int i = 0; i < agentCount; ++i) {
            double startX, startY = random.nextDouble(height / 2.0 - 30, height / 2.0 + 30);
            double endX, endY = height / 2.0;;
            if(random.nextBoolean()){
                startX = random.nextDouble(width / 4.0+20, width / 4.0 + 60);
                endX = 3 * width / 4.0;
            }else{
                startX = random.nextDouble(3*width / 4.0 - 60, 3*width / 4.0 - 20);
                endX = width / 4.0;
            }

            double agentMass = random.nextDouble(50, 100);
            agents.add(new Agent(
                    new Point(startX, startY),
                    agentMass,
                    agentMass / 50.0,
                    random.nextDouble(1.5, 2),
                    random.nextDouble(1.2, 1.3),
                    random.nextDouble(100, 105),
                    random.nextDouble(0.45, 0.55),
                    new Point(endX, endY)
            ));
        }
        return agents;
    }
}
