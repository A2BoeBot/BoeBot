package applicatie;

import TI.BoeBot;
import TI.Timer;
import hardware.motor.Grijper;
import hardware.motor.Motors;
import hardware.other.*;
import hardware.sensoren.*;
import interfaces.Alarm;
import interfaces.LedHandler;
import interfaces.UltrasoonHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotMain implements UltrasoonCallback, BluetoothCallback, LijnvolgersCallback {

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
    private Timer timer = new Timer(100);
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private int basisSnelheid = 30;
    private Afstandsbediening afstandsbediening;
    private int driveModus = 0;
    private double gevoeligheid = 2;
    private int minStuur, maxStuur;
    private int kruispunt;
    private int tijd;
    private int stuur;
    private int snelheid;
    private boolean kruispuntGeteld;
    private double begingetal = 2;
    private boolean stuurInverse;
    private Lijnvolger lijnvolgerRechts,lijnvolgerMidden,lijnvolgerLinks;

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        Updatable[] updatablesToAdd = {
                this.ultrasoonHandler = new UltrasoonHandler(this),
                this.ultrasoonBoven = new Ultrasoon(10, 11),
                this.ultrasoonOnder = new Ultrasoon(8, 9),
                this.ultrasoonAchter = new Ultrasoon(4, 3),
                this.ledHandler = new LedHandler(6),
                this.motors = new Motors(12, 13, this.ledHandler),
                this.lijnvolgerRechts = new Lijnvolger(0),
                this.lijnvolgerMidden = new Lijnvolger(1),
                this.lijnvolgerLinks = new Lijnvolger(2),
                this.lijvolgers = new Lijnvolgers(this),
                this.grijper = new Grijper(7, 750, 1200),
                this.bluetooth = new Bluetooth(9600, this),
                this.alarm = new Alarm(),
                this.afstandsbediening = new Afstandsbediening(6),
                this.buzzer = new Buzzer(0, 20, 1000)
        };
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
        this.updatables.addAll(Arrays.asList(updatablesToAdd));
        this.motors.zetSnelheden(this.basisSnelheid);
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
            BoeBot.wait(20);
        }
    }

    @Override
    public void gevaarAfstand(Ultrasoon ultrasoon) {
        this.alarm.stop();
        double tempSnelheid = ((ultrasoon.getAfstand() - ultrasoonHandler.getStopAfstand()) / (ultrasoonHandler.getGevaarAfstand() - ultrasoonHandler.getStopAfstand()) * this.snelheid);
        this.motors.zetSnelheden((int) tempSnelheid);
        this.maxStuur = (int) (tempSnelheid / 1.5);
        this.minStuur = (int) (tempSnelheid / 1.5 * -1);
    }

    @Override
    public void clearAfstand(Ultrasoon ultrasoon) {
        this.alarm.stop();
        this.motors.zetSnelheden(snelheid, 10);
        this.maxStuur = (int) (snelheid / 1.5);
        this.minStuur = (int) (snelheid / 1.5 * -1);
    }

    @Override
    public void stopAfstand(Ultrasoon ultrasoon) {
        this.alarm.start();
        this.motors.stop();
        this.minStuur = 0;
        this.maxStuur = 0;
    }


    @Override
    public void lijnVolgers(boolean[] states) {
//        System.out.println(minStuur + "min");
//        System.out.println(maxStuur + "max");
//        System.out.println(Arrays.toString(states));
        if (driveModus == -1 || timer.timeout()) {
            timer.mark();
            return;
        } else if (states[2]) {
            kruispuntGeteld = false;
            if (tijd > minStuur) {
                tijd += 1;
                stuurInverse = true;
            }
        } else if (states[0]) {
            kruispuntGeteld = false;
            if (tijd < maxStuur) {
                tijd += 1;
                stuurInverse = false;
            }
        } else if (states[1]) {
            kruispuntGeteld = false;
            tijd = 0;
        } else {
            tijd = 0;
            if (!kruispuntGeteld) {
                kruispuntGeteld = true;
                kruispunt++;
            }
        }
//        System.out.println(kruispunt);
//        System.out.println(tijd);
        if (tijd > 0)
            stuur = (int) Math.round(begingetal * Math.pow(gevoeligheid, tijd));
        else
            stuur = 0;
//        System.out.println(stuur);
        if (stuur >= maxStuur) {
            stuur = maxStuur;
        }
        if (stuur <= minStuur) {
            stuur = minStuur;
        }
//        System.out.println(stuur);
        motors.draaiRelatief(stuur, stuurInverse);
    }

    @Override
    public void tekstOntvangen(String tekst) {
        if (this.driveModus == -1) {
            if (tekst.matches("-?[0-9]+,-?[0-9]+")) {
                System.out.println(tekst);
                int x = Integer.parseInt(tekst.split(",")[0]);
                int y = Integer.parseInt(tekst.split(",")[1]);
                motors.zetSnelheden(y);
                motors.draaiRelatief(x, stuurInverse);
            }
//            if (tekst.equals("1")) {
//                this.motors.zetSnelheden(basisSnelheid);
//            } else if (tekst.equals("2")) {
//                this.motors.zetSnelheden(-basisSnelheid);
//            } else if (tekst.equals("3")) {
//                this.motors.draaiLinks(basisSnelheid);
//            } else if (tekst.equals("4")) {
//                this.motors.draaiRechts(basisSnelheid);
//            } else if (tekst.equals("5")) {
//                this.alarm.stop();
//                this.motors.stop();
//            } else if (tekst.equals("6")) {
//                System.out.println(this.grijper.getDutyCycle());
//                if (this.grijper.getDutyCycle() == this.grijper.getOpenDuty()) {
//                    this.grijper.dicht();
//                } else if (this.grijper.getDutyCycle() == this.grijper.getDichtDuty()) {
//                    this.grijper.open();
//                }
//            } else if (tekst.equals("7")) {
//                this.alarm.start();
//            }
        }
    }


}
