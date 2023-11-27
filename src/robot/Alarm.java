package robot;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;

public class Alarm implements Updatable {
    private Timer timer = new Timer(0);
    private Timer buzzerTimer = new Timer(0);

    private int rood;
    private int groen;
    private int blauw;
    private LED led;
    private int buzzerPin;
    private boolean ledState = true;
    private boolean kinpper = false;
    private boolean alarm = false;
    private float frequency;
    private int buzzerTijd;
    private boolean buzzerState = true;

    public void setLed(LED led) {
        this.led = led;
    }

    public void setKnipper(int tijd, int r, int g, int b) {
        this.kinpper = true;
        this.timer.setInterval(tijd);
        this.rood = r;
        this.groen = g;
        this.blauw = b;
        this.led = led;
    }

    public void setKnipper(boolean knipper) {
        this.kinpper = knipper;
    }

    public void setBuzzer(int buzzerPin, float frequency, int pauze, int tijd) {
        this.buzzerPin = buzzerPin;
        BoeBot.setMode(buzzerPin, PinMode.Output);
        this.frequency = frequency;
        this.buzzerTimer.setInterval(pauze);
        this.buzzerTijd = tijd;
    }


    public void start() {
        this.alarm = true;
    }

    public void stop() {
        BoeBot.digitalWrite(this.buzzerPin, false);
        this.alarm = false;
        this.led.alles(0,100,0);
    }

    @Override
    public void update() {
        if (this.alarm) {
            if (buzzerTimer.timeout()) {
                if (this.frequency == 0) {
                    BoeBot.digitalWrite(this.buzzerPin, this.buzzerState);
                } else {
                    if (buzzerState) {
                        BoeBot.freqOut(this.buzzerPin, this.frequency, this.buzzerTijd);
                    } else {
                        BoeBot.digitalWrite(this.buzzerPin, false);
                    }
                    this.buzzerState = !this.buzzerState;
                    buzzerTimer.mark();
                }
            }
            if (this.timer.timeout() && this.kinpper) {
                if (this.ledState) {
                    this.led.alles(this.rood, this.groen, this.blauw);
                } else {
                    this.led.uit();
                }
                this.ledState = !this.ledState;
                this.timer.mark();
            }
        }
    }
}
