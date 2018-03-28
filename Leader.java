package nl.saxion.dhi1vsq3;

import robocode.DeathEvent;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.io.IOException;

abstract class Leader extends CustomRobot {
    String follower;
    String otherLeader;
    String robotWhoWantsToJoin;

    public void setFollower(String follower, String otherLeader) {
        this.follower = findTeammate(follower);
        this.otherLeader = findTeammate(otherLeader);
        robotWhoWantsToJoin = "";
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        final double ERROR_MARGIN = 50.0;
        double bearingFromGun = getGunBearing(e.getBearing());

//      check if scanned robot is ally
        if (isTeammate(e.getName()) && Math.abs(bearingFromGun) <= ERROR_MARGIN) {
            setStrafing(true);
        }
//      check if scanned robot is enemy
        else if (!isTeammate(e.getName())) {
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
            setStrafing(false);
            attackEnemy();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
//      if other leader died
        if (event.getMessage() instanceof JoinTeamMessage) {
//          set robot who wants to join this team
            robotWhoWantsToJoin = ((JoinTeamMessage) event.getMessage()).getRobotWhoWantsToJoin();
            System.out.printf("Robot who want's to join: %s", robotWhoWantsToJoin);
        }
        else if (event.getMessage() instanceof GetFollowerMessage) {
            if (robotWhoWantsToJoin.isEmpty()) {
                GetFollowerMessage getFollowerMessage = (GetFollowerMessage) event.getMessage();
                getFollowerMessage.setLeaderFollower(follower);
                System.out.printf("I have sent my follower %s", follower);
            }
            else {
                robotWhoWantsToJoin = ((GetFollowerMessage) event.getMessage()).getLeaderFollower();
                System.out.printf("Robot who want's to join: %s", robotWhoWantsToJoin);
            }
        }
    }

    @Override
    public void onDeath(DeathEvent event) {
//      send a message to follower that he should take the lead
        try {
            sendMessage(follower, new TakeLeadMessage(true));
            System.out.printf("I %s am dying\n", this.getName());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
//      send a message to the other leader that my follower want's to join his team
        try {
            sendMessage(otherLeader, new JoinTeamMessage(follower));
            System.out.printf("I have sent %s that %s should join him\n", otherLeader, follower);
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
//      set new follower if current follower is dead
        else if (e.getName().equals(follower)) {
            System.out.printf("My follower died: %s\n", follower);
//          if the other leader is dead
            if (robotWhoWantsToJoin.isEmpty()) {
                follower = robotWhoWantsToJoin;
                try {
                    sendMessage(robotWhoWantsToJoin, new TakeLeadMessage(false));
                    System.out.printf("My new follower is: %s\n", robotWhoWantsToJoin);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
//          if the other leader is alive
            else {
                try {
                    sendMessage(otherLeader, new GetFollowerMessage());
                    System.out.printf("I have requested from %s his follower\n", otherLeader);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
