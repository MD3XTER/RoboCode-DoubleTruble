package nl.saxion.dhi1vsq3;

import java.awt.*;

public class ThirdRobot extends Leader{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

//      set color
        setGunColor(new Color(255, 0, 0));
        setBodyColor(new Color(255, 0, 0));
        setScanColor(new Color(255, 0, 0));
        setRadarColor(new Color(255, 0, 0));

        setFollower("FourthRobot");

        while (true) {
//          scan for enemies
            setTurnRadarRight(360);

//          attack enemy
            attackEnemy();

            execute();
        }
    }

}
