package nl.saxion.dhi1vsq3;

import java.io.Serializable;

public class EnemyMessage implements Serializable{
    ScannedRobot enemy;

    public EnemyMessage(ScannedRobot scannedRobot) {
        enemy = scannedRobot;
    }

    public ScannedRobot getEnemy() {
        return enemy;
    }
}
