package simulation.initializer;

import simulation.model.Agent;
import simulation.model.Board;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestAgentsInitializer implements AgentsInitializer {

    private final Random random;

    public TestAgentsInitializer() {
        this.random = new Random();
    }

    @Override
    public List<Agent> initialize(int agentCount, Board board) throws Exception {
        List<Agent> agents = new ArrayList<>();

        int width = board.getWidth();
        int height = board.getHeight();
        for(int i=0; i<agentCount; ++i){
            double x = random.nextDouble(width/5.0-20, width/5.0+20);
            double y = random.nextDouble(height/2.0-15, height/2.0+15);
            double agentMass = random.nextDouble(50, 100);

            agents.add(new Agent(
                    new Point(x, y),
                    agentMass,
                    agentMass/50.0,
                    random.nextDouble(1.5, 2),
                    random.nextDouble(1.3, 1.4),
                    random.nextDouble(100, 105),
                    random.nextDouble(0.45, 0.55),
                    new Point(width/2.0, height/2.0)
            ));
        }

        return agents;
    }

}
