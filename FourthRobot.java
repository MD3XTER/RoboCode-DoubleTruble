package nl.saxion.dhi1vsq3;

import java.awt.*;

public class FourthRobot extends Follower{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

//      set color
        setGunColor(new Color(0, 0, 255));
        setBodyColor(new Color(0, 0, 255));
        setScanColor(new Color(0, 0, 255));
        setRadarColor(new Color(0, 0, 255));

        while (true) {
//          attack enemy
            attackEnemy();

            execute();
        }
    }

}
