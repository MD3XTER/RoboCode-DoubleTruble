package nl.saxion.dhi1vsq3;

import robocode.Condition;

import java.awt.*;

public class FirstRobot extends Leader{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

//      set color
        setGunColor(new Color(217, 250, 27));
        setBodyColor(new Color(163, 8, 29));
        setScanColor(new Color(230, 0, 0));
        setRadarColor(new Color(0,0,0));

        addCustomEvent(new Condition("make_follower_individual") {
            public boolean test() {
                return (getEnergy() <= 20);
            }
        });

        setFollower("SecondRobot");

        while (true) {
//          scan for enemies
            setTurnRadarRight(360);

            moveToEnemy();

            attackEnemy();

            execute();
        }
    }

}
