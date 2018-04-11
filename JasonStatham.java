package nl.saxion.dhi1vsq3;

import java.awt.*;

public class JasonStatham extends Follower{

    public void run() {
        // we want this to be independent
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        // set robot color
        setGunColor(new Color(51, 102, 0));
        setBodyColor(new Color(102, 102, 51));
        setScanColor(new Color(51, 102, 0));
        setRadarColor(new Color(0, 0, 0));

        while (true) {
            // activate scanner
            setTurnRadarRight(360);

            // move towards the enemy
            moveToEnemy();

            // attack enemy
            attackEnemy();

            execute();
        }
    }

}
