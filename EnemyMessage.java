package nl.saxion.DHI1VSq3;

import java.io.Serializable;

public class EnemyMessage implements Serializable{
    EnemyRobot enemy;

    public EnemyMessage(EnemyRobot enemyRobot) {
        enemy = enemyRobot;
    }

    public EnemyRobot getEnemy() {
        return enemy;
    }
}
