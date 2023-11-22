package sensoren;

import TI.BoeBot;
import TI.PinMode;
import robot.Updatable;

public class Voelspriet implements Updatable {
    private boolean vorigeToestand;

    public Voelspriet() {
        this.vorigeToestand = true;
    }

    @Override
    public void update() {
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
