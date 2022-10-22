package simulation.physics;

import simulation.model.*;
import utils.MathUtil;

import java.util.List;

public class SocialForcePhysicalModel implements PhysicalModel{

    private final double scaleCoefficient;

    public SocialForcePhysicalModel(double scaleCoefficient) {
        this.scaleCoefficient = scaleCoefficient;
    }

    @Override
    public void apply(Agent agent, List<Agent> allAgents, Board board) throws Exception {
        // todo: implement physics here


//        fij - wektor siły zderzenia dwóch pieszych i oraz j:
//        fij = kg(ri + rj - dij) * nij
//
//        k - współczynnik skalujący
//        g(x) = 0, jeśli piersi i j się nie dotykają, w przeciwnym wypadku g(x) = x
//        ri - promień reprezentujący ciało pieszego równy mi / 320
//        dij - dystans między środkami ciężkości pieszych i j
//        nij - wektor jednostkowy między pieszymi i j



    }

    private Vector calculateAgentImpactForce(Agent agent, List<Agent> allAgents){
        Vector totalForce = new Vector(0, 0);

        for(Agent obstacle : allAgents){
            if(agent == obstacle) continue;
            double distance = MathUtil.getDistance(agent.getPosition(), obstacle.getPosition());
            if(distance <= agent.getAgentRadius() + obstacle.getAgentRadius()){
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
            double x0 = agent.getPosition().getX(), y0 = agent.getPosition().getY();
            double x1 = wall.getStartPoint().getX(), y1 = wall.getStartPoint().getY();
            double x2 = wall.getEndPoint().getX(), y2 = wall.getEndPoint().getY();
//            double distance =
//
//                    Math.abs(()() - ()()) /
//                            Math.sqrt(Math.pow(, 2) + Math.pow(, 2));
//
//            if(distance <= agent.getAgentRadius() + obstacle.getAgentRadius()){
//                Vector forceVector = new Vector(
//                        (agent.getAgentRadius() + obstacle.getAgentRadius() - distance) * scaleCoefficient,
//                        MathUtil.calculateMutualAngle(agent.getPosition(), obstacle.getPosition())
//                );
//                totalForce = MathUtil.add(totalForce, forceVector);
//            }
        }

        return totalForce;
    }


}
