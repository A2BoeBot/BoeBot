package sensoren;

import TI.BoeBot;
import TI.PinMode;
import robot.Updatable;

public class Ultrasoon implements Updatable {

    private int echoPin;
    private int triggerPin;
    private int time;
    private UltrasoonCallback callback;

    public Ultrasoon(int echoPin, int triggerPin, UltrasoonCallback callback) {
        this.echoPin = echoPin;
        this.triggerPin = triggerPin;
        this.callback = callback;
        BoeBot.setMode(this.triggerPin, PinMode.Output);
        BoeBot.setMode(this.echoPin, PinMode.Input);
    }

    // TODO: 22/11/2023 make this work and use the correct sensor

    @Override
    public void update() {
        BoeBot.digitalWrite(this.triggerPin, false);
        BoeBot.uwait(5);
        BoeBot.digitalWrite(this.triggerPin, true);
        BoeBot.uwait(5);
        BoeBot.digitalWrite(this.triggerPin, false);
        BoeBot.uwait(750);
        BoeBot.uwait(115);
        int pulseIn = BoeBot.pulseIn(this.echoPin, true, 18500);
        if(pulseIn>0) {
            System.out.println(pulseIn);
            this.callback.afstand(pulseIn);
        }
        BoeBot.uwait(200);
    }
}
