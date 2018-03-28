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
        this.robotWhoWantsToJoin = "";
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

//    @Override
//    public void onMessageReceived(MessageEvent event) {
////      if other leader died
//        if (event.getMessage() instanceof JoinTeamMessage) {
////          set robot who wants to join this team
//            this.robotWhoWantsToJoin = ((JoinTeamMessage) event.getMessage()).getRobotWhoWantsToJoin();
//            System.out.printf("Robot who want's to join: %s", this.robotWhoWantsToJoin);
//        }
//        else if (event.getMessage() instanceof GetFollowerMessage) {
//            if (this.robotWhoWantsToJoin.isEmpty()) {
//                GetFollowerMessage getFollowerMessage = (GetFollowerMessage) event.getMessage();
//                getFollowerMessage.setLeaderFollower(this.follower);
//                System.out.printf("I have sent my follower %s", this.follower);
//            }
//            else {
//                this.robotWhoWantsToJoin = ((GetFollowerMessage) event.getMessage()).getLeaderFollower();
//                System.out.printf("Robot who want's to join: %s", this.robotWhoWantsToJoin);
//            }
//        }
//    }

    @Override
    public void onDeath(DeathEvent event) {
//      send a message to follower that he should take the lead
        try {
            sendMessage(this.follower, new TakeLeadMessage(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
////      send a message to the other leader that my follower want's to join his team
//        try {
//            sendMessage(this.otherLeader, new JoinTeamMessage(this.follower));
//            System.out.printf("I have sent %s that %s should join him\n", this.otherLeader, this.follower);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
//      delete enemy after killed
        if (e.getName().equals(getEnemy().getName())) {
            getEnemy().reset();
        }
////      set new follower if current follower is dead
//        else if (e.getName().equals(this.follower)) {
//            System.out.printf("My follower died: %s\n", this.follower);
////          if the other leader is dead
//            if (this.robotWhoWantsToJoin.isEmpty())  {
//                this.follower = this.robotWhoWantsToJoin;
//                try {
//                    sendMessage(this.robotWhoWantsToJoin, new TakeLeadMessage(false));
//                    System.out.printf("My new follower is: %s\n", this.robotWhoWantsToJoin);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
////          if the other leader is alive
//            else {
//                try {
//                    sendMessage(this.otherLeader, new GetFollowerMessage());
//                    System.out.printf("I have requested from %s his follower\n", this.otherLeader);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
    }
}
