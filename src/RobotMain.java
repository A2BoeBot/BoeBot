import TI.BoeBot;
import TI.PinMode;

public class RobotMain {

    public static void main(String[] args) {
        Motors moters = new Motors(new Servo(12), new Servo(13), 0,0);
        moters.setSnelheid(200,50);

        moters.stop();

//        moters.setSnelheid(0,50);
//        boolean state = true;
//        BoeBot.setMode(0, PinMode.Output);
//
//        while (true) {
//            state = !state;
//            BoeBot.digitalWrite(0, state);
//            BoeBot.wait(250);
//        }
    }
}
