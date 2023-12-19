package hardware.other;

import TI.BoeBot;
import TI.PinMode;
import applicatie.Updatable;

public class Afstandsbediening implements Updatable {
    private static final int KNOP1 = 0b0000000;
    private int pulseLen;
    private int pin;
    private int pulses[] = new int[12];

    public Afstandsbediening(int pin) {
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
    }


    public void update() {
        this.pulseLen = BoeBot.pulseIn(pin, false, 6000);
        if (this.pulseLen > 2000) {
            for (int i = 0; i < 12; i++) {
                pulses[i] = BoeBot.pulseIn(this.pin, false, 20000);
            }
            int code = 0;
            for (int i = 0; i < 12; i++) {
                if (pulses[i] > 1000) {
                    code |= 1 << i;
                }
            }
            int buttonCode = code & 0b1111111;
            int deviceCode = (code >> 7) & 0b11111;
            System.out.println(buttonCode);
            System.out.println(Integer.toBinaryString(buttonCode));
            if (buttonCode == KNOP1){

            }
        }
    }
}

//    public static void main(String[] args) {
//        BoeBot.setMode(6, PinMode.Input);
//        System.out.println("Listening....");
//        while (true) {
//        int pulseLen = BoeBot.pulseIn(6, false, 6000);
//        if (pulseLen > 2000) {
//        int lengths[] = new int[12];
//        for (int i = 0; i < 12; i++) {
//        lengths[i] = BoeBot.pulseIn(6, false,20000);
//        if (lengths[i] > 1000){
//        lengths[i] = 1;
//        } else if (lengths[i] <= 1000){
//        lengths[i] = 0;
//        }
//        }
//        String temp = Arrays.toString(lengths);
//        System.out.println(temp);
//        System.out.println("");
//        }
//        BoeBot.wait(10);
//        }
//        }
