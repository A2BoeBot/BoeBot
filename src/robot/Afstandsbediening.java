package robot;

import TI.BoeBot;
import TI.PinMode;

public class Afstandsbediening implements Updatable {
    public static final int KNOP_1 = 0b10000000;
    public static final int KNOP_2 = 0b10000001;
    public static final int KNOP_3 = 0b10000010;
    public static final int KNOP_4 = 0b10000011;
    public static final int KNOP_5 = 0b10000100;
    public static final int KNOP_6 = 0b10000101;
    public static final int KNOP_7 = 0b10000110;
    public static final int KNOP_8 = 0b10000111;
    public static final int KNOP_9 = 0b10001000;
    public static final int KNOP_0 = 0b10001001;
    public static final int KNOP_UIT = 0b10010101;
    public static final int KNOP_CH_BOVEN = 0b10010000;
    public static final int KNOP_CH_ONDER = 0b10010001;
    public static final int KNOP_VOL_LINKS = 0b10010011;
    public static final int KNOP_VOL_RECHTS = 0b10010010;
    private int pulseLen;
    private int pin;
    private int lijstVanBitjes[] = new int[12];

    public Afstandsbediening(int pin) {
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
    }


    public void update() {
        this.pulseLen = BoeBot.pulseIn(pin, false, 6000);
        if (this.pulseLen > 2000) {
            for (int i = 0; i < 12; i++) {
                lijstVanBitjes[i] = BoeBot.pulseIn(this.pin, false, 20000);
            }
            int code = 0;
            for (int i = 0; i < 12; i++) {
                if (lijstVanBitjes[i] > 1000) {
                    code |= 1 << i;
                }
            }
            int buttoncode = code & 0b1111111;
            int devicecode = (code >> 7) & 0b11111;
            System.out.println(code);
            System.out.println(Integer.toBinaryString(code));
            switch (code) {
                case KNOP_1:
                    System.out.println("Knop 1");
                    break;
                case KNOP_2:
                    System.out.println("Knop 2");
                    break;
                case KNOP_3:
                    System.out.println("Knop 3");
                    break;
                case KNOP_4:
                    System.out.println("Knop 4");
                    break;
                case KNOP_5:
                    System.out.println("Knop 5");
                    break;
                case KNOP_6:
                    System.out.println("Knop 6");
                    break;
                case KNOP_7:
                    System.out.println("Knop 7");
                    break;
                case KNOP_8:
                    System.out.println("Knop 8");
                    break;
                case KNOP_9:
                    System.out.println("Knop 9");
                    break;
                case KNOP_0:
                    System.out.println("Knop 0");
                    break;
                case KNOP_UIT:
                    System.out.println("Knop uit");     //Noodstop
                    break;
                case KNOP_CH_BOVEN:
                    System.out.println("Knop CH+");
                    break;
                case KNOP_CH_ONDER:
                    System.out.println("Knop CH-");
                    break;
                case KNOP_VOL_LINKS:
                    System.out.println("Knop VOL-");
                    break;
                case KNOP_VOL_RECHTS:
                    System.out.println("Knop VOL+");
                    break;
                default:
                    break;
            }
        }
        BoeBot.wait(10);
    }
}