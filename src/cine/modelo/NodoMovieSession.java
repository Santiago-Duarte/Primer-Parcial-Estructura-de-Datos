package cine.modelo;

class NodoMovieSession {

    private final MovieSession movieSession;
    private NodoMovieSession siguiente;

    NodoMovieSession(MovieSession movieSession) {
        if (movieSession == null) {
            throw new IllegalArgumentException("La funcion no puede ser nula.");
        }
        this.movieSession = movieSession;
        this.siguiente = null;
    }

    MovieSession getMovieSession() {
        return movieSession;
    }

    NodoMovieSession getSiguiente() {
        return siguiente;
    }

    void setSiguiente(NodoMovieSession siguiente) {
        this.siguiente = siguiente;
    }
}
