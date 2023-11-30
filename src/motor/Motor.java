package motor;

import TI.Servo;
import TI.Timer;
import robot.Updatable;

public class Motor implements Updatable {
    private static final int DUTYCYCLE = 1500;
    private int snelheid, doelsnelheid, snelheidBijNoodRem;
    private Servo servo;
    private Timer timer;
    private boolean noodRemAan, inverseRichting;

    public Motor(int pin, boolean inverseRichting) {
        this.snelheid = 0;
        this.doelsnelheid = 0;
        this.snelheidBijNoodRem = 0;
        this.servo = new Servo(pin);
        this.timer = new Timer(100);
        this.noodRemAan = false;
        this.inverseRichting = inverseRichting;
    }

    public void zetSnelheid(int doelsnelheid) {
        this.snelheid = doelsnelheid;
        this.doelsnelheid = doelsnelheid;
        this.timer.setInterval(0);
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
        if (this.snelheid == this.doelsnelheid) {
            if (this.inverseRichting)
                this.servo.update(DUTYCYCLE - snelheid);
            else
                this.servo.update(DUTYCYCLE + snelheid);
            return;
        }
        int snelheidVerschil = this.doelsnelheid - this.snelheid;
        if (snelheidVerschil > 0)
            snelheidVerschil = -1;
        else if (snelheidVerschil < 0)
            snelheidVerschil = 1;
        this.snelheid -= snelheidVerschil;
        if (this.inverseRichting)
            this.servo.update(DUTYCYCLE - snelheid);
        else
            this.servo.update(DUTYCYCLE + snelheid);
        this.timer.mark();
    }
}