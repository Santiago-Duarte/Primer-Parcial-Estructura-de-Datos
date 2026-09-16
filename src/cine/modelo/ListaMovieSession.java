package cine.modelo;

import cine.excepciones.MovieSessionDuplicadaException;
import java.util.function.Consumer;

public class ListaMovieSession {

    private NodoMovieSession primero;
    private int tamaño;

    public ListaMovieSession() {
        this.primero = null;
        this.tamaño = 0;
    }

    public void agregar(MovieSession movieSession) throws MovieSessionDuplicadaException {
        if (movieSession == null) {
            throw new IllegalArgumentException("La movieSession no puede ser nula.");
        }

        NodoMovieSession actual = primero;
        while (actual != null) {
            if (actual.getMovieSession().getCodigo().equals(movieSession.getCodigo())) {
                throw new MovieSessionDuplicadaException("Ya existe una movieSession con el codigo: " + movieSession.getCodigo());
            }
            actual = actual.getSiguiente();
        }

        NodoMovieSession nuevo = new NodoMovieSession(movieSession);
        if (primero == null) {
            primero = nuevo;
        } else {
            actual = primero;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamaño++;
    }

    public MovieSession buscarPorCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        NodoMovieSession actual = primero;
        while (actual != null) {
            if (actual.getMovieSession().getCodigo().equals(codigo)) {
                return actual.getMovieSession();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public boolean existeCodigo(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int tamano() {
        return tamaño;
    }

    public void forEach(Consumer<MovieSession> accion) {
        NodoMovieSession actual = primero;
        while (actual != null) {
            accion.accept(actual.getMovieSession());
            actual = actual.getSiguiente();
        }
    }
}
