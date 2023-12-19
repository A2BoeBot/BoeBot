package hardware.other;

import TI.BoeBot;
import applicatie.Updatable;

import java.util.ArrayList;

public class LED implements Updatable {
    private ArrayList<NeoPixel> neoPixels = new ArrayList<>();

    public LED(int neoPixels) {
        for (int i = 0; i < neoPixels; i++) {
            this.neoPixels.add(new NeoPixel(i));
        }
    }

    public void alles(int r, int g, int b) {
        for (NeoPixel neoPixel : neoPixels) {
            neoPixel.setRood(r);
            neoPixel.setGroen(g);
            neoPixel.setBlauw(b);
        }
    }

    public void set(int address, int r, int g, int b) {
        neoPixels.get(address).setRood(r);
        neoPixels.get(address).setGroen(g);
        neoPixels.get(address).setBlauw(b);
    }

    public void uit() {
        for (NeoPixel neoPixel : neoPixels) {
            neoPixel.setRood(0);
            neoPixel.setGroen(0);
            neoPixel.setBlauw(0);
        }
    }


    @Override
    public void update() {
        for (NeoPixel neoPixel : neoPixels) {
            BoeBot.rgbSet(neoPixel.getAddress(), neoPixel.getRood(), neoPixel.getGroen(), neoPixel.getBlauw());
        }
        BoeBot.rgbShow();
    }


}

