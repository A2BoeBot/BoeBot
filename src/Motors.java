import TI.BoeBot;
import TI.Servo;
import TI.Timer;


public class Motors {
    private int pinLinks, pinRechts;
    private int snelheid = 0;
    private int maxSnelheid;
    private int maxDraaiSnelheid;
    private static final int DUTYCYCLE = 1500;
    private Servo links, rechts;

    public Motors(Servo links, Servo rechts,int maxSnelheid, int maxDraaiSnelheid) {
        this.pinLinks = pinLinks;
        this.pinRechts = pinRechts;
        this.maxSnelheid = maxSnelheid;
        this.maxDraaiSnelheid = maxDraaiSnelheid;
        this.links = links;
        this.rechts = rechts;
        links.start();
        rechts.start();
    }

    public void setSnelheid(int snelheid, int tijd){
        Timer t1 = new Timer(tijd);
        while (true){
            if (t1.timeout()) {
                if (this.snelheid < snelheid) {
                    this.snelheid += 1;
                } else if (this.snelheid > snelheid) {
                    this.snelheid -= 1;
                }
                if(this.snelheid == snelheid){
                    return;
                }
                System.out.println(this.snelheid);
                this.rechts.update(DUTYCYCLE + this.snelheid);
                this.links.update(DUTYCYCLE - this.snelheid);
                t1.mark();
            }
            BoeBot.wait(100);
        }
    }

    public void stop(){
        links.stop();
        rechts.stop();
    }

    public void draaien(int richting,int draaiSnelheid){

    }

    public void draaiGraden(int graden, int draaiSnelheid){

    }

    private void motor(boolean motorId, int dutyCycle) {
        if(motorId){
            rechts.update(dutyCycle);
        } else {
            links.update(   dutyCycle);
        }
    }
}
