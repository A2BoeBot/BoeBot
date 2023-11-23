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

    public Voelspriet(VoelsprietCallback callback, int pinLinks, int pinRechts) {
        BoeBot.setMode(pinLinks, PinMode.Output);
        BoeBot.setMode(pinRechts, PinMode.Output);
        this.pinLinks = pinLinks;
        this.pinRechts = pinRechts;
        this.vorigeToestand = true;
        this.callback = callback;
        this.timer = new Timer(100);
    }

    @Override
    public void update() {
        if (!this.timer.timeout())
            return;
        if (BoeBot.digitalRead(pinLinks) || BoeBot.digitalRead(pinRechts)) {
            callback.noodRem();
            vorigeToestand = false;
        }
        if (!BoeBot.digitalRead(pinLinks) && !BoeBot.digitalRead(pinRechts) && vorigeToestand == false) {
            vorigeToestand = true;
            callback.herstartNaNoodRem();
        }
    }
}
