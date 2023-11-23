package robot;

import TI.BoeBot;
import TI.Timer;

public class LED {
    Timer timer = new Timer(1000);
    Boolean state = false;

    public void rgbALL(int r,int g,int b){
        for (int i = 0; i < 6; i++) {
            BoeBot.rgbSet(i,r,g,b);
        }
        BoeBot.rgbShow();
    }

    public void ledStop() {
        if (timer.timeout()) {
            this.state = !this.state;
            if (state) {
                rgbALL(255, 0, 0);
            } else {
                rgbALL(0, 0, 0);
            }
            timer.mark();
//            BoeBot.digitalWrite(0, this.state);
        }
    }
}


