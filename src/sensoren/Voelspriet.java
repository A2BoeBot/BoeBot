package sensoren;

import TI.BoeBot;
import TI.PinMode;
import TI.Timer;
import robot.Updatable;

public class Voelspriet implements Updatable {
    private boolean vorigeToestand;
    private VoelsprietCallback callback;
    private Timer timer;
    private int pinLinks, pinRechts;

    public Voelspriet(int pinLinks, int pinRechts, VoelsprietCallback callback) {
        this.pinLinks = pinLinks;
        this.pinRechts = pinRechts;
        BoeBot.setMode(pinLinks, PinMode.Input);
        BoeBot.setMode(pinRechts, PinMode.Input);
        this.vorigeToestand = true;
        this.callback = callback;
        this.timer = new Timer(100);
    }

    @Override
    public void update() {
        if (!this.timer.timeout())
            return;
        if (!BoeBot.digitalRead(this.pinLinks) || !BoeBot.digitalRead(this.pinRechts)) {
            this.callback.stop();
            this.vorigeToestand = false;
        }
        if (BoeBot.digitalRead(pinLinks) && BoeBot.digitalRead(this.pinRechts) && !this.vorigeToestand) {
            this.vorigeToestand = true;
            this.callback.herstartNaNoodRem();
        }
        this.timer.mark();
    }
}
