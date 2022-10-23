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
        if(agent.isStopped() || agent.getPosition().equals(agent.getAgentDesiredPosition())) {
            agent.stop();
            return;
        }

        // 1st
        Vector acceleration = agent.getDesiredVelocity().subtract(agent.getVelocity());
        acceleration.multiplyByConstant(1 / agent.getAgentRelaxationTime());

        // 2nd
        Vector obstacleImpactAcceleration = calculateAgentImpactForce(agent, allAgents);
        obstacleImpactAcceleration.multiplyByConstant(1 / agent.getAgentMass());
        acceleration =  acceleration.add(obstacleImpactAcceleration);

        // 3rd
        Vector wallImpactAcceleration = calculateWallImpactForce(agent, board.getWalls());
        wallImpactAcceleration.multiplyByConstant(1 / agent.getAgentMass());
        acceleration = acceleration.add(wallImpactAcceleration);

        // apply changes on the agent
        Vector velocityChange = acceleration.multiplyByConstantCopy(timeQuantum);
        agent.setNextVelocity(agent.getVelocity().add(velocityChange));

        Vector positionChange = velocityChange.multiplyByConstantCopy(timeQuantum);
        agent.setNextPosition(agent.getPosition().add(positionChange.toPoint()));
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
                totalForce = totalForce.add(forceVector);
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
                totalForce = totalForce.add(forceVector);
            }
        }

        return totalForce;
    }

}