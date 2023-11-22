package sensoren;

import robot.Updatable;

public class Voelspriet implements Updatable {
    private boolean vorigeToestand;

    public Voelspriet() {
        this.vorigeToestand = true;
    }

    @Override
    public void update() {

    }
}
