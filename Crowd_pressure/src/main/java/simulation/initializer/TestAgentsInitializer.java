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
            double x = random.nextDouble(width/6.0+10, width/5.0+10);
            double y = random.nextDouble(height/2.0-30, height/2.0+30);
            double agentMass = random.nextDouble(60, 100);

            agents.add(new Agent(
                    new Point(x, y),
                    agentMass,
                    agentMass/50.0,
                    random.nextDouble(1, 2),
                    random.nextDouble(1.22173, 1.26626),
                    random.nextDouble(100, 101),
                    random.nextDouble(0.4, 0.6),
                    new Point(width/2.0, height/2.0)
            ));
        }
        return agents;
    }

}
