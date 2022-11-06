package simulation.physics;

import simulation.model.*;
import utils.MathUtil;
import view.SimulationApplication;

import java.util.List;

public class SocialForcePhysicalModel implements PhysicalModel{

    private final double scaleCoefficient;
    private final double destinationRadius;
    private final double timeQuantum;

    public SocialForcePhysicalModel(double scaleCoefficient, double destinationRadius, double timeQuantum) {
        this.scaleCoefficient = scaleCoefficient;
        this.destinationRadius = destinationRadius;
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        if(MathUtil.isInsideCircle(agent.getPosition(), agent.getAgentDesiredPosition(), destinationRadius)) {
            agent.stop();
            return;
        }

        if(SimulationApplication.DEBUG_MODE) System.out.println("Agent [ " + agent.getAgentMass() + " ]");
        if(SimulationApplication.DEBUG_MODE) System.out.println("---------- Heuristics impact:");
        if(SimulationApplication.DEBUG_MODE) System.out.printf("Desired velocity: (%f, %f) ~ (%f, %f)\n", agent.getDesiredVelocity().getValue(), agent.getDesiredVelocity().getAngle(), agent.getDesiredVelocity().toPoint().getX(), agent.getDesiredVelocity().toPoint().getY());
        if(SimulationApplication.DEBUG_MODE) System.out.printf("Velocity: (%f, %f) ~ (%f, %f)\n", agent.getVelocity().getValue(), agent.getVelocity().getAngle(), agent.getVelocity().toPoint().getX(), agent.getVelocity().toPoint().getY());

        Vector acceleration = agent.getDesiredVelocity().subtract(agent.getVelocity());

        if(SimulationApplication.DEBUG_MODE) System.out.printf("Acceleration (des - comfortable): (%f, %f) ~ (%f, %f)\n", acceleration.getValue(), acceleration.getAngle(), acceleration.toPoint().getX(), acceleration.toPoint().getY());

        acceleration.multiplyByConstant(1 / agent.getAgentRelaxationTime());

        if(SimulationApplication.DEBUG_MODE) System.out.printf("Acceleration (previous * coefficient): (%f, %f) ~ (%f, %f)\n", acceleration.getValue(), acceleration.getAngle(), acceleration.toPoint().getX(), acceleration.toPoint().getY());
        if(SimulationApplication.DEBUG_MODE) System.out.println("Coefficient: " + (1 / agent.getAgentRelaxationTime()));
        if(SimulationApplication.DEBUG_MODE) System.out.println("---------- Obstacle impact:");

        Vector obstacleImpactAcceleration = calculateAgentImpactForce(agent, allAgents);

        if(SimulationApplication.DEBUG_MODE) System.out.printf("ObstacleImpactAcceleration (all): (%f, %f) ~ (%f, %f)\n", obstacleImpactAcceleration.getValue(), obstacleImpactAcceleration.getAngle(), obstacleImpactAcceleration.toPoint().getX(), obstacleImpactAcceleration.toPoint().getY());

        obstacleImpactAcceleration.multiplyByConstant(1 / agent.getAgentMass());

        if(SimulationApplication.DEBUG_MODE) System.out.printf("ObstacleImpactAcceleration (all*coefficient): (%f, %f) ~ (%f, %f)\n", obstacleImpactAcceleration.getValue(), obstacleImpactAcceleration.getAngle(), obstacleImpactAcceleration.toPoint().getX(), obstacleImpactAcceleration.toPoint().getY());
        if(SimulationApplication.DEBUG_MODE) System.out.println("Coefficient: " + (1 / agent.getAgentMass()));

        acceleration =  acceleration.add(obstacleImpactAcceleration);

        if(SimulationApplication.DEBUG_MODE) System.out.printf("Acceleration (previous acc + obstacle impact): (%f, %f) ~ (%f, %f)\n", acceleration.getValue(), acceleration.getAngle(), acceleration.toPoint().getX(), acceleration.toPoint().getY());
        if(SimulationApplication.DEBUG_MODE) System.out.println("---------- Wall impact:");

        Vector wallImpactAcceleration = calculateWallImpactForce(agent, board.getWalls());

        if(SimulationApplication.DEBUG_MODE) System.out.printf("WallImpactAcceleration (all): (%f, %f) ~ (%f, %f)\n", wallImpactAcceleration.getValue(), wallImpactAcceleration.getAngle(), wallImpactAcceleration.toPoint().getX(), wallImpactAcceleration.toPoint().getY());

        wallImpactAcceleration.multiplyByConstant(1 / agent.getAgentMass());

        if(SimulationApplication.DEBUG_MODE) System.out.printf("WallImpactAcceleration (all*coefficient): (%f, %f) ~ (%f, %f)\n", wallImpactAcceleration.getValue(), wallImpactAcceleration.getAngle(), wallImpactAcceleration.toPoint().getX(), wallImpactAcceleration.toPoint().getY());
        if(SimulationApplication.DEBUG_MODE) System.out.println("Coefficient: " + (1 / agent.getAgentMass()));

        acceleration = acceleration.add(wallImpactAcceleration);

        if(SimulationApplication.DEBUG_MODE) System.out.printf("Acceleration (previous acc + wall impact): (%f, %f) ~ (%f, %f)\n", acceleration.getValue(), acceleration.getAngle(), acceleration.toPoint().getX(), acceleration.toPoint().getY());

        // apply changes on the agent
        Vector velocityChange = acceleration.multiplyByConstantCopy(timeQuantum/1000.0);
        agent.setNextVelocity(agent.getVelocity().add(velocityChange));

        Vector positionChange = agent.getVelocity().multiplyByConstantCopy(timeQuantum/1000.0);
        agent.setNextPosition(agent.getPosition().add(positionChange.toPoint()));

        if(SimulationApplication.DEBUG_MODE) System.out.println("------------------------------------------------------------");
    }

    private Vector calculateAgentImpactForce(Agent agent, List<Agent> allAgents){
        Vector totalForce = new Vector(0, 0);

        for(Agent obstacle : allAgents){
            if(agent == obstacle) continue;
            double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), obstacle.getPosition());
            if(distance < agent.getAgentRadius() + obstacle.getAgentRadius()){
                Vector forceVector = new Vector(
                        (agent.getAgentRadius() + obstacle.getAgentRadius() - distance) * scaleCoefficient,
                        MathUtil.calculateMutualAngle(obstacle.getPosition(), agent.getPosition())
                );
                totalForce = totalForce.add(forceVector);
            }
        }

        return totalForce;
    }

    private Vector calculateWallImpactForce(Agent agent, List<Wall> walls){
        Vector totalForce = new Vector(0, 0);
        for(Wall wall : walls){
            Point crossingPoint = MathUtil.getCrossingPointInShortestPath(agent.getPosition(), wall.getStartPoint(), wall.getEndPoint());
            if(crossingPoint.getX() >= Math.min(wall.getStartPoint().getX(), wall.getEndPoint().getX()) &&
                    crossingPoint.getX() <= Math.max(wall.getStartPoint().getX(), wall.getEndPoint().getX()) &&
                    crossingPoint.getY() >= Math.min(wall.getStartPoint().getY(), wall.getEndPoint().getY()) &&
                    crossingPoint.getY() <= Math.max(wall.getStartPoint().getY(), wall.getEndPoint().getY())){

                double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), crossingPoint);
                if (distance < agent.getAgentRadius()){
                    Vector forceVector = new Vector(
                            (agent.getAgentRadius() - distance) * scaleCoefficient,
                            MathUtil.calculateMutualAngle(crossingPoint, agent.getPosition())
                    );
                    totalForce = totalForce.add(forceVector);
                }
            }
        }

        return totalForce;
    }
}