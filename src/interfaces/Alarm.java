package interfaces;

import TI.Timer;
import hardware.other.Buzzer;
import applicatie.Updatable;

public class Alarm implements Updatable {
    private Timer timer = new Timer(0);
    private Timer buzzerTimer = new Timer(0);

    private int rood;
    private int groen;
    private int blauw;
    private LedHandler ledHandler;
    private Buzzer buzzer;
    private boolean ledState = true;
    private boolean kinpper = false;
    private boolean alarm = false;
    private boolean buzzerState = true;

    public void setLedHandler(LedHandler ledHandler) {
        this.ledHandler = ledHandler;
    }

    public void setKnipper(int tijd, int r, int g, int b) {
        this.kinpper = true;
        this.timer.setInterval(tijd);
        this.rood = r;
        this.groen = g;
        this.blauw = b;
    }

    public void setKnipper(boolean knipper) {
        this.kinpper = knipper;
    }

    public void setBuzzer(Buzzer buzzer, int pauze) {
        this.buzzerTimer.setInterval(pauze);
        this.buzzer = buzzer;
    }

    public void setBuzzer(Buzzer buzzer) {
        this.buzzerTimer.setInterval(0);
        this.buzzer = buzzer;
    }


    public void start() {
        this.alarm = true;
    }

    public void stop() {
        buzzer.state(false);
        this.alarm = false;
    }

    @Override
    public void update() {
        if (this.alarm) {
            if (buzzerTimer.timeout()) {
                buzzer.state(this.buzzerState);
                this.buzzerState = !this.buzzerState;
                buzzerTimer.mark();
            }
            if (this.timer.timeout() && this.kinpper) {
                this.ledHandler.alarm(this.ledState, this.rood, this.groen, this.blauw);
                this.ledState = !this.ledState;
                this.timer.mark();
            }
        }
    }
}
