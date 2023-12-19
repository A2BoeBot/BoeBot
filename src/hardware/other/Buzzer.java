package hardware.other;

import TI.BoeBot;
import TI.PinMode;
import applicatie.Updatable;

public class Buzzer implements Updatable {
    private int buzzerPin;
    private float frequency;
    private int buzzerTijd;
    private boolean buzzerState;

    public Buzzer(int buzzerPin, float frequency, int buzzerTijd) {
        this.buzzerPin = buzzerPin;
        BoeBot.setMode(this.buzzerPin, PinMode.Output);
        this.frequency = frequency;
        this.buzzerTijd = buzzerTijd;
    }

    public void state(boolean buzzerState){
        this.buzzerState = buzzerState;
    }

    @Override
    public void update() {
        if (this.frequency == 0) {
            BoeBot.digitalWrite(this.buzzerPin, this.buzzerState);
        } else {
            if (this.buzzerState) {
                BoeBot.freqOut(this.buzzerPin, this.frequency, this.buzzerTijd);
            } else {
                BoeBot.digitalWrite(this.buzzerPin, false);
            }
        }
    }
}
