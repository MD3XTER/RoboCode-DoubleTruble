package nl.saxion.dhi1vsq3;

import robocode.Condition;

import java.awt.*;

public class BruceWillis extends Leader{

    public void run() {
        // we want this to be independent
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        // set robot color
        setGunColor(new Color(217, 250, 27));
        setBodyColor(new Color(163, 8, 29));
        setScanColor(new Color(230, 0, 0));
        setRadarColor(new Color(0,0,0));

        // when this robot is nearly dead, it will fire the custom event
        addCustomEvent(new Condition("make_follower_individual") {
            public boolean test() {
                return (getEnergy() <= 20);
            }
        });

        // set our follower
        setFollower("SylvesterStallone");

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