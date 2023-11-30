package motor;

import robot.Updatable;

public class Motors implements Updatable {
    private Motor linksMotor, rechtsMotor;

    public Motors(int pinLinksMotor, int pinRechtsMotor) {
        this.linksMotor = new Motor(pinLinksMotor, true);
        this.rechtsMotor = new Motor(pinRechtsMotor, false);
    }

    public void stop() {
        this.linksMotor.stop();
        this.rechtsMotor.stop();
    }

    public void zetSnelheden(int snelheid) {
        this.linksMotor.zetSnelheid(snelheid);
        this.rechtsMotor.zetSnelheid(snelheid);
    }

    public void zetSnelheden(int snelheid, int tijd) {
        this.linksMotor.zetSnelheid(snelheid, tijd);
        this.rechtsMotor.zetSnelheid(snelheid, tijd);
    }

    public void draai(int graden) {

    }

    public void draaiLinks(int snelheid) {
        linksMotor.zetSnelheid(snelheid);
        rechtsMotor.zetSnelheid(-snelheid);
    }

    public void draaiRechts(int snelheid) {
        linksMotor.zetSnelheid(-snelheid);
        rechtsMotor.zetSnelheid(snelheid);
    }

    public void herstart() {
        this.linksMotor.herstart();
        this.rechtsMotor.herstart();
    }

    @Override
    public void update() {
        linksMotor.update();
        rechtsMotor.update();
    }
}
