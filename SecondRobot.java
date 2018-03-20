package nl.saxion.zgirvaci;

public class SecondRobot extends CustomRobot{

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
//          start scanning
            setTurnRadarRight(360);

            execute();
        }
    }

}
