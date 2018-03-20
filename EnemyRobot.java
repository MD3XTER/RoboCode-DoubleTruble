package nl.saxion.zgirvaci;

import robocode.Robot;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalAbsoluteAngleDegrees;

public class EnemyRobot {
    private String name;
    private double bearing;
    private double distance;
    private double energy;
    private double heading;
    private double velocity;
    private double x;
    private double y;

    public EnemyRobot() {
        reset();
    }

    public void update(ScannedRobotEvent e, Robot robot) {
        name = e.getName();
        bearing = e.getBearing();
        distance = e.getDistance();
        energy = e.getEnergy();
        heading = e.getHeading();
        velocity = e.getVelocity();
        double absoluteBearing = normalAbsoluteAngleDegrees(robot.getHeading() + e.getBearing());
        x = robot.getX() + e.getDistance() * Math.sin(Math.toRadians(absoluteBearing));
        y = robot.getY() + e.getDistance() * Math.cos(Math.toRadians(absoluteBearing));
    }

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

    public boolean none() {
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