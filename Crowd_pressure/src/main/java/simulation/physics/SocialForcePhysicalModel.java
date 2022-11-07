package simulation.physics;

import simulation.model.*;
import utils.MathUtil;

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

        // acceleration from desired speed
        Vector acceleration = agent.getDesiredVelocity().subtract(agent.getVelocity());
        acceleration.multiplyByConstant(1 / agent.getAgentRelaxationTime());

        // acceleration from other agents impact
        Vector obstacleImpactAcceleration = calculateAgentImpactForce(agent, allAgents);
        obstacleImpactAcceleration.multiplyByConstant(1 / agent.getAgentMass());
        acceleration =  acceleration.add(obstacleImpactAcceleration);

        // acceleration from wall impact
        Vector wallImpactAcceleration = calculateWallImpactForce(agent, board.getWalls());
        wallImpactAcceleration.multiplyByConstant(1 / agent.getAgentMass());
        acceleration = acceleration.add(wallImpactAcceleration);


        // apply changes on the agent
        Vector velocityChange = acceleration.multiplyByConstantCopy(timeQuantum/1000.0);
        agent.setNextVelocity(agent.getVelocity().add(velocityChange));

        Vector positionChange = agent.getVelocity().multiplyByConstantCopy(timeQuantum/1000.0);
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