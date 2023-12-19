package hardware.motor;

import TI.Servo;
import TI.Timer;
import applicatie.Updatable;

public class Motor implements Updatable {
    private static final int DUTYCYCLE = 1500;
    private int snelheid, doelsnelheid;
    private Servo servo;
    private Timer timer;
    private boolean inverseRichting;

    public Motor(int pin, boolean inverseRichting) {
        this.snelheid = 0;
        this.doelsnelheid = 0;
        this.servo = new Servo(pin);
        this.timer = new Timer(100);
        this.inverseRichting = inverseRichting;
    }

    public void zetSnelheid(int doelsnelheid) {
        this.snelheid = doelsnelheid;
        this.doelsnelheid = doelsnelheid;
        this.timer.setInterval(0);
    }

    public int getSnelheid() {
        return snelheid;
    }

    public void zetSnelheid(int doelsnelheid, int tijd) {
        this.doelsnelheid = doelsnelheid;
        this.timer.setInterval(tijd);
    }

    public void stop() {
        this.snelheid = 0;
        this.doelsnelheid = 0;
    }

    @Override
    public void update() {
        if (!this.timer.timeout())
            return;
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