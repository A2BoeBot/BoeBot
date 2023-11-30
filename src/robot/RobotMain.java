package robot;

import TI.BoeBot;
import motor.Grijper;
import motor.GrijperCallback;
import motor.Motors;
import sensoren.*;

import java.util.ArrayList;

public class RobotMain implements VoelsprietCallback, UltrasoonCallback, GrijperCallback, BluetoothCallback {

    private Grijper grijper;
    private Motors motors;
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
        Updatable[] updatablesToAdd = {
                this.motors = new Motors(12, 13),
                this.voelspriet = new Voelspriet(5, 6, this),
                this.ultrasoonBoven = new Ultrasoon(10, 11, this),
                this.ultrasoonOnder = new Ultrasoon(8, 9, this),
                this.grijper = new Grijper(7, 750, 1200, this),
                this.bluetooth = new Bluetooth(9600, this),
                this.led = new LED(6),
                this.alarm = new Alarm()
        };
        for (Updatable updatable : updatablesToAdd) {
            this.updatables.add(updatable);
        }
        this.led.alles(0, 100, 0);
        this.alarm.setLed(led);
        this.alarm.setBuzzer(0, 20, 1000, 1000);
        this.alarm.setKnipper(1000, 255, 0, 0);
    }

    private void run() {
        for (; ; ) {
            for (Updatable updatable : this.updatables) {
                updatable.update();
            }
            BoeBot.wait(10);
        }
    }

    @Override
    public void stop() {
        this.alarm.start();
        this.motors.stop();
    }


    @Override
    public void herstartNaNoodRem() {
        this.alarm.stop();
        this.motors.herstart();
    }

    // TODO: 25/11/2023 grijper pakt object op in modus 1
    @Override
    public void afstand(double afstand, Ultrasoon ultrasoon) {
        if (ultrasoon == this.ultrasoonBoven && driveModus == 0) {
//            System.out.println(afstand + "boven");
            if (afstand >= 1) {
                this.alarm.stop();
                this.motors.zetSnelheden(basisSnelheid, 10);
            } else if (afstand <= 0.25) {
                this.motors.zetSnelheden(0);
                this.motors.stop();
            } else {
                this.alarm.stop();
                this.motors.zetSnelheden((int) (afstand * basisSnelheid), 10);
            }
        } else if (ultrasoon == this.ultrasoonOnder && driveModus == 1) {
            if (afstand <= 0.055) {
                this.grijper.dicht();
                System.out.println("dicht");
            } else if (afstand < 0.20) {
                System.out.println("sne");
                this.motors.zetSnelheden((int) (afstand * basisSnelheid) + 10);
                this.motors.zetSnelheden((int) (afstand * basisSnelheid) + 10);
            } else {
                System.out.println(afstand + "onder");
            }
        }
    }

    @Override
    public void tekstOntvangen(String tekst) {
        System.out.println(tekst);
        if (this.driveModus == -1) {
            if (tekst.equals("1")) {
                this.motors.zetSnelheden(basisSnelheid);
            } else if (tekst.equals("2")) {
                this.motors.zetSnelheden(-basisSnelheid);
            } else if (tekst.equals("3")) {
                this.motors.draaiLinks(basisSnelheid);
            } else if (tekst.equals("4")) {
                this.motors.draaiRechts(basisSnelheid);
            } else if (tekst.equals("5")) {
                ;
                this.alarm.stop();
                this.motors.stop();
            } else if (tekst.equals("6")) {
                System.out.println(this.grijper.getDutyCycle());
                if (this.grijper.getDutyCycle() == this.grijper.getOpenDuty()) {
                    this.grijper.dicht();
                } else if (this.grijper.getDutyCycle() == this.grijper.getDichtDuty()) {
                    this.grijper.open();
                }
            } else if (tekst.equals("7")) {
                this.alarm.start();
            }
        }
    }
}
