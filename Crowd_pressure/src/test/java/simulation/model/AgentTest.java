package simulation.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    private final double delta = 0.000001;
    private Agent agent1;
    private Agent agent2;

    @BeforeEach
    void setUp() {
        agent1 = new Agent(new Point(3, 1), 60, 1, 6, 150, 450, 0.5, new Point(100, 200));
        agent2 = new Agent(new Point(3, 1), 60, 1, 6, 150, 450, 0.5, new Point(100, 200));
    }

    @Test
    void setNextField() {
        Assertions.assertEquals(agent1.getAgentComfortableSpeed(), agent1.getVelocity().getValue(), delta);
        Assertions.assertEquals(agent2.getAgentComfortableSpeed(), agent2.getVelocity().getValue(), delta);

        agent1.setNextPosition(new Point(10, 10));
        agent1.setNextVelocity(new Vector(1, 1));
        agent2.setNextPosition(new Point(10, 10));
        agent2.setNextVelocity(new Vector(1, 1));

        Assertions.assertEquals(agent1.getAgentComfortableSpeed(), agent1.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(3, 1), agent1.getPosition());
        Assertions.assertEquals(agent2.getAgentComfortableSpeed(), agent2.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(3, 1), agent2.getPosition());

        agent1.prepareToNextStep();
        agent2.prepareToNextStep();

        Assertions.assertEquals(1, agent1.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(10, 10), agent1.getPosition());
        Assertions.assertEquals(1, agent2.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(10, 10), agent2.getPosition());

        agent1.setNextPosition(new Point(11, 11));
        agent1.setNextVelocity(new Vector(3, 1));
        agent2.setNextPosition(new Point(14, 14));
        agent2.setNextVelocity(new Vector(12, 1));

        Assertions.assertEquals(1, agent1.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(10, 10), agent1.getPosition());
        Assertions.assertEquals(1, agent2.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(10, 10), agent2.getPosition());

        agent1.prepareToNextStep();
        agent2.prepareToNextStep();

        Assertions.assertEquals(3, agent1.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(11, 11), agent1.getPosition());
        Assertions.assertEquals(12, agent2.getVelocity().getValue(), delta);
        Assertions.assertEquals(new Point(14, 14), agent2.getPosition());
    }
}