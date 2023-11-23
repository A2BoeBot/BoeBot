package robot;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import motor.Motor;
import motor.MotorCallback;
import sensoren.Voelspriet;
import sensoren.VoelsprietCallback;
import sensoren.Ultrasoon;
import sensoren.UltrasoonCallback;
import sensoren.Voelspriet;
import sensoren.VoelsprietCallback;

import java.util.ArrayList;

public class RobotMain implements MotorCallback, VoelsprietCallback, UltrasoonCallback {
    private Motor linksMotor, rechtsMotor;
    private Motor[] motors;
    private Voelspriet voelspriet;
    private Ultrasoon ultrasoon;
    Timer timer = new Timer(1000);
    Boolean state = false;
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
        for (Motor motor : motors) {
            motor.zetSnelheid(100, 100);
        }
    }

    private void run() {
        RGB.rgbALL(0, 0, 0);
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
            BoeBot.wait(10);
        }
    }

    public static void freqOut(int pin, float frequency, int time){
        BoeBot.freqOut(pin, frequency, time);
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
        BoeBot.wait(1);
        if (timer.timeout()) {
            this.state = !this.state;
            if (state) {
                RGB.rgbALL(255, 0, 0);
            } else {
                RGB.rgbALL(0, 0, 0);
            }
            timer.mark();
//            BoeBot.digitalWrite(0, this.state);
        }
        for (Motor motor : motors) {
            motor.stop();
        }
    }


    @Override
    public void herstartNaNoodRem() {
        RGB.rgbALL(0, 0, 0);
//        BoeBot.digitalWrite(0, false);
        for (Motor motor : motors) {
            motor.herstart();
        }
    }


    @Override
    public void afstand(double afstand) {

    }
}
