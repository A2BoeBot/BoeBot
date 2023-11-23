package robot;

import TI.BoeBot;

public class RGB {

    public static void rgbALL(int r,int g,int b){
        for (int i = 0; i < 6; i++) {
            BoeBot.rgbSet(i,r,g,b);
        }
        BoeBot.rgbShow();
    }
}


