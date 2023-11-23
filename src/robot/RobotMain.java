package robot;

import motor.Motor;
import motor.MotorCallback;
import sensoren.Voelspriet;
import sensoren.VoelsprietCallback;
import sensoren.Ultrasoon;
import sensoren.UltrasoonCallback;

import java.util.ArrayList;

public class RobotMain implements MotorCallback, VoelsprietCallback, UltrasoonCallback {
    private Motor linksMotor, rechtsMotor;
    private Motor[] motors;
    private Voelspriet voelspriet;
    private Ultrasoon ultrasoon;
    private ArrayList<Updatable> updatables = new ArrayList<>();

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
//        updatables.add(linksMotor = new Motor(12, this));
//        updatables.add(rechtsMotor = new Motor(13, this));
//        updatables.add(voelspriet = new Voelspriet(6, 7, this));
        updatables.add(ultrasoon = new Ultrasoon(13, 14, this));
        motors = new Motor[2];
        motors[0] = linksMotor;
        motors[1] = rechtsMotor;
//        this.stelMotorenIn(1400, 1000);
    }

    private void run() {
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
//            BoeBot.wait(10);
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

    @Override
    public void noodRem() {
        System.out.println("noodrem");
    }

    @Override
    public void herstartNaNoodRem() {
        System.out.println("herstart");
    }


    @Override
    public void afstand(double afstand) {

    }
}
