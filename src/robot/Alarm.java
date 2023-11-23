package robot;

import TI.BoeBot;
import TI.PinMode;

public class Alarm {
    public static void freqOut(int pin, float frequency, int time){
        BoeBot.setMode(1, PinMode.Output);
        BoeBot.freqOut(pin, frequency, time);
    }
}
