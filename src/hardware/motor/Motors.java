package hardware.motor;

import TI.Timer;
import interfaces.LedHandler;
import applicatie.Updatable;

public class Motors implements Updatable {
    private Motor linksMotor, rechtsMotor;
    private int snelheidLinks, snelheidRechts;
    private Timer draaiTimer;
    private boolean isAanHetDraaien;

    public int getSnelheidLinks() {
        return linksMotor.getSnelheid();
    }

    public int getSnelheidRechts() {
        return rechtsMotor.getSnelheid();
    }

    private boolean stop = false;

    private LedHandler ledHandler;

    public Motors(int pinLinksMotor, int pinRechtsMotor, LedHandler ledHandler, Timer draaiTimer) {
        this.linksMotor = new Motor(pinLinksMotor, true);
        this.rechtsMotor = new Motor(pinRechtsMotor, false);
        this.ledHandler = ledHandler;
        this.draaiTimer = draaiTimer;
        this.isAanHetDraaien = false;
    }

    public void stop() {
        this.linksMotor.stop();
        this.rechtsMotor.stop();
        this.stop = true;
    }

    public void zetSnelheden(int snelheid) {
        this.stop = false;
        this.linksMotor.zetSnelheid(snelheid);
        this.rechtsMotor.zetSnelheid(snelheid);
        this.snelheidLinks = snelheid;
        this.snelheidRechts = snelheid;
    }

    public void zetSnelheden(int snelheid, int tijd) {
        this.stop = false;
        this.linksMotor.zetSnelheid(snelheid, tijd);
        this.rechtsMotor.zetSnelheid(snelheid, tijd);
        this.snelheidLinks = snelheid;
        this.snelheidRechts = snelheid;
    }

    public void draai(int graden, int basisSnelheid) {
        if (graden > 0) {
            this.draaiRechts(basisSnelheid);
            this.draaiTimer.setInterval(1600*(graden/90));
        }
        else if (graden < 0) {
            this.draaiLinks(basisSnelheid);
            this.draaiTimer.setInterval(1600*(graden/90));
        }
        this.draaiTimer.mark();
        this.isAanHetDraaien = true;
    }

    public void draaiLinks(int snelheid) {
        this.linksMotor.zetSnelheid(snelheid);
        this.rechtsMotor.zetSnelheid(-snelheid);
        this.snelheidLinks = snelheid;
        this.snelheidRechts = -snelheid;
    }

    public void draaiRechts(int snelheid) {
        this.linksMotor.zetSnelheid(-snelheid);
        this.rechtsMotor.zetSnelheid(snelheid);
        this.snelheidLinks = -snelheid;
        this.snelheidRechts = snelheid;
    }

    public void draaiRelatief(int snelheid, boolean stuurInverse) {
        if (stuurInverse) {
            this.linksMotor.zetSnelheid(this.snelheidLinks + snelheid);
            this.rechtsMotor.zetSnelheid(this.snelheidRechts - snelheid);
        } else {
            this.linksMotor.zetSnelheid(this.snelheidLinks - snelheid);
            this.rechtsMotor.zetSnelheid(this.snelheidRechts + snelheid);
        }
    }


    @Override
    public void update() {
        this.linksMotor.update();
        this.rechtsMotor.update();
        if (ledHandler != null && !this.stop) {
            if (this.linksMotor.getSnelheid() > this.rechtsMotor.getSnelheid()) {
                ledHandler.links();
            } else if (this.linksMotor.getSnelheid() < this.rechtsMotor.getSnelheid()) {
                ledHandler.rechts();
            } else {
                if (this.linksMotor.getSnelheid() > 0) {
                    ledHandler.vooruit();
                } else if (this.linksMotor.getSnelheid() < 0) {
                    ledHandler.achteruit();
                } else {
                    ledHandler.uit();
                }
            }
        }
        if (!draaiTimer.timeout())
            return;
        draaiTimer.mark();
        if (isAanHetDraaien) {
            this.zetSnelheden(Math.abs(this.snelheidLinks));
            this.isAanHetDraaien = false;
        }
    }
}
