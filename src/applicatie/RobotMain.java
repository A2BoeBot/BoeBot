package applicatie;

import TI.BoeBot;
import TI.Timer;
import hardware.motor.Grijper;
import hardware.motor.Motors;
import hardware.other.*;
import hardware.sensoren.*;
import interfaces.*;
import hardware.other.AfstandsbedieningCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotMain implements UltrasoonCallback, BluetoothCallback, LijnvolgersCallback, AfstandsbedieningCallback {

    private Grijper grijper;
    private Motors motors;
    private LedHandler ledHandler;
    private Lijnvolgers lijvolgers;
    private Ultrasoon ultrasoonBoven;
    private Ultrasoon ultrasoonAchter;
    private Ultrasoon ultrasoonOnder;
    private UltrasoonHandler ultrasoonHandler;

    private Alarm alarm;
    private Buzzer buzzer;
    private Bluetooth bluetooth;
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private int basisSnelheid = 30;
    private Afstandsbediening afstandsbediening;
    private String driveModus = "idle";
    private double gevoeligheid = 2;
    private int minStuur, maxStuur;
    private int tijd, stuur, snelheid;
    private double begingetal = 2;
    private Lijnvolger lijnvolgerRechts, lijnvolgerMidden, lijnvolgerLinks;
    private Route route = new Route();

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        this.route.voegActiesToe("wwaw");

        Updatable[] updatablesToAdd = {
                this.ultrasoonHandler = new UltrasoonHandler(this),
                this.ultrasoonBoven = new Ultrasoon(7, 8),
                this.ultrasoonOnder = new Ultrasoon(6, 5),
                this.ultrasoonAchter = new Ultrasoon(9, 10),
                this.ledHandler = new LedHandler(6),
                this.motors = new Motors(12, 13, this.ledHandler, new Timer(1550)),
                this.lijnvolgerRechts = new Lijnvolger(1),
                this.lijnvolgerMidden = new Lijnvolger(2),
                this.lijnvolgerLinks = new Lijnvolger(3),
                this.lijvolgers = new Lijnvolgers(this),
                this.grijper = new Grijper(0, 750, 1200),
                this.bluetooth = new Bluetooth(9600, this),
                this.alarm = new Alarm(),
                this.afstandsbediening = new Afstandsbediening(11, this),
                this.buzzer = new Buzzer(2, 20, 1000)
        };
        this.updatables.addAll(Arrays.asList(updatablesToAdd));
        this.lijvolgers.voegLijnvolgerToe(lijnvolgerRechts);
        this.lijvolgers.voegLijnvolgerToe(lijnvolgerMidden);
        this.lijvolgers.voegLijnvolgerToe(lijnvolgerLinks);
        this.ultrasoonHandler.voegUltrasoonToe(this.ultrasoonBoven, 0);
        this.ultrasoonHandler.voegUltrasoonToe(this.ultrasoonOnder, 1);
        this.ultrasoonHandler.voegUltrasoonToe(this.ultrasoonAchter, 2);
        this.ultrasoonHandler.setGevaarAfstand(0.5);
        this.ultrasoonHandler.setStopAfstand(0.25);
        this.ultrasoonHandler.setMotors(motors);
        this.ultrasoonHandler.setGrijper(grijper);
        this.alarm.setLedHandler(this.ledHandler);
        this.alarm.setBuzzer(this.buzzer, 1000);
        this.alarm.setKnipper(1000, 255, 0, 0);
        this.snelheid = this.basisSnelheid;
    }

    private void run() {
        for (; ; ) {
            for (Updatable updatable : this.updatables) {
                updatable.update();
            }
            if (driveModus.equals("idle")){
                motors.stop();
            }
//            System.out.println(driveModus);
            BoeBot.wait(20);
        }
    }

    @Override
    public void gevaarAfstand(Ultrasoon ultrasoon) {
        if (!driveModus.equals("idle")) {
            this.alarm.stop();
            double tempSnelheid = ((ultrasoon.getAfstand() - ultrasoonHandler.getStopAfstand()) / (ultrasoonHandler.getGevaarAfstand() - ultrasoonHandler.getStopAfstand()) * this.snelheid);
            this.motors.zetSnelheden((int) tempSnelheid);
            this.maxStuur = (int) (tempSnelheid / 1.5);
            this.minStuur = (int) (tempSnelheid / 1.5 * -1);
        }
    }

    @Override
    public void clearAfstand(Ultrasoon ultrasoon) {
        if (!driveModus.equals("idle")) {
            this.alarm.stop();
            this.motors.zetSnelheden(snelheid, 10);
            this.maxStuur = (int) (snelheid / 1.5);
            this.minStuur = (int) (snelheid / 1.5 * -1);
        }
    }

    @Override
    public void stopAfstand(Ultrasoon ultrasoon) {
        if (!driveModus.equals("idle")) {
            this.alarm.start();
            this.motors.stop();
            this.minStuur = 0;
            this.maxStuur = 0;
        }
    }


    @Override
    public void lijnVolgers(boolean[] states) {
        boolean stuurInverse = false;
        if (driveModus.equals("route")) {
            System.out.println(Arrays.toString(states));
            if (states[2]) {
                if (tijd > minStuur) {
                    tijd += 1;
                    stuurInverse = true;
                }
            } else if (states[0]) {
                if (tijd < maxStuur) {
                    tijd += 1;
                    stuurInverse = false;
                }
            } else if (states[1]) {
                tijd = 0;
            } else {
                // route volgende actie
                this.motors.stop();
                this.route.actie(motors,grijper,ledHandler,basisSnelheid);
                tijd = 0;
            }
            if (tijd > 0)
                stuur = (int) Math.round(begingetal * Math.pow(gevoeligheid, tijd));
            else
                stuur = 0;
            if (stuur >= maxStuur) {
                stuur = maxStuur;
            }
            if (stuur <= minStuur) {
                stuur = minStuur;
            }
            motors.draaiRelatief(stuur, stuurInverse);
        }
    }

    @Override
    public void tekstOntvangen(String tekst) {

    }

    @Override
    public void knop_1_Ingedrukt() {
        driveModus = "afstandsbediening";
        this.grijper.open();
    }

    @Override
    public void knop_2_Ingedrukt() {
        driveModus = "afstandsbediening";
        this.grijper.dicht();
    }

    @Override
    public void knop_3_Ingedrukt() {
        driveModus = "afstandsbediening";
        ledHandler.deur(this.motors);
    }

    @Override
    public void knop_7_Ingedrukt() {
        driveModus = "afstandsbediening";
    }

    @Override
    public void knop_8_Ingedrukt() {
        driveModus = "idle";
    }

    @Override
    public void knop_9_Ingedrukt() {
        driveModus = "route";
    }

    @Override
    public void knop_Uit_Ingedrukt() {
            driveModus = "idle";
            this.alarm.start();
            this.motors.stop();
        }

    @Override
    public void knop_Ch_BovenIngedrukt() {
            driveModus = "afstandsbediening";
            this.motors.zetSnelheden(basisSnelheid);
        }

    @Override
    public void knop_Ch_OnderIngedrukt() {
            driveModus = "afstandsbediening";
            this.motors.zetSnelheden(-basisSnelheid);
        }

    @Override
    public void knop_Vol_Links_Ingedrukt() {
            driveModus = "afstandsbediening";
            this.motors.draaiLinks(basisSnelheid);
        }

    @Override
    public void knop_Vol_Rechts_Ingedrukt() {
            driveModus = "afstandsbediening";
            this.motors.draaiRechts(basisSnelheid);
    }
}
