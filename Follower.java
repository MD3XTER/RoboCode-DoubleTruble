package nl.saxion.dhi1vsq3;

import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

public abstract class Follower extends CustomRobot {

    boolean takeLead = false;

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        final double ERROR_MARGIN = 50.0;
        double bearingFromGun = getGunBearing(e.getBearing());

//      check if scanned robot is ally
        if (isTeammate(e.getName()) && Math.abs(bearingFromGun) <= ERROR_MARGIN) {
            setStrafing(true);
        }
//      check if scanned robot is enemy
        else if (!isTeammate(e.getName())) {
//          update enemy if it is different or closer
            if ((getEnemy().none() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName())) && takeLead) {
//              update enemy
                getEnemy().update(e, this);
            }

            setStrafing(false);
            attackEnemy();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
//      if leader send enemy location
        if (event.getMessage() instanceof EnemyMessage && !takeLead) {
//          get enemy from the leader
            EnemyRobot enemy = ((EnemyMessage) event.getMessage()).getEnemy();
//          update enemy relative to this robot
            enemy.updateRelativeToTheRobot(this);
//          update enemy
            setEnemy(enemy);
        }
//      if a leader message me about my team
        if (event.getMessage() instanceof TakeLeadMessage) {
            takeLead = ((TakeLeadMessage) event.getMessage()).isTakeLead();
            System.out.printf("takeLead: %b\n", takeLead);
        }
    }
}
