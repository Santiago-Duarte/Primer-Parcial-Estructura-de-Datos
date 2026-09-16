package cine.servicio;

import cine.modelo.MovieSession;
import java.util.function.Consumer;

public class ServicioReproduccion {

    private final GestorMovieSession gestor;

    public ServicioReproduccion(GestorMovieSession gestor) {
        this.gestor = gestor;
    }

    public void reproducirTodas(Consumer<MovieSession> accion) {
        gestor.recorrerFunciones(accion);
    }

    public boolean hayFunciones() {
        return !gestor.estaVacio();
    }
}
