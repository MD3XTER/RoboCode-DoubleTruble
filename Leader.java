package nl.saxion.dhi1vsq3;

import robocode.CustomEvent;
import robocode.ScannedRobotEvent;

import java.io.IOException;

abstract class Leader extends CustomRobot {
    private String follower;

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
            // check if scanned is closer than the previous enemy or we don't have any enemy instances or the scanned robot is the same as previous one
            if (getEnemy().isEmpty() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName())) {
                // update enemy we want to kill with the current scanned robot
                getEnemy().update(e, this);
                // send to the follower which enemy he needs to kill
                try {
                    sendMessage(follower, new EnemyMessage(getEnemy()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
     * When this event is fired we send a message to our follower telling him that he should take the lead
     * @param e
     */
    public void onCustomEvent(CustomEvent e) {
        if (e.getCondition().getName().equals("make_follower_individual")) {
            try {
                sendMessage(this.follower, "take_lead");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * This function searches a teammate based on the partial name and set is as our follower
     * @param follower - follower partial name
     */
    public void setFollower(String follower) {
        this.follower = findTeammate(follower);
    }
}