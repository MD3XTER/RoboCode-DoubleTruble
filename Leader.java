package nl.saxion.dhi1vsq3;

import robocode.DeathEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.io.IOException;

abstract class Leader extends CustomRobot {
    String follower;

    public void setFollower(String follower) {
        this.follower = findTeammate(follower);
        try {
            sendMessage(follower, new TakeLead());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
//      check if scanned robot is enemy
        if (!isTeammate(e.getName())){
//          update enemy if it is different or closer
            if (getEnemy().none() || e.getDistance() < getEnemy().getDistance() || e.getName().equals(getEnemy().getName())) {
//              update enemy
                getEnemy().update(e, this);

//              send the enemyToFollow to the enemy
                try {
                    sendMessage(follower, new EnemyMessage(getEnemy()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
//      send a message to follower that he should take the lead
        try {
            sendMessage(follower, new TakeLead());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
//      delete enemy after killed
        if (e.getName().equals(getEnemy().getName())) {
            getEnemy().reset();
        }
    }
}
