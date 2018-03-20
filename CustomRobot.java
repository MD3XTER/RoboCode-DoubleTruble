package nl.saxion.zgirvaci;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.util.ArrayList;

import static robocode.util.Utils.normalRelativeAngleDegrees;

abstract class CustomRobot extends TeamRobot {
    private static ArrayList<String> teamRobots = new ArrayList<>();
    private static EnemyRobot enemyToFollow = new EnemyRobot();

    CustomRobot() {
//      TODO - add allies here
        teamRobots.add("nl.saxion.zgirvaci.FirstRobot*");
        teamRobots.add("nl.saxion.zgirvaci.SecondRobot*");
        teamRobots.add("nl.saxion.zgirvaci.ThirdRobot*");
        teamRobots.add("nl.saxion.zgirvaci.FourthRobot*");
    }

    public void onScannedRobot(ScannedRobotEvent e) {
//      check if is a enemy robot
        if (!teamRobots.contains(e.getName())){

//          update enemy if it is different or closer
            if (enemyToFollow.none() || e.getDistance() < enemyToFollow.getDistance() || e.getName().equals(enemyToFollow.getName())) {
                enemyToFollow.update(e, this);
            }

//          calculate correct bearing between gun and
            double absoluteBearing = getHeading() + enemyToFollow.getBearing();
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

//          turn gun to enemy
            setTurnGunRight(bearingFromGun);

//          fire after gun has turned to the enemy
            double ERROR_MARGIN = 3.0;
            if (Math.abs(getGunTurnRemaining()) <= ERROR_MARGIN) {
//              calculate maximum distance possible between robots
                double maxDistance = Math.sqrt(Math.pow(getBattleFieldWidth(),2)+Math.pow(getBattleFieldHeight(),2)) - 100;
//              calculate which size of bullets to use
                double bullets = Math.min(maxDistance / enemyToFollow.getDistance(), 3);

//              wait until gun heat is 0
                if (getGunHeat() == 0.0) {
                    setFire(bullets);
                } else {
                    moveToEnemy();
                }
            }
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
//      delete enemy after killed
        if (e.getName().equals(enemyToFollow.getName())) {
            enemyToFollow.reset();
        }
    }

    void moveToEnemy() {
        double CLOSE_MARGIN = 200.0;

//      head towards the enemy
        setTurnRight(enemyToFollow.getBearing());

//      move towards the enemy with a close margin
        setAhead(enemyToFollow.getDistance() - CLOSE_MARGIN);
    }

}
