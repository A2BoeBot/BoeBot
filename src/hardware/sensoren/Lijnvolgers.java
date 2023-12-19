package hardware.sensoren;

import TI.BoeBot;
import applicatie.Updatable;

import java.util.ArrayList;
import java.util.Arrays;

public class Lijnvolgers implements Updatable {
    private ArrayList<Lijnvolger> lijvolgers;
    private final LijnvolgersCallback callback;
    private boolean[] states = new boolean[3];
    private int[] inputs = new int[3];

    public Lijnvolgers(LijnvolgersCallback callback) {
        this.lijvolgers = new ArrayList<>();
        this.callback = callback;
    }
    public void voegLijnvolgerToe(Lijnvolger lijnvolger){
        this.lijvolgers.add(lijnvolger);

    }

    @Override
    public void update() {
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = lijvolgers.get(i).getInput();
        }
        int baseline = max(inputs);
        boolean gelijk = baseline - gemiddeld(inputs) < 10;
        for (int i = 0; i < inputs.length; i++) {
            states[i] = !(inputs[i] < baseline) && !gelijk;
        }
        callback.lijnVolgers(states);
    }

    public static int max(int[] ints) {
        int max = ints[0];
        for (int i : ints) {
            if (max < i) {
                max = i;
            }
        }
        return max;
    }

    public static int gemiddeld(int[] ints) {
        int totaal = 0;
        int aantal = 0;
        for (int i : ints) {
            totaal += i;
            aantal++;
        }
        return totaal / aantal;
    }
}
