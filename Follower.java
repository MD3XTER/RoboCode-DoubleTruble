package nl.saxion.dhi1vsq3;

import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

public abstract class Follower extends CustomRobot {

    private boolean takeLead = false;

    /**
     * I think this function speaks for itself :)
     * I have described everything in the comments
     * @param e
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        final double SAFE_MARGIN = 100.0;
        double gunBearingFromScannedRobot = getGunBearing(e.getBearing());
        double gunBearingFromRobotInFrontOfUs = getGunBearing(getRobotInFrontOfUs().getBearing());
        // if the scanned robot is closer to us than the robotInFrontOfUs and the current scanned robot is in front of us
        // we also have a safe margin, we want to specify what (in front of us) means
        if (e.getDistance() < getRobotInFrontOfUs().getDistance() && (Math.abs(gunBearingFromScannedRobot) < SAFE_MARGIN)) {
            // update robotInFrontOfUs
            getRobotInFrontOfUs().update(e, this);
        }
        // if robotInFrontOfUs is not any more in front of us, rest it
        if (Math.abs(gunBearingFromRobotInFrontOfUs) > SAFE_MARGIN) {
            getRobotInFrontOfUs().reset();
        }
        // check if scanned robot is enemy
        if (!isTeammate(e.getName())) {
            // check if scanned is closer than the previous enemy or we don't have any enemy instances
            // or the scanned robot is the same as previous one or this robot is not dependent on the leader
            if (getEnemy().isEmpty() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName()) && takeLead) {
                // update enemy we want to kill with the current scanned robot
                getEnemy().update(e, this);
            }
            // don't fire if the robot that is in front of us is friendly
            if (isTeammate(getRobotInFrontOfUs().getName())) {
                setAllyIsInFrontOfUs(true);
            }
            // if not then attack our enemy robot
            else {
                setAllyIsInFrontOfUs(false);
                attackEnemy();
            }
        }
    }

    /**
     * I think this function speaks for itself :)
     * I have described everything in the comments
     * @param event
     */
    @Override
    public void onMessageReceived(MessageEvent event) {
        // if leader sent enemy location update enemy object
        if (event.getMessage() instanceof EnemyMessage && !takeLead) {
            // get enemy from the leader
            ScannedRobot enemy = ((EnemyMessage) event.getMessage()).getEnemy();
            // update enemy relative to this robot, we use this function instead of update because
            // getBearing method will still be relative to the robot that scanned enemy robot, which is leader
            // we want to get relative getBearing for this robot, not leader
            enemy.updateRelativeToTheRobot(this);
            // update enemy object
            setEnemy(enemy);
        }
        // if leader is dead, take lead
        else if (event.getMessage() == "take_lead") {
            this.takeLead = true;
        }
    }
}
