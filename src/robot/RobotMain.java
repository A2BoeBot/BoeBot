package robot;

import TI.BoeBot;
import TI.Timer;
import motor.Grijper;
import motor.Motors;
import sensoren.*;

import java.util.ArrayList;
import java.util.Arrays;

public class RobotMain implements UltrasoonCallback, BluetoothCallback, LijnvolgerCallback, AfstandsbedieningCallback {

    private Grijper grijper;
    private Motors motors;
    private LED led;
    private Lijnvolger lijvolger;
    private Ultrasoon ultrasoonBoven;
    private Ultrasoon ultrasoonAchter;
    private Ultrasoon ultrasoonOnder;
    private Alarm alarm;
    private Bluetooth bluetooth;
    private Timer timer = new Timer(100);
    private ArrayList<Updatable> updatables = new ArrayList<>();
    private int basisSnelheid = 20;
    private Afstandsbediening afstandsbediening;
    private int driveModus = 0;
    private double gevoeligheid = 2;
    private int minStuur, maxStuur;
    private int kruispunt, tijd, stuur, snelheid;
    private boolean kruispuntGeteld;
    private double stopAfstand = 0.25;
    private double gevaarAfstand = 0.5;

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
                this.alarm = new Alarm(),
                this.afstandsbediening = new Afstandsbediening(6, this)
        };
        this.updatables.addAll(Arrays.asList(updatablesToAdd));
        motors.zetSnelheden(basisSnelheid);
        this.alarm.setLed(led);
        this.alarm.setBuzzer(0, 20, 1000, 1000);
        this.alarm.setKnipper(1000, 255, 0, 0);
        this.snelheid = this.basisSnelheid;
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
        if (driveModus == 0) {
            if (snelheid > 0 && ultrasoon == this.ultrasoonAchter) {
                return;
            }
            if ((snelheid < 0 || grijper.getDutyCycle() == grijper.getOpenDuty()) && ultrasoon == this.ultrasoonBoven) {
                return;
            }
            if ((snelheid < 0 || grijper.getDutyCycle() == grijper.getDichtDuty()) && ultrasoon == this.ultrasoonOnder) {
                return;
            }
//            System.out.println(afstand+"afstand");
            if (afstand >= gevaarAfstand) {
                this.alarm.stop();
                this.motors.herstart();
                this.motors.zetSnelheden(snelheid, 10);
                this.maxStuur = (int) (snelheid / 1.5);
                this.minStuur = (int) (snelheid / 1.5 * -1);
            } else if (afstand <= stopAfstand) {
                this.alarm.start();
                this.motors.stop();
            } else {
                this.alarm.stop();
                this.motors.herstart();
                this.motors.zetSnelheden((int) ((afstand - stopAfstand) / (gevaarAfstand - stopAfstand) * snelheid));
                this.maxStuur = (int) (afstand * snelheid / 1.5);
                this.minStuur = (int) (afstand * snelheid / 1.5 * -1);
            }
        }
    }


    @Override
    public void getLijn(boolean[] states) {
//        System.out.println(Arrays.toString(states));
        if (driveModus == -1 || timer.timeout()) {
            timer.mark();
            return;
        } else if (states[2]) {
            kruispuntGeteld = false;
            if (tijd > minStuur) {
                tijd -= 1;
            }
        } else if (states[0]) {
            kruispuntGeteld = false;
            if (tijd < maxStuur) {
                tijd += 1;
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
        System.out.println(tijd);
        if (tijd > 0)
            stuur = (int) Math.round(Math.pow(gevoeligheid, tijd));
        else
            stuur = (int) Math.round(Math.pow(gevoeligheid, tijd * -1)) * -1;
        if (stuur > maxStuur) {
            stuur = maxStuur;
        }
        if (stuur < minStuur) {
            stuur = minStuur;
        }
        System.out.println(stuur);
        motors.draaiRelatief(stuur);
//
    }

    @Override
    public void tekstOntvangen(String tekst) {
        if (this.driveModus == -1) {
            if (tekst.matches("-?[0-9]+,-?[0-9]+")) {
                System.out.println(tekst);
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


    @Override
    public void knop_1_Ingedrukt() {
        this.grijper.open();
    }

    @Override
    public void knop_2_Ingedrukt() {
        this.grijper.dicht();
    }

    @Override
    public void knop_3_Ingedrukt() {

    }

    @Override
    public void knop_4_Ingedrukt() {

    }

    @Override
    public void knop_5_Ingedrukt() {

    }

    @Override
    public void knop_6_Ingedrukt() {

    }

    @Override
    public void knop_7_Ingedrukt() {
        driveModus = 0;
    }

    @Override
    public void knop_8_Ingedrukt() {
        driveModus = -1;
    }

    @Override
    public void knop_9_Ingedrukt() {

    }

    @Override
    public void knop_0_Ingedrukt() {
    }

    @Override
    public void knop_Uit_Ingedrukt() {
        this.alarm.start();
        this.motors.stop();
    }

    @Override
    public void knop_Ch_BovenIngedrukt() {
        this.motors.zetSnelheden(basisSnelheid);
    }

    @Override
    public void knop_Ch_OnderIngedrukt() {
        this.motors.zetSnelheden(-basisSnelheid );
    }

    @Override
    public void knop_Vol_Links_Ingedrukt() {
        this.motors.draaiLinks(basisSnelheid);
    }

    @Override
    public void knop_Vol_Rechts_Ingedrukt() {
        this.motors.draaiRechts(basisSnelheid);
    }
}
