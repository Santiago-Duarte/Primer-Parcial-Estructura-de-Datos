package cine.modelo;

import cine.excepciones.PuestoInvalidoException;
import cine.excepciones.PuestoOcupadoException;

public class Puesto {

    private final int numero;
    private EstadoPuesto estado;

    public Puesto(int numero) throws PuestoInvalidoException {
        this(numero, EstadoPuesto.DISPONIBLE);
    }

    public Puesto(int numero, EstadoPuesto estado) throws PuestoInvalidoException {
        if (numero < 1) {
            throw new PuestoInvalidoException("Numero de puesto invalido: " + numero + ". Debe ser mayor a 0.");
        }
        this.numero = numero;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoPuesto getEstado() {
        return estado;
    }

    public boolean estaDisponible() {
        return estado == EstadoPuesto.DISPONIBLE;
    }

    public void marcarOcupado() throws PuestoOcupadoException {
        if (estado == EstadoPuesto.OCUPADO) {
            throw new PuestoOcupadoException("El puesto " + numero + " ya esta ocupado.");
        }
        this.estado = EstadoPuesto.OCUPADO;
    }

    void marcarDisponible() {
        if (estado == EstadoPuesto.DISPONIBLE) {
            throw new IllegalStateException("El puesto " + numero + " ya esta disponible.");
        }
        this.estado = EstadoPuesto.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "Puesto " + numero + " - " + estado;
    }
}
