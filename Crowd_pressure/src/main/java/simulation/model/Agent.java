package simulation.model;

import utils.Point;

public class Agent {

    // modifiable
    private int currentIndex; // 0, 1
    private double velocity[];
    private double angle[];
    private Point currentPosition[];
    private boolean isStopped;

    // not modifiable
    private double mass; // [kg]
    private double radius; // [m]
    private double comfortableSpeed; // [m/sec]
    private double visionAngle; // [degrees]
    private double destinationAngle; // [degrees]
    private double horizonDistance; // [m]
    private double relaxationTime;
    private Point desiredPosition;

    // todo: describe the agent

    public Agent(Point initPosition, double mass, double comfortableSpeed, double visionAngle, double horizonDistance, double relaxationTime) {
        this.currentIndex = 0;
        this.velocity = new double[]{comfortableSpeed, 0};
        this.currentPosition = new Point[]{initPosition, null};
        this.isStopped = false;

        this.mass = mass;
        this.radius = mass / 320;
        this.comfortableSpeed = comfortableSpeed;
        this.visionAngle = visionAngle;
        this.horizonDistance = horizonDistance;
        this.relaxationTime = relaxationTime;
    }

}
