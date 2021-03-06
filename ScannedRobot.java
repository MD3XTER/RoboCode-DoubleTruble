package nl.saxion.dhi1vsq3;

import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.io.Serializable;

import static robocode.util.Utils.normalAbsoluteAngleDegrees;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class ScannedRobot implements Serializable {
    private String name;
    private double bearing;
    private double distance;
    private double energy;
    private double heading;
    private double velocity;
    private double x;
    private double y;

    public ScannedRobot() {
        reset();
    }

    /**
     * This function takes two parameters, and initiate a robot object
     * @param e - scanned robot
     * @param robot - robot that scanned that robot
     */
    public void update(ScannedRobotEvent e, Robot robot) {
        name = e.getName();
        bearing = e.getBearing();
        distance = e.getDistance();
        energy = e.getEnergy();
        heading = e.getHeading();
        velocity = e.getVelocity();
        // calculate absolute bearing of this robot relative to the robot calling this function
        double absoluteBearing = normalAbsoluteAngleDegrees(robot.getHeading() + e.getBearing());
        x = robot.getX() + e.getDistance() * Math.sin(Math.toRadians(absoluteBearing));
        y = robot.getY() + e.getDistance() * Math.cos(Math.toRadians(absoluteBearing));
    }

    /**
     * This function update robot object with data relative to the robot parameter
     * @param robot - robot that scanned that robot
     */
    public void updateRelativeToTheRobot(Robot robot) {
        double x0 = x-robot.getX();
        double y0 = y-robot.getY();
        double bearingFromTheNorth = Math.toDegrees(Math.atan2(x0, y0));
        double absoluteBearing = bearingFromTheNorth - robot.getHeading();
        bearing = normalRelativeAngleDegrees(absoluteBearing);
        distance = Math.sqrt(Math.pow(x0, 2) + Math.pow(y0, 2));
    }

    /**
     * This function reset object with default values
     */
    public void reset() {
        name = "";
        bearing = 0.0;
        distance = 99999999.0;
        energy = 0.0;
        heading = 0.0;
        velocity = 0.0;
        x = 0.0;
        y = 0.0;
    }

    public boolean isEmpty() {
        return name.equals("");
    }

    public String getName() {
        return name;
    }

    public double getBearing() {
        return bearing;
    }

    public double getDistance() {
        return distance;
    }

    public double getEnergy() {
        return energy;
    }

    public double getHeading() {
        return heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
