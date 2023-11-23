package motor;

import TI.Servo;
import TI.Timer;
import robot.Updatable;

public class Motor implements Updatable {
    private int snelheid, doelsnelheid, snelheidBijNoodRem;
    public Servo servo;
    private Timer timer;
    private MotorCallback callback;

    public Motor(int pin, MotorCallback callback) {
        this.snelheid = 0;
        this.doelsnelheid = 0;
        this.snelheidBijNoodRem = 0;
        this.servo = new Servo(pin);
        this.timer = new Timer(100);
        this.callback = callback;
    }

    public void zetSnelheid(int doelsnelheid) {
        this.doelsnelheid = doelsnelheid;
    }

    public void zetSnelheid(int doelsnelheid, int tijd) {
        this.doelsnelheid = doelsnelheid;
        this.timer.setInterval(tijd);
    }

    public void noodRem() {
        this.snelheidBijNoodRem = doelsnelheid;
        this.doelsnelheid = 0;
        this.snelheid = 0;
    }

    public void herstart() {
        this.zetSnelheid(this.snelheidBijNoodRem);
    }
    @Override
    public void update() {
        if (!this.timer.timeout())
            return;
        if (this.snelheid == this.doelsnelheid)
            return;
        int snelheidVerschil = this.doelsnelheid - this.snelheid;
        if (snelheidVerschil > 0)
            snelheidVerschil = 1;
        else if (snelheidVerschil < 0)
            snelheidVerschil = -1;
        this.snelheid += snelheidVerschil;
        callback.updateMotor(this, snelheid);
        this.timer.mark();
    }
}