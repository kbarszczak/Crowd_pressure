package simulation.initializer.agent;

import simulation.initializer.board.Map3BoardInitializer;
import simulation.model.Agent;
import simulation.model.Board;
import simulation.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map3AgentsInitializer implements AgentsInitializer {

    private final Random random;

    public Map3AgentsInitializer() {
        this.random = new Random();
    }

    @Override
    public List<Agent> initialize(int agentCount, Board board) throws Exception {
        List<Agent> agents = new ArrayList<>();

        int width = board.getWidth();
        int height = board.getHeight();
        for (int i = 0; i < agentCount; ++i) {
            double x = random.nextDouble(width / 2.0 - 120, width / 2.0 - 20);
            double y = random.nextDouble(height / 2.0 - Map3BoardInitializer.TUNNEL_HEIGHT+2, height / 2.0 + Map3BoardInitializer.TUNNEL_HEIGHT-2);
            double agentMass = random.nextDouble(60, 90);

            agents.add(new Agent(
                    new Point(x, y),
                    agentMass,
                    agentMass / 50.0,
                    random.nextDouble(1.5, 2),
                    random.nextDouble(1.2, 1.3),
                    random.nextDouble(100, 105),
                    random.nextDouble(0.45, 0.55),
                    new Point(4 / 5.0 * width, height / 2.0)
            ));
        }
        return agents;
    }

}

