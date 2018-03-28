package nl.saxion.dhi1vsq3;

import java.io.Serializable;

public class JoinTeamMessage implements Serializable{
    String robotWhoWantsToJoin;

    public JoinTeamMessage(String robotToJoin) {
        this.robotWhoWantsToJoin = robotToJoin;
    }

    public String getRobotWhoWantsToJoin() {
        return robotWhoWantsToJoin;
    }
}
