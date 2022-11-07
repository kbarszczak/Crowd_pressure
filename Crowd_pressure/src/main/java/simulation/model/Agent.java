package simulation.model;

import utils.MathUtil;

public class Agent {

    // modifiable
    private int index;
    private boolean isStopped;
    private final Point[] position;
    private final Vector[] velocity;
    private final Vector desiredVelocity;

    // not modifiable
    private final double agentMass;
    private final double agentRadius;
    private final double agentComfortableSpeed;
    private final double agentVisionAngle;
    private final double agentMaxVisionDistance;
    private final double agentRelaxationTime;
    private final Point agentDesiredPosition;

    public Agent(Point initPosition, double agentMass, double agentRadius, double agentComfortableSpeed, double agentVisionAngle, double agentMaxVisionDistance, double agentRelaxationTime, Point agentDesiredPosition) {
        // modifiable
        this.index = 0;
        this.isStopped = false;
        this.position = new Point[]{initPosition, new Point(initPosition.getX(), initPosition.getY())};
        this.velocity = new Vector[]{new Vector(agentComfortableSpeed, MathUtil.calculateMutualAngle(initPosition, agentDesiredPosition)), new Vector(0, 0)};
        this.desiredVelocity = new Vector(0, 0);

        // not modifiable
        this.agentMass = agentMass;
        this.agentRadius = agentRadius;
        this.agentComfortableSpeed = agentComfortableSpeed;
        this.agentVisionAngle = agentVisionAngle;
        this.agentMaxVisionDistance = agentMaxVisionDistance;
        this.agentRelaxationTime = agentRelaxationTime;
        this.agentDesiredPosition = agentDesiredPosition;
    }

    public Agent(Agent agent){
        this(
            new Point(agent.getPosition().getX(), agent.getPosition().getY()),
            agent.agentMass,
            agent.agentRadius,
            agent.agentComfortableSpeed,
            agent.agentVisionAngle,
            agent.agentMaxVisionDistance,
            agent.agentRelaxationTime,
            new Point(agent.agentDesiredPosition.getX(), agent.agentDesiredPosition.getY())
        );
    }

    public boolean isStopped() {
        return isStopped;
    }

    public Vector getVelocity() {
        return velocity[index];
    }

    public Vector getDesiredVelocity() {
        return desiredVelocity;
    }

    public Point getPosition() {
        return position[index];
    }

    public double getAgentMass() {
        return agentMass;
    }

    public double getAgentRadius() {
        return agentRadius;
    }

    public double getAgentComfortableSpeed() {
        return agentComfortableSpeed;
    }

    public double getAgentVisionAngle() {
        return agentVisionAngle;
    }

    public double getAgentMaxVisionDistance() {
        return agentMaxVisionDistance;
    }

    public double getAgentRelaxationTime() {
        return agentRelaxationTime;
    }

    public Point getAgentDesiredPosition() {
        return agentDesiredPosition;
    }

    public void setNextPosition(Point position) {
        this.position[(index + 1) % 2] = position;
    }

    public void setNextVelocity(Vector velocity) {
        this.velocity[(index + 1) % 2] = velocity;
    }

    public void prepareToNextStep() {
        index = (index + 1) % 2;
    }

    public void stop() {
        isStopped = true;
    }
}
