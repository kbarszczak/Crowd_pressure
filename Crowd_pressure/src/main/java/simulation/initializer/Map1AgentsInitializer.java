package simulation.initializer;

import simulation.model.Agent;
import simulation.model.Board;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map1AgentsInitializer implements AgentsInitializer {

    private final Random random;

    public Map1AgentsInitializer() {
        this.random = new Random();
    }

    @Override
    public List<Agent> initialize(int agentCount, Board board) throws Exception {
        List<Agent> agents = new ArrayList<>();

        int width = board.getWidth();
        int height = board.getHeight();
        for(int i=0; i<agentCount; ++i){
            double x = random.nextDouble(10, width/4.0);
            double y = random.nextDouble(height/4.0, 3*height/4.0);
            double agentMass = random.nextDouble(50, 100);

            agents.add(new Agent(
                    new Point(x, y),
                    agentMass,
                    agentMass/50.0,
                    random.nextDouble(3, 4),
                    random.nextDouble(1.22173, 1.39626),
                    random.nextDouble(200, Math.max(width, height)),
                    random.nextDouble(0.4, 0.6),
                    new Point(4/5.0*width, height/2.0)
            ));
        }
        return agents;
    }

}
