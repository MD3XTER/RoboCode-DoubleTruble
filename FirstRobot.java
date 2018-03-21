package nl.saxion.zgirvaci;

import java.awt.*;

public class FirstRobot extends Leader{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

//      set color
        setGunColor(new Color(255, 0, 0));
        setBodyColor(new Color(255, 0, 0));
        setScanColor(new Color(255, 0, 0));
        setRadarColor(new Color(255, 0, 0));

        setFollower("SecondRobot");

        while (true) {
//          scan for enemies
            setTurnRadarRight(360);

//          attack enemy
            attackEnemy();

            execute();
        }
    }

}
