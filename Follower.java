package nl.saxion.zgirvaci;

import robocode.MessageEvent;

public abstract class Follower extends CustomRobot {

    @Override
    public void onMessageReceived(MessageEvent event) {
        if (event.getMessage() instanceof EnemyMessage) {
//          get enemy from the leader
            EnemyRobot enemy = ((EnemyMessage) event.getMessage()).getEnemy();
//          update enemy relative to this robot
            enemy.updateRelativeToTheRobot(this);
//          update enemy
            setEnemy(enemy);
        }
    }
}
