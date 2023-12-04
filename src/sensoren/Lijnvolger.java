package sensoren;

import TI.BoeBot;
import motor.Motors;
import robot.MiniPID;
import robot.Updatable;
import sensoren.Bluetooth;

import java.util.Arrays;

public class Lijnvolger implements Updatable {
    private int rechtsPin, middenPin, linksPin;
    private boolean[] states = new boolean[3];
    private int[] inputs = new int[3];
    private LijnvolgerCallback callback;

    public Lijnvolger(int rechtsPin, int middenPin, int linksPin, LijnvolgerCallback callback) {
        this.rechtsPin = rechtsPin;
        this.middenPin = middenPin;
        this.linksPin = linksPin;
        this.callback = callback;
    }

    @Override
    public void update() {
        inputs[0] = BoeBot.analogRead(rechtsPin);
        inputs[1] = BoeBot.analogRead(middenPin);
        inputs[2] = BoeBot.analogRead(linksPin);
        int baseline = max(inputs);
        boolean gelijk = baseline-gemiddeld(inputs) < 150;
        for (int i = 0; i < inputs.length; i++) {
            states[i] = !(inputs[i] < baseline) && !gelijk;
        }
        callback.getLijn(states);
    }

    public static int max(int[] ints) {
        int max = ints[0];
        for (int i : ints) {
            if (max < i) {
                max = i;
            }
        }
        return max;
    }
    public static int gemiddeld(int[] ints) {
        int totaal = 0;
        int aantal = 0;
        for (int i : ints) {
            totaal += i;
            aantal ++;
        }
        return totaal/aantal;
    }
}
