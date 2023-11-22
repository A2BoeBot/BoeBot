package motor;

import TI.Servo;
import TI.Timer;
import robot.Updatable;

public class Motor implements Updatable {
    private int snelheid, doelsnelheid;
    public Servo servo;
    private Timer timer;
    private MotorCallback callback;

    public Motor(int pin, MotorCallback callback) {
        this.servo = new Servo(pin);
        this.timer = new Timer(100);
        this.callback = callback;
    }

    public void zetSnelheid(int doelsnelheid, int tijd) {
        this.doelsnelheid = doelsnelheid;
        this.timer.setInterval(tijd);
    }

    @Override
    public void update() {
        if (!this.timer.timeout())
            return;
        if (this.snelheid == this.doelsnelheid)
            return;
        int snelheidVerschil = this.doelsnelheid - this.snelheid;
        if (snelheidVerschil > 15)
            snelheidVerschil = 15;
        else if (snelheidVerschil < -15)
            snelheidVerschil = -15;
        this.snelheid += snelheidVerschil;
        callback.updateMotor(this, snelheid);
    }
}