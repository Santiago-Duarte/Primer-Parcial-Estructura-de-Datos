package cine.servicio;

import cine.excepciones.FuncionNoEncontradaException;
import cine.excepciones.PuestoInvalidoException;
import cine.excepciones.PuestoOcupadoException;
import cine.modelo.MovieSession;

public class ServicioVenta {

    private final GestorMovieSession gestor;

    public ServicioVenta(GestorMovieSession gestor) {
        this.gestor = gestor;
    }

    public MovieSession buscarFuncion(String codigo) throws FuncionNoEncontradaException {
        return gestor.buscarPorCodigo(codigo);
    }

    public MovieSession[] listarFunciones() {
        return gestor.listarTodas();
    }

    public boolean hayFunciones() {
        return !gestor.estaVacio();
    }

    public void venderEntrada(MovieSession movieSession, int numeroPuesto)
            throws PuestoInvalidoException, PuestoOcupadoException {
        movieSession.venderPuesto(numeroPuesto);
    }
}
