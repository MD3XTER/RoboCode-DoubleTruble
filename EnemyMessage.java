package nl.saxion.zgirvaci;

import java.io.Serializable;

public class EnemyMessage implements Serializable{
    EnemyRobot enemyToFollow;

    public EnemyMessage(EnemyRobot enemyRobot) {
        enemyToFollow = enemyRobot;
    }

    public EnemyRobot getEnemyToFollow() {
        return enemyToFollow;
    }
}
