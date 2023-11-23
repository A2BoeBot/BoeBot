package sensoren;

import TI.BoeBot;
import TI.PinMode;
import robot.Updatable;

public class Ultrasoon implements Updatable {

    private int echoPin;
    private int triggerPin;
    private int time;
    private UltrasoonCallback callback;
    private int timeout = 20000;
    private double afstand;

    public Ultrasoon(int echoPin, int triggerPin, UltrasoonCallback callback) {
        this.echoPin = echoPin;
        this.triggerPin = triggerPin;
        this.callback = callback;
        BoeBot.setMode(this.triggerPin, PinMode.Output);
        BoeBot.setMode(this.echoPin, PinMode.Input);
        BoeBot.digitalWrite(triggerPin, false);
    }

    public double getAfstand(){
        return this.afstand;
    }


    @Override
    public void update() {
//        BoeBot.digitalWrite(this.triggerPin, false);
        BoeBot.uwait(200);
        BoeBot.digitalWrite(this.triggerPin, true); // trigger pulse
        BoeBot.uwait(10);
        BoeBot.digitalWrite(this.triggerPin, false);
        int pulseIn = BoeBot.pulseIn(this.echoPin, true, this.timeout);
        if (pulseIn > 0) {
            double afstand = (pulseIn/1000000.0)*343;
            this.afstand = afstand;
            this.callback.afstand(this.afstand, this);
        }
    }
}
