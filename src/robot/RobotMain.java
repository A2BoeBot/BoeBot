package robot;

import TI.BoeBot;
import TI.Timer;
import motor.Grijper;
import motor.GrijperCallback;
import motor.Motor;
import motor.MotorCallback;
import sensoren.Voelspriet;
import sensoren.VoelsprietCallback;
import sensoren.Ultrasoon;
import sensoren.UltrasoonCallback;

import java.util.ArrayList;

public class RobotMain implements MotorCallback, VoelsprietCallback, UltrasoonCallback, GrijperCallback {

    private Grijper grijper;
    private Motor linksMotor, rechtsMotor;
    private Motor[] motors;
    private Voelspriet voelspriet;
    private LED led;
    private Alarm alarm;
    private Ultrasoon ultrasoonBoven;
    private Ultrasoon ultrasoonOnder;
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private int basisSnelheid = 50;
    private int driveModus = 0;

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        updatables.add(linksMotor = new Motor(12, this));
        updatables.add(rechtsMotor = new Motor(13, this));
        updatables.add(voelspriet = new Voelspriet(5, 6, this));
        updatables.add(ultrasoonBoven = new Ultrasoon(10, 11, this));
        updatables.add(ultrasoonOnder = new Ultrasoon(8, 9, this));
        updatables.add(grijper = new Grijper(7, 500, 900, this));
        updatables.add(led = new LED(6));
        led.alles(0, 255, 0);
        updatables.add(alarm = new Alarm());
        alarm.setLed(led);
        alarm.setBuzzer(0, 20, 1000, 1000);
        alarm.setKnipper(1000, 255, 0, 0);
        motors = new Motor[]{linksMotor, rechtsMotor};
        for (Motor motor : motors) {
            motor.zetSnelheid(basisSnelheid, 100);
        }
    }

    private void run() {
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
            BoeBot.wait(10);
        }
    }


    // TODO: 25/11/2023 led geeft richting aan
    @Override
    public void updateMotor(Motor motor, int snelheid) {
        final int DUTYCYCLE = 1500;
        if (motor == linksMotor) {
            linksMotor.servo.update(DUTYCYCLE - snelheid);
        } else if (motor == rechtsMotor) {
            rechtsMotor.servo.update(DUTYCYCLE + snelheid);
        }
    }

    @Override
    public void stop() {
        alarm.start();
        for (Motor motor : motors) {
            motor.stop();
        }
    }


    @Override
    public void herstartNaNoodRem() {
        alarm.stop();
        for (Motor motor : motors) {
            motor.herstart();
        }
    }

    // TODO: 25/11/2023 grijper pakt object op in modus 1
    @Override
    public void afstand(double afstand, Ultrasoon ultrasoon) {
        if (ultrasoon == this.ultrasoonBoven && driveModus == 0) {
//            System.out.println(afstand + "boven");
            if (afstand >= 1) {
                alarm.stop();
                System.out.println("clear");
                for (Motor motor : motors) {
                    motor.zetSnelheid(basisSnelheid, 10);
                }
            } else if (afstand <= 0.25) {
                for (Motor motor : motors) {
                    motor.zetSnelheid(0);
                    stop();
                }
            } else {
                alarm.stop();
                System.out.println("clear2");
                for (Motor motor : motors) {
                    motor.zetSnelheid((int) (afstand * basisSnelheid), 10);
                }
            }
        } else if (ultrasoon == this.ultrasoonOnder && driveModus == 1) {
            if (afstand <= 0.055) {
                grijper.dicht();
                System.out.println("dicht");
            } else if (afstand < 0.20) {
                System.out.println("sne");
                for (Motor motor : motors) {
                    motor.zetSnelheid((int) (afstand * basisSnelheid) + 10);
                }
            } else {
                System.out.println(afstand + "onder");
            }
        }
    }
}
