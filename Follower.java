package nl.saxion.zgirvaci;

import robocode.MessageEvent;

public abstract class Follower extends CustomRobot {
    private EnemyRobot enemyToFollow;

    @Override
    public void onMessageReceived(MessageEvent event) {
        if (event.getMessage() instanceof EnemyMessage) {
            enemyToFollow = (EnemyRobot) event.getMessage();
        }
    }
}
