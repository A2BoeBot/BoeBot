package robot;

import TI.BoeBot;
import motor.Grijper;
import motor.GrijperCallback;
import motor.Motors;
import sensoren.*;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotMain implements UltrasoonCallback, GrijperCallback, BluetoothCallback, LijnvolgerCallback {

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
    private int basisSnelheid = 20;
    private int driveModus = 0;
    private MiniPID miniPID = new MiniPID(1, 0, 50, 0.01);
    private int stuur;
    private int gevoeligheid = 4;
    private int minStuur = gevoeligheid * -10;
    private int maxStuur = gevoeligheid * 10;

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
                this.ultrasoonAchter = new Ultrasoon(3, 4, this),
                this.grijper = new Grijper(7, 750, 1200, this),
                this.bluetooth = new Bluetooth(9600, this),
                this.alarm = new Alarm()
        };
        this.updatables.addAll(Arrays.asList(updatablesToAdd));
        motors.zetSnelheden(basisSnelheid);
        miniPID.setOutputLimits(minStuur, maxStuur);
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
                this.motors.zetSnelheden(basisSnelheid, 10);
            } else if (afstand <= 0.25) {
                this.alarm.start();
                this.motors.stop();
            } else {
                this.alarm.stop();
                this.motors.herstart();
                this.motors.zetSnelheden((int) (afstand * basisSnelheid), 10);
            }
        } else if (ultrasoon == this.ultrasoonOnder) {
//            System.out.println(afstand + "onder");
        }
    }


    @Override
    public void getLijn(boolean[] states) {
        boolean[] temp = {false, false, false};
        boolean kruispunt = false;
//        if(Arrays.equals(states, temp))
//        System.out.println(Arrays.toString(states));
        if (states[2]) {
            if (stuur > minStuur) {
                stuur -= gevoeligheid;
            }
        } else if (states[0]) {
            if (stuur < maxStuur) {
                stuur += gevoeligheid;
            }
        } else if (states[1]) {
//            stuur = 0;
            if (stuur > 0) {
                stuur -= gevoeligheid / 2;
            } else if (stuur < 0) {
                stuur += gevoeligheid / 2;
            }
        } else {
            kruispunt = true;
            stuur = 0;
        }
        System.out.println(kruispunt);
        double pid = miniPID.getOutput(0, stuur);
//        if (stuur != 0);
//            System.out.println(stuur);
//        if (pid != 0);
//            System.out.println(pid);
        motors.draaiRelatief((int) pid);
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
