package hardware.sensoren;

import TI.BoeBot;
import TI.PinMode;
import applicatie.Updatable;

public class Lijnvolger implements Updatable {
    private int pin;
    private int input;

    public Lijnvolger(int pin) {
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
    }

    @Override
    public void update() {
        this.input = BoeBot.analogRead(this.pin);
    }

    public int getInput() {
        return this.input;
    }
}
