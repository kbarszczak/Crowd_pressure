package simulation.model;

import utils.MathUtil;

public class Agent {

    // modifiable
    private int index;
    private boolean inMove;
    private Point []position;
    private Vector []acceleration;
    private Vector []velocity;
    private Vector desiredVelocity;

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
        this.inMove = false;
        this.position = new Point[]{initPosition, new Point(initPosition.getX(), initPosition.getY())};
        this.acceleration = new Vector[]{new Vector(0, 0), new Vector(0, 0)};
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

    public boolean isInMove() {
        return inMove;
    }

    public Vector getVelocity() {
        return velocity[index];
    }

    public Vector getNextVelocity(){
        return velocity[(index+1)%2];
    }

    public Vector getDesiredVelocity() {
        return desiredVelocity;
    }

    public Vector getAcceleration() {
        return acceleration[index];
    }

    public Vector getNextAcceleration(){
        return acceleration[(index+1)%2];
    }

    public Point getPosition() {
        return position[index];
    }

    public Point getNextPosition(){
        return position[(index+1)%2];
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

    public void prepareToNextStep(){
        index = (index+1) % 2;
    }
}
