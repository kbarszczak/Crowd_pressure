package simulation.physics;

import simulation.model.*;
import utils.MathUtil;

import java.util.List;

public class SocialForcePhysicalModel implements PhysicalModel{

    private final double scaleCoefficient;
    private final double timeQuantum;

    public SocialForcePhysicalModel(double scaleCoefficient, double timeQuantum) {
        this.scaleCoefficient = scaleCoefficient;
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        // 1st
        Vector acceleration = MathUtil.subtract(agent.getDesiredVelocity(), agent.getVelocity());
        acceleration.setValue(acceleration.getValue() / agent.getAgentRelaxationTime());

        // 2nd
        Vector obstacleImpactAcceleration = calculateAgentImpactForce(agent, allAgents);
        obstacleImpactAcceleration.setValue(obstacleImpactAcceleration.getValue() / agent.getAgentMass());
        acceleration =  MathUtil.add(acceleration, obstacleImpactAcceleration);

        // 3rd
        Vector wallImpactAcceleration = calculateWallImpactForce(agent, board.getWalls());
        wallImpactAcceleration.setValue(wallImpactAcceleration.getValue() / agent.getAgentMass());
        acceleration = MathUtil.add(acceleration, wallImpactAcceleration);
        System.out.println("Acceleration change: " + acceleration.getValue() + " " + acceleration.getAngle());

        // apply changes on the agent
        System.out.println(timeQuantum);
        Vector velocityChange = new Vector(acceleration.getValue() * timeQuantum, acceleration.getAngle());
        System.out.println("Velocity change: " + velocityChange.getValue() + " " + velocityChange.getAngle());
        agent.setNextVelocity(MathUtil.add(agent.getVelocity(), velocityChange));
        Vector positionChange = new Vector(velocityChange.getValue() * timeQuantum, velocityChange.getAngle());
        agent.setNextPosition(MathUtil.add(agent.getPosition().toVector(), positionChange).toPoint());

        System.out.println();
    }

    private Vector calculateAgentImpactForce(Agent agent, List<Agent> allAgents){
        Vector totalForce = new Vector(0, 0);

        for(Agent obstacle : allAgents){
            if(agent == obstacle) continue;
            double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), obstacle.getPosition());
            if(distance < agent.getAgentRadius() + obstacle.getAgentRadius()){
                Vector forceVector = new Vector(
                        (agent.getAgentRadius() + obstacle.getAgentRadius() - distance) * scaleCoefficient,
                        MathUtil.calculateMutualAngle(agent.getPosition(), obstacle.getPosition())
                );
                totalForce = MathUtil.add(totalForce, forceVector);
            }
        }

        return totalForce;
    }

    private Vector calculateWallImpactForce(Agent agent, List<Wall> walls){
        Vector totalForce = new Vector(0, 0);
        for(Wall wall : walls){
            Point crossingPoint = MathUtil.getCrossingPointInShortestPath(agent.getPosition(), wall.getStartPoint(), wall.getEndPoint());
            double distance = MathUtil.calculateDistanceBetweenPoints(agent.getPosition(), crossingPoint);
            if (distance < agent.getAgentRadius()){
                Vector forceVector = new Vector(
                        (agent.getAgentRadius() - distance) * scaleCoefficient,
                        MathUtil.calculateMutualAngle(agent.getPosition(), crossingPoint)
                );
                totalForce = MathUtil.add(totalForce, forceVector);
            }
        }

        return totalForce;
    }

}