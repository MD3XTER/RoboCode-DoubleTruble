package nl.saxion.dhi1vsq3;

import robocode.RobotDeathEvent;
import robocode.TeamRobot;

import static robocode.util.Utils.normalRelativeAngleDegrees;

abstract class CustomRobot extends TeamRobot {
    private ScannedRobot enemy = new ScannedRobot();
    private ScannedRobot robotInRange = new ScannedRobot();
    private byte moveDirection = 1;
    private boolean strafing = false;

    public void attackEnemy() {
        // check if there is actually an enemy
        if (!enemy.none()) {
            // turn gun to enemy
            setTurnGunRight(getGunBearing(enemy.getBearing()));

            // fire after gun has turned to the enemy
            final double ERROR_MARGIN = 1.0;
            if (Math.abs(getGunTurnRemaining()) <= ERROR_MARGIN) {
                // calculate maximum distance possible between robots
                double maxDistance = Math.sqrt(Math.pow(getBattleFieldWidth(), 2) + Math.pow(getBattleFieldHeight(), 2)) - 100;
                // calculate which size of bullets to use
                double bullets = Math.min(maxDistance / enemy.getDistance(), 3);

                // wait until gun heat is 0
                if (getGunHeat() == 0.0) {
                    setFire(bullets);
                }
            }
        }
    }

    public void moveToEnemy() {
        final double CLOSE_MARGIN = 100.0;
        final double ERROR_MARGIN = 25.0;

        if (strafing) {
            // switch directions if we've stopped
            if (Math.abs(getVelocity()) <= 0) {
                moveDirection *= -1;
            }

            // circle our enemy
            setTurnRight(normalRelativeAngleDegrees(enemy.getBearing() + 90 - (15 * moveDirection)));
            setAhead(1000 * moveDirection);
        } else {
            // head towards the enemy
            setTurnRight(enemy.getBearing());
            if (Math.abs(getTurnRemaining()) <= ERROR_MARGIN) {
                setTurnRight(0.0);
            }

            // move towards the enemy with a close margin
            setAhead(enemy.getDistance() - CLOSE_MARGIN);
        }
    }

    public double getGunBearing(double robotBearing) {
        // calculate correct bearing between gun and
        double absoluteBearing = getHeading() + robotBearing;
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        return bearingFromGun;
    }

    public String findTeammate(String teammate) {
        for (String name : getTeammates()) {
            if (name.contains(teammate)) {
                return name;
            }
        }
        return null;
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        // delete enemy after killed
        if (e.getName().equals(enemy.getName())) {
            enemy.reset();
        }
        if (e.getName().equals(robotInRange.getName())) {
            robotInRange.reset();
        }
    }

    public ScannedRobot getEnemy() {
        return enemy;
    }

    public ScannedRobot getRobotInRange() {
        return robotInRange;
    }

    public void setEnemy(ScannedRobot scannedRobot) {
        enemy = scannedRobot;
    }

    public void setStrafing(boolean strafing) {
        this.strafing = strafing;
    }
}
