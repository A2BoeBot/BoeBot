package robot;

import TI.BoeBot;
import motor.Motor;
import motor.MotorCallback;

import java.util.ArrayList;

public class RobotMain implements MotorCallback {
    private Motor linksMotor, rechtsMotor;
    private ArrayList<Updatable> updatables = new ArrayList<>();

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        updatables.add(linksMotor = new Motor(12, this));
        updatables.add(rechtsMotor = new Motor(13, this));
        linksMotor.zetSnelheid(1400, 1000);
        rechtsMotor.zetSnelheid(1400, 1000);
    }

    private void run() {
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
            BoeBot.wait(1);
        }
    }

    @Override
    public void updateMotor(Motor motor, int snelheid) {
        final int DUTYCYCLE = 1500;
        if (motor == linksMotor) {
            linksMotor.servo.update(DUTYCYCLE + snelheid);
        } else if (motor == rechtsMotor) {
            rechtsMotor.servo.update(DUTYCYCLE - snelheid);
        }
    }
}
