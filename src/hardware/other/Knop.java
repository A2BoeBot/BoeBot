package hardware.other;

import TI.BoeBot;
import TI.PinMode;
import applicatie.Updatable;
import hardware.motor.Motors;
import interfaces.Alarm;

public class Knop implements Updatable {
    private int pin;
    private Motors motors;
    private Alarm alarm;

    public Knop(int pin, Motors motors, Alarm alarm){
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
        this.motors = motors;
        this.alarm = alarm;
    }
    @Override
    public void update() {
        if (!BoeBot.digitalRead(pin)) {
            this.motors.stop();
            this.alarm.start();
        }
    }
}
