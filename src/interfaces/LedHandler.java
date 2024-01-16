package interfaces;

import TI.BoeBot;
import hardware.motor.Motors;
import hardware.other.LED;
import applicatie.Updatable;

public class LedHandler implements Updatable {
    private LED led;

    public LedHandler(int ledAantal) {
        this.led = new LED(ledAantal);
    }

    public void uit() {
        led.uit();
    }

    public void vooruit() {
        for (int i = 0; i < 6; i++) {
            if (i > 2) {
                this.led.set(i, 0, 100, 0);
            } else {
                this.led.set(i, 0, 0, 0);
            }
        }
    }

    public void achteruit() {
        for (int i = 0; i < 6; i++) {
            if (i < 3) {
                this.led.set(i, 0, 100, 0);
            } else {
                this.led.set(i, 0, 0, 0);
            }
        }
    }

    public void links() {
        for (int i = 0; i < 6; i++) {
            if (i == 2 || i == 3) {
                this.led.set(i, 255, 100, 0);
            } else {
                this.led.set(i, 0, 0, 0);
            }
        }
    }

    public void rechts() {
        for (int i = 0; i < 6; i++) {
            if (i == 0 || i == 5) {
                this.led.set(i, 255, 100, 0);
            } else {
                this.led.set(i, 0, 0, 0);
            }
        }
    }

    public void deur(boolean aanOfUit) {
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 6; i++) {
                this.led.set(i * 10, i * 10, i, i * 10);
            }
            BoeBot.rgbShow();
        }
        uit();
    }

    public void alles(boolean ledState, int rood, int groen, int blauw) {
        if (ledState) {
            this.led.alles(rood, groen, blauw);
        } else {
            this.led.uit();
        }
    }


    @Override
    public void update() {
        this.led.update();
    }
}
