package nl.saxion.dhi1vsq3;

import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

public abstract class Follower extends CustomRobot {

    boolean takeLead = false;

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
//      check if scanned robot is enemy
        if (!isTeammate(e.getName())){
//          update enemy if it is different or closer
            if (getEnemy().none() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName())) {
//              update enemy
                getEnemy().update(e, this);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
//      if leader send enemy location
        if (event.getMessage() instanceof EnemyMessage) {
//          get enemy from the leader
            EnemyRobot enemy = ((EnemyMessage) event.getMessage()).getEnemy();
//          update enemy relative to this robot
            enemy.updateRelativeToTheRobot(this);
//          update enemy
            setEnemy(enemy);
        }
//      if leader is dead
        else if (event.getMessage() instanceof TakeLead) {
            takeLead = true;
        }
    }

    public boolean isTakeLead() {
        return takeLead;
    }
}
