package nl.saxion.zgirvaci;

import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

abstract class Leader extends CustomRobot {
    String follower;
    EnemyRobot enemy;

    public void setFollower(String follower) {
        this.follower = findTeammate(follower);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
//      check if is a enemy robot
        if (!isTeammate(e.getName())){

//          update enemy if it is different or closer
            if (enemy.none() || e.getDistance() < enemy.getDistance() || e.getName().equals(enemy.getName())) {
                enemy.update(e, this);
            }

//          call the shoot method
            shootEnemy(enemy);
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
//      delete enemy after killed
        if (e.getName().equals(enemy.getName())) {
            enemy.reset();
        }
    }
}
