package nl.saxion.dhi1vsq3;

import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

public abstract class Follower extends CustomRobot {

    boolean takeLead = false;

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
            if ((getEnemy().none() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName())) && takeLead) {
//              update enemy
                getEnemy().update(e, this);
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

    @Override
    public void onMessageReceived(MessageEvent event) {
//      if leader send enemy location
        if (event.getMessage() instanceof EnemyMessage && !takeLead) {
//          get enemy from the leader
            ScannedRobot enemy = ((EnemyMessage) event.getMessage()).getEnemy();
//          update enemy relative to this robot
            enemy.updateRelativeToTheRobot(this);
//          update enemy
            setEnemy(enemy);
        }
//      if leader is dead
        else if (event.getMessage() instanceof TakeLeadMessage) {
            this.takeLead = true;
        }
    }
}
