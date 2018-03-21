package nl.saxion.zgirvaci;

public class FirstRobot extends Leader{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        setFollower("SecondRobot");

        while (true) {
//          start scanning
            setTurnRadarRight(360);

            execute();
        }
    }

}
