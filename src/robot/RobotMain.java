package robot;

import TI.BoeBot;
import TI.Timer;
import motor.Grijper;
import motor.GrijperCallback;
import motor.Motor;
import motor.MotorCallback;
import sensoren.*;

import java.util.ArrayList;

public class RobotMain implements MotorCallback, VoelsprietCallback, UltrasoonCallback, GrijperCallback, BluetoothCallback {

    private Grijper grijper;
    private Motor linksMotor, rechtsMotor;
    private Motor[] motors;
    private Voelspriet voelspriet;
    private LED led;
    private Alarm alarm;
    private Ultrasoon ultrasoonBoven;
    private Ultrasoon ultrasoonOnder;
    private Bluetooth bluetooth;
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
        updatables.add(grijper = new Grijper(7, 750, 1200, this));
        updatables.add(bluetooth = new Bluetooth(9600, this));
        updatables.add(led = new LED(6));
        led.alles(0, 100, 0);
        updatables.add(alarm = new Alarm());
        alarm.setLed(led);
        alarm.setBuzzer(0, 20, 1000, 1000);
        alarm.setKnipper(1000, 255, 0, 0);
        motors = new Motor[]{linksMotor, rechtsMotor};
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

    @Override
    public void tekstOntvangen(String tekst) {
        System.out.println(tekst);
        if (driveModus == -1) {
            if (tekst.equals("1")) {
                for (Motor motor : motors) {
                    motor.zetSnelheid(basisSnelheid);
                }
            } else if (tekst.equals("2")) {
                for (Motor motor : motors) {
                    motor.zetSnelheid(-basisSnelheid);
                }
            } else if (tekst.equals("3")) {
                linksMotor.zetSnelheid(basisSnelheid);
                rechtsMotor.zetSnelheid(-basisSnelheid);
            } else if (tekst.equals("4")) {
                linksMotor.zetSnelheid(-basisSnelheid);
                rechtsMotor.zetSnelheid(basisSnelheid);
            } else if (tekst.equals("5")) {;
                alarm.stop();
                for (Motor motor : motors) {
                    motor.stop();
                }
            }else if (tekst.equals("6")) {
                System.out.println(grijper.getDutyCycle());
               if( grijper.getDutyCycle() == grijper.getOpenDuty()){
                   grijper.dicht();
               } else if (grijper.getDutyCycle() == grijper.getDichtDuty()) {
                   grijper.open();
               }
            } else if (tekst.equals("7")) {
                alarm.start();
            }
        }
    }
}
