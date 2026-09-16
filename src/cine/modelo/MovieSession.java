package cine.modelo;

import cine.excepciones.PuestoInvalidoException;
import cine.excepciones.PuestoOcupadoException;

public class MovieSession {

    private static final int CANTIDAD_PUESTOS = 20;

    private final String codigo;
    private final String pelicula;
    private final String horaInicio;
    private final Puesto[] puestos;

    public MovieSession(String codigo, String pelicula, String horaInicio) throws PuestoInvalidoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede ser nulo o vacio.");
        }
        if (pelicula == null || pelicula.trim().isEmpty()) {
            throw new IllegalArgumentException("La pelicula no puede ser nula o vacia.");
        }
        if (horaInicio == null || horaInicio.trim().isEmpty()) {
            throw new IllegalArgumentException("La hora de inicio no puede ser nula o vacia.");
        }
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.horaInicio = horaInicio;
        this.puestos = new Puesto[CANTIDAD_PUESTOS];
        for (int i = 0; i < CANTIDAD_PUESTOS; i++) {
            puestos[i] = new Puesto(i + 1);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPelicula() {
        return pelicula;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public Puesto getPuesto(int numero) throws PuestoInvalidoException {
        if (numero < 1 || numero > CANTIDAD_PUESTOS) {
            throw new PuestoInvalidoException("Numero de puesto invalido: " + numero + ". Debe estar entre 1 y " + CANTIDAD_PUESTOS + ".");
        }
        Puesto original = puestos[numero - 1];
        return new Puesto(original.getNumero(), original.getEstado());
    }

    public int contarVendidos() {
        int vendidos = 0;
        for (Puesto puesto : puestos) {
            if (puesto.getEstado() == EstadoPuesto.OCUPADO) {
                vendidos++;
            }
        }
        return vendidos;
    }

    public int contarDisponibles() {
        return CANTIDAD_PUESTOS - contarVendidos();
    }

    public int totalPuestos() {
        return CANTIDAD_PUESTOS;
    }

    public void venderPuesto(int numero) throws PuestoInvalidoException, PuestoOcupadoException {
        if (numero < 1 || numero > CANTIDAD_PUESTOS) {
            throw new PuestoInvalidoException("Numero de puesto invalido: " + numero + ". Debe estar entre 1 y " + CANTIDAD_PUESTOS + ".");
        }
        Puesto puesto = puestos[numero - 1];
        if (!puesto.estaDisponible()) {
            throw new PuestoOcupadoException("El puesto " + numero + " ya esta ocupado.");
        }
        puesto.marcarOcupado();
    }

    @Override
    public String toString() {
        return "Funcion [" + codigo + "] " + pelicula + " - " + horaInicio
                + " (" + contarVendidos() + "/" + CANTIDAD_PUESTOS + " vendidos)";
    }
}
