package cine.servicio;

import cine.excepciones.FuncionNoEncontradaException;
import cine.excepciones.MovieSessionDuplicadaException;
import cine.modelo.ListaMovieSession;
import cine.modelo.MovieSession;
import java.util.function.Consumer;

public class GestorMovieSession {

    private final ListaMovieSession lista;

    public GestorMovieSession() {
        this.lista = new ListaMovieSession();
    }

    public void registrar(MovieSession movieSession) throws MovieSessionDuplicadaException {
        if (movieSession == null) {
            throw new IllegalArgumentException("La movieSession no puede ser nula.");
        }
        if (movieSession.getCodigo() == null || movieSession.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de la movieSession no puede ser nulo o vacio.");
        }
        lista.agregar(movieSession);
    }

    public MovieSession buscarPorCodigo(String codigo) throws FuncionNoEncontradaException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new FuncionNoEncontradaException("El codigo no puede ser nulo o vacio.");
        }
        MovieSession resultado = lista.buscarPorCodigo(codigo);
        if (resultado == null) {
            throw new FuncionNoEncontradaException("No se encontro ninguna movieSession con el codigo: " + codigo);
        }
        return resultado;
    }

    public MovieSession[] listarTodas() {
        MovieSession[] resultado = new MovieSession[lista.tamano()];
        int[] contador = {0};
        lista.forEach(ms -> {
            resultado[contador[0]] = ms;
            contador[0]++;
        });
        return resultado;
    }

    public boolean estaVacio() {
        return lista.estaVacia();
    }

    public void recorrerFunciones(Consumer<MovieSession> accion) {
        lista.forEach(accion);
    }
}
