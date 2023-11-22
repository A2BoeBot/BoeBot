package robot;

import TI.BoeBot;
import motor.Motor;
import motor.MotorCallback;
import sensoren.Voelspriet;
import sensoren.VoelsprietCallback;
import java.util.ArrayList;

public class RobotMain implements MotorCallback, VoelsprietCallback {
    private Motor linksMotor, rechtsMotor;
    private Motor[] motors;
    private Voelspriet voelspriet;
    private ArrayList<Updatable> updatables = new ArrayList<>();

    public static void main(String[] args) {
        RobotMain robot = new RobotMain();
        robot.init();
        robot.run();
    }

    public void init() {
        updatables.add(linksMotor = new Motor(12, this));
        updatables.add(rechtsMotor = new Motor(13, this));
        updatables.add(voelspriet = new Voelspriet(this, 5 ,6));
        motors = new Motor[2];
        motors[0] = linksMotor;
        motors[1] = rechtsMotor;
        this.stelMotorenIn(1400, 1000);
    }

    private void run() {
        for (; ; ) {
            for (Updatable updatable : updatables) {
                updatable.update();
            }
            BoeBot.wait(1);
        }
    }

    public void stelMotorenIn(int doelsnelheid, int tijd) {
        for (Motor motor : motors) {
            motor.zetSnelheid(doelsnelheid, tijd);
        }
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
    public void herstartNaNoodRem() {
        for (Motor motor : motors) {
            motor.herstart();
        }
    }

    @Override
    public void noodRem(){
        for (Motor motor : motors) {
            motor.noodRem();
        }
    }
}
