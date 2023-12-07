package robot;

import TI.BoeBot;
import motor.Grijper;
import motor.Motors;
import sensoren.*;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotMain implements UltrasoonCallback, BluetoothCallback, LijnvolgerCallback {

    private Grijper grijper;
    private Motors motors;
    private LED led;
    private Lijnvolger lijvolger;
    private Ultrasoon ultrasoonBoven;
    private Ultrasoon ultrasoonAchter;
    private Ultrasoon ultrasoonOnder;
    private Alarm alarm;
    private Bluetooth bluetooth;
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private final int basisSnelheid = 50;
    private int driveModus = 0;
    private int gevoeligheid = 4;
    private double minStuur, maxStuur;
    private int kruispunt, stuur, snelheid;
    private int draaikruispunt = 3;
    private boolean kruispuntGeteld;

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        Updatable[] updatablesToAdd = {
                this.led = new LED(6),
                this.motors = new Motors(12, 13, led),
                this.lijvolger = new Lijnvolger(0, 1, 2, this),
                this.ultrasoonBoven = new Ultrasoon(10, 11, this),
                this.ultrasoonOnder = new Ultrasoon(8, 9, this),
                this.ultrasoonAchter = new Ultrasoon(4, 3, this),
                this.grijper = new Grijper(7, 750, 1200),
                this.bluetooth = new Bluetooth(9600, this),
                this.alarm = new Alarm()
        };
        this.updatables.addAll(Arrays.asList(updatablesToAdd));
        motors.zetSnelheden(basisSnelheid);
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

    // TODO: 25/11/2023 grijper pakt object op
    @Override
    public void afstand(double afstand, Ultrasoon ultrasoon) {
        if (ultrasoon == this.ultrasoonBoven && driveModus == 0) {
//            System.out.println(afstand + "boven");
            if (afstand >= 1) {
                this.alarm.stop();
                this.motors.herstart();
                this.motors.zetSnelheden(snelheid, 10);
            } else if (afstand <= 0.25) {
                this.alarm.start();
                this.motors.stop();
            } else {
                this.alarm.stop();
                this.motors.herstart();
                this.motors.zetSnelheden((int) (afstand * snelheid), 10);
                this.maxStuur = afstand * snelheid / 1.5;
                this.minStuur = afstand * snelheid / 1.5 * -1;
            }
        } else if (ultrasoon == this.ultrasoonOnder) {
//            System.out.println(afstand + "onder");
        } else if (ultrasoon == this.ultrasoonAchter) {
//            System.out.println(afstand + "achter");
        }
    }


    @Override
    public void getLijn(boolean[] states) {
        System.out.println(Arrays.toString(states));
        if (driveModus == -1) {
            return;
        } else if (states[2]) {
            kruispuntGeteld = false;
            if (stuur > minStuur) {
                stuur -= 1;
            }
            stuur = (int) Math.pow((stuur),gevoeligheid);
        } else if (states[0]) {
            kruispuntGeteld = false;
            if (stuur < maxStuur) {
                stuur += 1;
            }
            stuur = (int) Math.pow((stuur),gevoeligheid);
        } else if (states[1]) {
            kruispuntGeteld = false;
            stuur = 0;
            if (kruispunt >= draaikruispunt) {
//                snelheid = 0;
//                return;
//                driveModus = 1;
//                motors.stop();
//                alarm.start();
            }
        } else {
            stuur = 0;
            if (!kruispuntGeteld) {
                kruispuntGeteld = true;
                kruispunt++;
            }

        }
        System.out.println(kruispunt);
//        System.out.println(stuur);


        motors.draaiRelatief(stuur);
        if (stuur == 0) {
            snelheid = basisSnelheid;
            this.maxStuur = snelheid / 1.5;
            this.minStuur = snelheid / 1.5 * -1;
        } else {
            snelheid = (int) (basisSnelheid - 10);
            this.maxStuur = basisSnelheid / 1.5;
            this.minStuur = basisSnelheid / 1.5 * -1;
        }
//
    }

    @Override
    public void tekstOntvangen(String tekst) {
        if (this.driveModus == -1) {
            System.out.print(tekst);
            if(tekst.matches("-?[0-9]+,-?[0-9]+")){
                int x = Integer.parseInt(tekst.split(",")[0]);
                int y = Integer.parseInt(tekst.split(",")[1]);
                motors.zetSnelheden(y);
                motors.draaiRelatief(x);
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
