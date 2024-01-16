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
        System.out.println(huidigeRoute);
        switch (this.huidigeRoute.charAt(0)) {
            case 'w':
                motors.zetSnelheden(basisSnelheid);
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
            case 'a':
                motors.draai(-90, basisSnelheid);
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
            case 's':
                // TODO: 16/01/2024 draai om
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
            case 'd':
                motors.draai(90, basisSnelheid);
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
            case 'i':
                grijper.dicht();
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
            case 'e':
                grijper.open();
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
            case 'o':
                // TODO: 16/01/2024 deur functie
                this.huidigeRoute = this.huidigeRoute.substring(1);
                return true;
        }
        return false;
    }
}
