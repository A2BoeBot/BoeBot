package interfaces;

import TI.BoeBot;
import applicatie.RobotMain;
import applicatie.Updatable;
import hardware.motor.Grijper;
import hardware.motor.Motors;
import hardware.sensoren.Ultrasoon;
import hardware.sensoren.UltrasoonCallback;

import java.util.ArrayList;

public class UltrasoonHandler implements Updatable {
    private ArrayList<Ultrasoon> ultrasoonsOnder = new ArrayList<>();
    private ArrayList<Ultrasoon> ultrasoonsBoven = new ArrayList<>();
    private ArrayList<Ultrasoon> ultrasoonsAchter = new ArrayList<>();
    private ArrayList<Ultrasoon> ultrasoons = new ArrayList<>();
    private double stopAfstand;
    private double gevaarAfstand;

    private UltrasoonCallback callback;
    private Motors motors;
    private Grijper grijper;

    public UltrasoonHandler(UltrasoonCallback callback) {
        this.callback = callback;
        this.gevaarAfstand = 1;
        this.stopAfstand = 0.2;
    }

    public double getStopAfstand() {
        return stopAfstand;
    }

    public double getGevaarAfstand() {
        return gevaarAfstand;
    }

    public void setStopAfstand(double stopAfstand) {
        this.stopAfstand = stopAfstand;
    }

    public void setGevaarAfstand(double gevaarAfstand) {
        this.gevaarAfstand = gevaarAfstand;
    }

    public void voegUltrasoonToe(Ultrasoon ultrasoonSensor, int positie) {
        this.ultrasoons.add(ultrasoonSensor);
        if (positie == 0)
            ultrasoonsBoven.add(ultrasoonSensor);
        else if (positie == 1)
            ultrasoonsOnder.add(ultrasoonSensor);
        else if (positie == 2)
            ultrasoonsAchter.add(ultrasoonSensor);
    }

    @Override
    public void update() {
        int snelheid = (motors.getSnelheidLinks() + motors.getSnelheidRechts()) / 2;
        if (snelheid < 0) {
            for (Ultrasoon ultrasoon : ultrasoonsAchter) {
                doCallback(ultrasoon);
            }
        } else {
            if (grijper.getDutyCycle() == grijper.getOpenDuty()) {
                for (Ultrasoon ultrasoon : ultrasoonsOnder) {
                    doCallback(ultrasoon);
                }
            } else {
                for (Ultrasoon ultrasoon : ultrasoonsBoven) {
                    doCallback(ultrasoon);
                }
            }
        }
    }

    private void doCallback(Ultrasoon ultrasoon) {
        if (ultrasoon.getAfstand() < stopAfstand)
            callback.stopAfstand(ultrasoon);
        else if (ultrasoon.getAfstand() < gevaarAfstand)
            callback.gevaarAfstand(ultrasoon);
        if (ultrasoon.getAfstand() > gevaarAfstand && ultrasoon.getAfstand() > stopAfstand)
            callback.clearAfstand(ultrasoon);
    }

    public void setMotors(Motors motors) {
        this.motors = motors;
    }

    public void setGrijper(Grijper grijper) {
        this.grijper = grijper;
    }
}
