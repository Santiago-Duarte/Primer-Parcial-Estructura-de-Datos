package cine;

import cine.servicio.GestorMovieSession;
import cine.ui.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        GestorMovieSession gestor = new GestorMovieSession();
        MenuPrincipal menu = new MenuPrincipal(gestor);
        menu.run();
    }
}
