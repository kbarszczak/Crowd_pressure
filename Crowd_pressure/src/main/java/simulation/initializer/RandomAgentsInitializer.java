package simulation.initializer;

import simulation.model.Agent;
import simulation.model.Board;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAgentsInitializer implements AgentsInitializer {

    private final Random random;

    public RandomAgentsInitializer() {
        this.random = new Random();
    }

    @Override
    public List<Agent> initialize(int agentCount, Board board) throws Exception {
        List<Agent> agents = new ArrayList<>();

        for(int i=0; i<agentCount; ++i){
            double x = random.nextDouble(1, board.getWidth()-1);
            double y = random.nextDouble(1, board.getHeight()-1);
            double agentMass = random.nextDouble(40, 120);

            // todo: verify position
            agents.add(new Agent(
                    new Point(x, y),
                    agentMass,
                    agentMass/20,
                    random.nextDouble(10, 12),
                    random.nextDouble(1.22173, 1.39626),
                    random.nextDouble(200, 300),
                    random.nextDouble(0.4, 0.6),
                    new Point(100, 200) // todo: fix
            ));
        }
        return agents;
    }

}
