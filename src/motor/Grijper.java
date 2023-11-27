package motor;

import TI.BoeBot;
import TI.Servo;
import robot.RobotMain;
import robot.Updatable;

public class Grijper implements Updatable {
    private Servo grijper;
    private int openDuty;
    private int dichtDuty;
    private int dutyCycle = 500;

    public Grijper(int pin, int dichtDuty, int openDuty, GrijperCallback callback) {
        this.dichtDuty = dichtDuty;
        this.openDuty = openDuty;
        this.dutyCycle = openDuty;
        this.grijper = new Servo(pin);
        grijper.start();
        grijper.update(openDuty);
    }

    public int getOpenDuty() {
        return openDuty;
    }

    public int getDichtDuty() {
        return dichtDuty;
    }

    public int getDutyCycle() {
        return dutyCycle;
    }

    public void dicht(){
        for (int i = this.dutyCycle; i >= this.dichtDuty; i -= 10) {
            this.dutyCycle = i;
            this.update();
            BoeBot.wait(10);
        }
    }
    public void open(){
        for (int i = this.dutyCycle; i <= this.openDuty; i += 10) {
            this.dutyCycle = i;
            this.update();
            BoeBot.wait(10);
        }
    }
    @Override
    public void update() {
        grijper.update(dutyCycle);
    }
}
