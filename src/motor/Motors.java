package motor;

import robot.LED;
import robot.Updatable;

public class Motors implements Updatable {
    private Motor linksMotor, rechtsMotor;
    private int snelheidLinks, snelheidRechts;
    private boolean stop = false;

    private LED led;

    public Motors(int pinLinksMotor, int pinRechtsMotor, LED led) {
        this.linksMotor = new Motor(pinLinksMotor, true);
        this.rechtsMotor = new Motor(pinRechtsMotor, false);
        this.led = led;
    }

    public void stop() {
        this.linksMotor.stop();
        this.rechtsMotor.stop();
        this.stop = true;
    }

    public void zetSnelheden(int snelheid) {
        this.linksMotor.zetSnelheid(snelheid);
        this.rechtsMotor.zetSnelheid(snelheid);
        this.snelheidLinks = snelheid;
        this.snelheidRechts = snelheid;
    }

    public void zetSnelheden(int snelheid, int tijd) {
        this.linksMotor.zetSnelheid(snelheid, tijd);
        this.rechtsMotor.zetSnelheid(snelheid, tijd);
        this.snelheidLinks = snelheid;
        this.snelheidRechts = snelheid;
    }

    public void draai(int graden) {

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

    public void draaiRelatief(int snelheid) {
        this.linksMotor.zetSnelheid(this.snelheidLinks - snelheid);
        this.rechtsMotor.zetSnelheid(this.snelheidRechts + snelheid);
    }

    public void herstart() {
        this.linksMotor.herstart();
        this.rechtsMotor.herstart();
        this.stop = false;
    }

    @Override
    public void update() {
        this.linksMotor.update();
        this.rechtsMotor.update();
        if (led != null && !this.stop) {
            if (this.linksMotor.getSnelheid() > this.rechtsMotor.getSnelheid()) {
                led.links();
            } else if (this.linksMotor.getSnelheid() < this.rechtsMotor.getSnelheid()) {
                led.rechts();
            } else {
                if (this.linksMotor.getSnelheid() > 0) {
                    led.vooruit();
                } else if (this.linksMotor.getSnelheid() < 0) {
                    led.achteruit();
                } else {
//                    led.uit();
                }
            }
        }
    }
}
