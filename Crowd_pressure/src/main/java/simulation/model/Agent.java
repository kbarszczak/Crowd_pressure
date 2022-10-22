package simulation.model;

import utils.MathUtil;
import utils.Point;

public class Agent {

    /**
     * All angles in radian
     */

    // modifiable
    private int index;
    private boolean inMove;
    private double []velocity;
    private double []angle; // relative to cartesian
    private Point []position;

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
        this.velocity = new double[]{agentComfortableSpeed, 0};
        this.angle = new double[]{MathUtil.calculateMutualAngle(initPosition, agentDesiredPosition), 0};
        this.position = new Point[]{initPosition, null};

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

    public double getVelocity() {
        return velocity[index];
    }

    public double getAngle() {
        return angle[index];
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

    public void setNextVelocity(double velocity) {
        this.velocity[(index+1)%2] = velocity;
    }

    public void setNextAngle(double angle) {
        this.angle[(index+1)%2] = angle;
    }

    public void setNextPosition(Point position) {
        this.position[(index+1)%2] = position;
    }

    public void prepareToNextStep(){
        index = (index+1) % 2;
    }
}
