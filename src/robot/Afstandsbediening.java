package robot;

import TI.BoeBot;
import TI.PinMode;

public class Afstandsbediening implements Updatable {
    private int pulseLen;
    private int pin;
    private int lijstVanBitjes[] = new int[12];
    private String bitBouwer = "";

    public Afstandsbediening(int pin) {
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
    }


    public void update() {
        this.pulseLen = BoeBot.pulseIn(pin, false, 6000);
        if (this.pulseLen > 2000) {
            for (int i = 0; i < 12; i++) {
                lijstVanBitjes[i] = BoeBot.pulseIn(this.pin, false, 20000);
                if (lijstVanBitjes[i] > 1000) {
                    bitBouwer += "1";
                    lijstVanBitjes[i] = 1;
                } else if (lijstVanBitjes[i] <= 1000) {
                    bitBouwer += "0";
                    lijstVanBitjes[i] = 0;
                }
            }
            if (bitBouwer.equals("000000000000")) {           //knop 1
                System.out.println("knop1");
            } else if (bitBouwer.equals("100000000000")) {   //knop 2

            } else if (bitBouwer.equals("010000000000")) {  //knop 3

            } else if (bitBouwer.equals("110000000000")) {  //knop 4

            } else if (bitBouwer.equals("001000000000")) {  //knop 5

            } else if (bitBouwer.equals("100100000000")) {  //knop 6

            } else if (bitBouwer.equals("100000000000")) {  //knop 7
//            System.out.println(temp);
                System.out.println(lijstVanBitjes);
                System.out.println("");
                System.out.println(bitBouwer);
            }
            BoeBot.wait(10);
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
