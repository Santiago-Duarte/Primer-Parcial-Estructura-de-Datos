package cine.ui;

import cine.excepciones.PuestoInvalidoException;
import cine.modelo.EstadoPuesto;
import cine.modelo.MovieSession;
import cine.modelo.Puesto;

public class VistaPuestos {

    private static final int COLUMNAS = 5;
    private static final int TOTAL_PUESTOS = 20;

    private VistaPuestos() {
    }

    public static String generarCuadricula(MovieSession movieSession) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= TOTAL_PUESTOS; i++) {
            try {
                Puesto puesto = movieSession.getPuesto(i);
                String estado = (puesto.getEstado() == EstadoPuesto.OCUPADO) ? "O" : "D";
                sb.append(String.format("%2d  [%s]", puesto.getNumero(), estado));
            } catch (PuestoInvalidoException e) {
                sb.append(String.format("%2d [?]", i));
            }
            if (i % COLUMNAS == 0) {
                sb.append("\n");
            } else {
                sb.append("    ");
            }
        }
        return sb.toString();
    }
}
