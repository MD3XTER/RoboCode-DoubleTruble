package nl.saxion.dhi1vsq3;

import java.awt.*;

public class SecondRobot extends Follower{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

//      set color
        setGunColor(new Color(51, 102, 0));
        setBodyColor(new Color(102, 102, 51));
        setScanColor(new Color(51, 102, 0));
        setRadarColor(new Color(0, 0, 0));

        while (true) {
            setTurnRadarRight(360);

            moveToEnemy();

            attackEnemy();

            execute();
        }
    }

}
