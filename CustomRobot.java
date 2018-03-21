package nl.saxion.zgirvaci;

import robocode.TeamRobot;

import static robocode.util.Utils.normalRelativeAngleDegrees;

abstract class CustomRobot extends TeamRobot {
    private EnemyRobot enemy = new EnemyRobot();

    public void attackEnemy() {
//      check if there is actually an enemy
        if (!enemy.none()) {
//          calculate correct bearing between gun and
            double absoluteBearing = getHeading() + enemy.getBearing();
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

//          turn gun to enemy
            setTurnGunRight(bearingFromGun);

            followEnemy();

//          fire after gun has turned to the enemy
            final double ERROR_MARGIN = 1.0;
            if (Math.abs(getGunTurnRemaining()) <= ERROR_MARGIN) {
//              calculate maximum distance possible between robots
                double maxDistance = Math.sqrt(Math.pow(getBattleFieldWidth(), 2) + Math.pow(getBattleFieldHeight(), 2)) - 100;
//              calculate which size of bullets to use
                double bullets = Math.min(maxDistance / enemy.getDistance(), 3);

//              wait until gun heat is 0
                if (getGunHeat() == 0.0) {
                    setFire(bullets);
                }
            }
        }
    }

    public void followEnemy() {
        final double CLOSE_MARGIN = 100.0;

//      head towards the enemy
        final double ERROR_MARGIN = 25.0;
        setTurnRight(enemy.getBearing());
        if (Math.abs(getTurnRemaining()) <= ERROR_MARGIN) {
            setTurnRight(0.0);
        }

//      move towards the enemy with a close margin
        setAhead(enemy.getDistance() - CLOSE_MARGIN);
    }

    public String findTeammate(String teammate) {
        for (String name: getTeammates()) {
            if (name.contains(teammate)) {
                return name;
            }
        }
        return null;
    }

    public EnemyRobot getEnemy() {
        return enemy;
    }

    public void setEnemy(EnemyRobot enemyRobot) {
        enemy = enemyRobot;
    }
}
