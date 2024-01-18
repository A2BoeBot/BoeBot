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
        if(!huidigeRoute.isEmpty()) {
            System.out.println(huidigeRoute.charAt(0));
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
                    motors.draai(180, basisSnelheid);
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
                    ledHandler.deur(motors);
                    this.huidigeRoute = this.huidigeRoute.substring(1);
                    return true;
            }
        }
        return false;
    }
}
