package hardware.other;

import TI.BoeBot;
import TI.PinMode;
import applicatie.Updatable;
import hardware.sensoren.AfstandsbedieningCallback;

import java.util.Arrays;

public class Afstandsbediening implements Updatable {
    public static final int KNOP_1 = 0;
    public static final int KNOP_2 = 0b1;
    public static final int KNOP_3 = 0b10;
    public static final int KNOP_4 = 0b11;
    public static final int KNOP_5 = 0b100;
    public static final int KNOP_6 = 0b101;
    public static final int KNOP_7 = 0b110;
    public static final int KNOP_8 = 0b111;
    public static final int KNOP_9 = 0b1000;
    public static final int KNOP_0 = 0b1001;
    public static final int KNOP_UIT = 0b10101;
    public static final int KNOP_CH_BOVEN = 0b10000;
    public static final int KNOP_CH_ONDER = 0b10001;
    public static final int KNOP_VOL_LINKS = 0b10011;
    public static final int KNOP_VOL_RECHTS = 0b10010;
    private int pulseLen;
    private int pin;
    private int lijstVanBitjes[] = new int[12];
    private AfstandsbedieningCallback callback;

    public Afstandsbediening(int pin, AfstandsbedieningCallback callback) {
        this.pin = pin;
        BoeBot.setMode(pin, PinMode.Input);
        this.callback = callback;
    }


    public void update() {
        this.pulseLen = BoeBot.pulseIn(pin, false, 6000);
        if (this.pulseLen > 2000) {
            for (int i = 0; i < 12; i++) {
                lijstVanBitjes[i] = BoeBot.pulseIn(this.pin, false, 20000);
            }
            System.out.println(Arrays.toString(lijstVanBitjes));
            int code = 0;
            for (int i = 0; i < 12; i++) {
                if (lijstVanBitjes[i] > 1000) {
                    code |= 1 << i;
                }
            }
            int buttoncode = code & 0b1111111;
            int devicecode = (code >> 7) & 0b11111;
            switch (buttoncode) {
                case KNOP_1: this.callback.knop_1_Ingedrukt(); break;
                case KNOP_2:
                    this.callback.knop_2_Ingedrukt();
                    break;
                case KNOP_3:
                    this.callback.knop_3_Ingedrukt();
                    break;
                case KNOP_4:
                    this.callback.knop_4_Ingedrukt();
                    break;
                case KNOP_5:
                    this.callback.knop_5_Ingedrukt();
                    break;
                case KNOP_6:
                    this.callback.knop_6_Ingedrukt();
                    break;
                case KNOP_7:
                    this.callback.knop_7_Ingedrukt();
                    break;
                case KNOP_8:
                    this.callback.knop_8_Ingedrukt();
                    break;
                case KNOP_9:
                    this.callback.knop_9_Ingedrukt();
                    break;
                case KNOP_0:
                    this.callback.knop_0_Ingedrukt();
                    break;
                case KNOP_UIT:
                    this.callback.knop_Uit_Ingedrukt();
                    break;
                case KNOP_CH_BOVEN:
                    this.callback.knop_Ch_BovenIngedrukt();
                    break;
                case KNOP_CH_ONDER:
                    this.callback.knop_Ch_OnderIngedrukt();
                    break;
                case KNOP_VOL_LINKS:
                    this.callback.knop_Vol_Links_Ingedrukt();
                    break;
                case KNOP_VOL_RECHTS:
                    this.callback.knop_Vol_Rechts_Ingedrukt();
                    break;
                default:
                    break;
            }
        }
    }
}