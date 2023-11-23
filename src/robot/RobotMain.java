package robot;

import TI.BoeBot;
import TI.PinMode;
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
    private LED led = new LED();
    private ArrayList<Updatable> updatables = new ArrayList<>();

    public static void main(String[] args) {
//        BoeBot.setMode(0, PinMode.Output);
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        updatables.add(linksMotor = new Motor(12, this));
        updatables.add(rechtsMotor = new Motor(13, this));
        updatables.add(voelspriet = new Voelspriet(5, 6, this));
        updatables.add(ultrasoon = new Ultrasoon(14, 15, this));
        motors = new Motor[2];
        motors[0] = linksMotor;
        motors[1] = rechtsMotor;
        led = new LED();
        for (Motor motor : motors) {
            motor.zetSnelheid(100, 100);
        }
    }

    private void run() {
        led.rgbALL(0, 0, 0);
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
            BoeBot.wait(10);
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
    public void stop() {
        led.ledStop();
        for (Motor motor : motors) {
            motor.stop();
        }
        Alarm.freqOut(1, 180000, 1000);
    }



    @Override
    public void herstartNaNoodRem() {
        BoeBot.setMode(1, PinMode.Output);
        BoeBot.freqOut(1, 1, 1);
        led.rgbALL(0, 0, 0);
//        BoeBot.digitalWrite(0, false);
        for (Motor motor : motors) {
            motor.herstart();
        }
    }


    @Override
    public void afstand(double afstand) {

    }
}
