package nl.saxion.dhi1vsq3;

import robocode.RobotDeathEvent;
import robocode.TeamRobot;

import static robocode.util.Utils.normalRelativeAngleDegrees;

abstract class CustomRobot extends TeamRobot {
    private ScannedRobot enemy = new ScannedRobot();
    private ScannedRobot robotInFrontOfUs = new ScannedRobot();
    private byte moveDirection = 1;
    private boolean allyIsInFrontOfUs = false;

    /**
     * Shoot enemy strategy
     */
    public void attackEnemy() {
        // check if we have a instance of an enemy to kill
        if (!enemy.isEmpty()) {
            // turn gun to enemy
            setTurnGunRight(getGunBearing(enemy.getBearing()));

            final double ERROR_MARGIN = 1.0;
            // fire after gun has turned to the enemy with a error margin instead of just checking if getGunTurnRemaining == 0
            // we don't want to be too precise about this, no one is perfect :)
            if (Math.abs(getGunTurnRemaining()) <= ERROR_MARGIN) {
                // calculate maximum distance between two robots possible depending on the map size
                double maxDistance = Math.sqrt(Math.pow(getBattleFieldWidth(), 2) + Math.pow(getBattleFieldHeight(), 2)) - 100;
                // calculate which size of bullets to use based on enemy distance
                double bullets = Math.min(maxDistance / enemy.getDistance(), 3);
                // wait until gun heat is 0
                if (getGunHeat() == 0.0) {
                    setFire(bullets);
                }
            }
        }
    }

    /**
     * move to enemy strategy
     */
    public void moveToEnemy() {
        final double CLOSE_MARGIN = 100.0;
        final double ERROR_MARGIN = 25.0;

        // if a teammate is in front of us, we want to avoid bumping into it, so instead of just moving in the enemy direction
        // we will do a strafing in the enemy direction, search on google what strafing means :)
        if (allyIsInFrontOfUs) {
            // switch directions if we've stopped, that usually means we have collided into something
            if (Math.abs(getVelocity()) <= 0) {
                moveDirection *= -1;
            }
            // do a strafing in the enemy direction
            setTurnRight(normalRelativeAngleDegrees(enemy.getBearing() + 90 - (15 * moveDirection)));
            setAhead(1000 * moveDirection);
        }
        // if no teammates are in front of us, we want our robot to just head in the enemy direction
        else {
            // head towards the enemy
            setTurnRight(enemy.getBearing());
            // I have already discussed why we need a error margin in the attackEnemy function
            if (Math.abs(getTurnRemaining()) <= ERROR_MARGIN) {
                setTurnRight(0.0);
            }
            // move towards the enemy with a close margin, we want to keep a safe distance between us and enemy robot
            // we don't want to collide with them
            setAhead(enemy.getDistance() - CLOSE_MARGIN);
        }
    }

    /**
     * This function calculates gun bearing by knowing the robot bearing relative to another robot
     * @param robotBearing - is the robot bearing relative to another robot, this is usually calculated from enemy.getBearing();
     * @return calculated normalized gun bearing
     */
    public double getGunBearing(double robotBearing) {
        double absoluteBearing = getHeading() + robotBearing;
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        return bearingFromGun;
    }

    /**
     * This function finds complete name(including package name) of a teammate by knowing partial name of the robot
     * @param teammate - partial name
     * @return complete name of the teammate
     */
    public String findTeammate(String teammate) {
        for (String name : getTeammates()) {
            if (name.contains(teammate)) {
                return name;
            }
        }
        return null;
    }

    /**
     * This function resets enemy and robotInFrontOfUs objects when robots are dead
     * @param e
     */
    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        if (e.getName().equals(enemy.getName())) {
            enemy.reset();
        }
        if (e.getName().equals(robotInFrontOfUs.getName())) {
            robotInFrontOfUs.reset();
        }
    }

    /**
     * @return enemy object
     */
    public ScannedRobot getEnemy() {
        return enemy;
    }

    /**
     * @return robotInFrontOfUs object
     */
    public ScannedRobot getRobotInFrontOfUs() {
        return robotInFrontOfUs;
    }

    /**
     * This function sets enemy object that robot should kill
     * @param scannedRobot
     */
    public void setEnemy(ScannedRobot scannedRobot) {
        enemy = scannedRobot;
    }

    /**
     * This function sets allyIsInFrontOfUs, this will help us in the future to avoid friendly collision and fire
     * @param allyIsInFrontOfUs
     */
    public void setAllyIsInFrontOfUs(boolean allyIsInFrontOfUs) {
        this.allyIsInFrontOfUs = allyIsInFrontOfUs;
    }
}
