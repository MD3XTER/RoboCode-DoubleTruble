package nl.saxion.dhi1vsq3;

import robocode.CustomEvent;
import robocode.ScannedRobotEvent;

import java.io.IOException;

abstract class Leader extends CustomRobot {
    String follower;

    public void setFollower(String follower) {
        this.follower = findTeammate(follower);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        final double ERROR_MARGIN = 50.0;
        double bearingFromEventRobotGun = getGunBearing(e.getBearing());
        double bearingFromRangeRobotGun = getGunBearing(getRobotInRange().getBearing());


        if (e.getDistance() < getRobotInRange().getDistance() && (Math.abs(bearingFromEventRobotGun) < Math.abs(bearingFromRangeRobotGun))) {
            getRobotInRange().update(e, this);
        }

        if (Math.abs(bearingFromRangeRobotGun) > ERROR_MARGIN) {
            getRobotInRange().reset();
        }

//      check if scanned robot is enemy
        if (!isTeammate(e.getName())) {
//          update enemy if it is different or closer
            if (getEnemy().none() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName())) {
//              update enemy
                getEnemy().update(e, this);

//              send the enemyToFollow to the enemy
                try {
                    sendMessage(follower, new EnemyMessage(getEnemy()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
//          check if scanned robot is ally
            if (isTeammate(getRobotInRange().getName())) {
                setStrafing(true);
            }
            else {
                setStrafing(false);
                attackEnemy();
            }
        }
    }

    public void onCustomEvent(CustomEvent e) {
        if (e.getCondition().getName().equals("make_follower_individual")) {
//          send a message to my follower that he should become individual
            try {
                sendMessage(this.follower, new TakeLeadMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}