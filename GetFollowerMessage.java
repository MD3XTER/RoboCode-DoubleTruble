package nl.saxion.dhi1vsq3;

import java.io.Serializable;

public class GetFollowerMessage implements Serializable {
    String leaderFollower;

    public String getLeaderFollower() {
        return leaderFollower;
    }

    public void setLeaderFollower(String leaderFollower) {
        this.leaderFollower = leaderFollower;
    }
}
