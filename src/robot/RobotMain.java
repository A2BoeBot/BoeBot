package robot;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import TI.BoeBot;
import robot.LED;
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
    private Ultrasoon ultrasoon;
    private Ultrasoon ultrasoonOnder;
    Timer timer = new Timer(1000);
    Boolean state = false;
    private LED led = new LED();
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private int basisSnelheid = 50;

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        updatables.add(linksMotor = new Motor(12, this));
        updatables.add(rechtsMotor = new Motor(13, this));
        updatables.add(voelspriet = new Voelspriet(5, 6, this));
        updatables.add(ultrasoon = new Ultrasoon(10, 11, this));
        updatables.add(ultrasoonOnder = new Ultrasoon(8, 9, this));
//        updatables.add(grijper = new Grijper(7, 500, 900, this));
        motors = new Motor[2];
        motors[0] = linksMotor;
        motors[1] = rechtsMotor;
        BoeBot.setMode(0, PinMode.Input);
        led = new LED();
        for (Motor motor : motors) {
            motor.zetSnelheid(basisSnelheid, 100);
        }
    }

    private void run() {
        led.rgbALL(0, 0, 0);
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
            BoeBot.wait(10);
            if (!BoeBot.digitalRead(0)) {
//                grijper.open();
            }
        }
    }



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
        led.ledStop();
        for (Motor motor : motors) {
            motor.stop();
        }
        Alarm.freqOut(1, 180000, 1000);
    }



    @Override
    public void herstartNaNoodRem() {
        led.rgbALL(0, 0, 0);
        BoeBot.setMode(1, PinMode.Output);
        BoeBot.freqOut(1, 1, 1);
        led.rgbALL(0, 0, 0);
//        BoeBot.digitalWrite(0, false);
        for (Motor motor : motors) {
            motor.herstart();
        }
    }


    @Override
    public void afstand(double afstand, Ultrasoon ultrasoon) {
        if (ultrasoon == this.ultrasoon) {
//            System.out.println(afstand + "boven");
            if (afstand >= 1) {
                System.out.println("clear");
                for (Motor motor : motors) {
                    motor.zetSnelheid(basisSnelheid, 10);
                }
                led.rgbALL(0, 0, 0);
            } else if (afstand <= 0.25) {
                for (Motor motor : motors) {
                    motor.zetSnelheid(0);
                    stop();
                }
            } else {
                for (Motor motor : motors) {
                    motor.zetSnelheid((int) (afstand * basisSnelheid), 10);
                    led.rgbALL(0, 0, 0);

                }
            }
        } else if (ultrasoon == this.ultrasoonOnder) {
//            if (afstand <= 0.055 && BoeBot.digitalRead(0)) {
//                grijper.dicht();
//                System.out.println("dicht");
//            } else if(afstand <0.20){
//                System.out.println("sne");
//                System.out.println(afstand + "onder");
//                for (Motor motor : motors) {
//                    motor.zetSnelheid((int) (afstand * basisSnelheid)+10);
//                }
//            } else {
//
//                System.out.println(afstand + "onder");
//            }
        }
    }
}
