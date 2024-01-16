package interfaces;

import hardware.motor.Motors;
import hardware.motor.Grijper;
import interfaces.LedHandler;

public class Route {
    private String huidigeRoute;

    public Route() {
        this.huidigeRoute = "";
    }

    public void voegActiesToe(String route) {
        this.huidigeRoute += route;
    }

    public boolean actie(Motors motors, Grijper grijper, LedHandler ledHandler, int basisSnelheid) {
        switch (this.huidigeRoute.charAt(0)) {
            case 'w':
                motors.zetSnelheden(basisSnelheid);
                return true;
            case 'a':
                motors.draai(-90, basisSnelheid);
                return true;
            case 's':
                motors.draai(180, basisSnelheid);
                motors.zetSnelheden(basisSnelheid);
                return true;
            case 'd':
                motors.draai(90, basisSnelheid);
                return true;
            case 'i':
                ledHandler.deur(true);
                return true;
            case 'e':

                return true;
        }
        return false;
    }
}
