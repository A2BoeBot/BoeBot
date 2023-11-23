package motor;

import TI.Servo;
import TI.Timer;
import robot.Updatable;

public class Motor implements Updatable {
    private int snelheid, doelsnelheid, snelheidBijNoodRem;
    public Servo servo;
    private Timer timer;
    private MotorCallback callback;
    private boolean noodRemAan;

    public Motor(int pin, MotorCallback callback) {
        this.snelheid = 0;
        this.doelsnelheid = 0;
        this.snelheidBijNoodRem = 0;
        this.servo = new Servo(pin);
        this.timer = new Timer(100);
        this.callback = callback;
        this.noodRemAan = false;
    }

    public void zetSnelheid(int doelsnelheid) {
        this.doelsnelheid = doelsnelheid;
    }

    public void zetSnelheid(int doelsnelheid, int tijd) {
        this.doelsnelheid = doelsnelheid;
        this.timer.setInterval(tijd);
    }

    public void stop() {
        if (!this.noodRemAan) {
            this.snelheidBijNoodRem = doelsnelheid;
            this.noodRemAan = true;
        }
        this.doelsnelheid = 0;
        this.snelheid = 0;
    }

    public void herstart() {
        this.noodRemAan = false;
        this.zetSnelheid(this.snelheidBijNoodRem);
    }
    @Override
    public void update() {
        if (!this.timer.timeout())
            return;
        if (this.snelheid == this.doelsnelheid)
            callback.updateMotor(this, snelheid);
        int snelheidVerschil = this.doelsnelheid - this.snelheid;
        if (snelheidVerschil > 0)
            snelheidVerschil = -1;
        else if (snelheidVerschil < 0)
            snelheidVerschil = 1;
        this.snelheid += snelheidVerschil;
        callback.updateMotor(this, snelheid);
        this.timer.mark();
    }
}