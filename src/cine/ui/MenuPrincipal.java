package cine.ui;

import cine.excepciones.MovieSessionDuplicadaException;
import cine.excepciones.PuestoInvalidoException;
import cine.excepciones.PuestoOcupadoException;
import cine.modelo.MovieSession;
import cine.servicio.GestorMovieSession;
import cine.servicio.ServicioReproduccion;
import cine.servicio.ServicioVenta;

public class MenuPrincipal {

    private final GestorMovieSession gestor;
    private final ServicioVenta servicioVenta;
    private final ServicioReproduccion servicioReproduccion;

    public MenuPrincipal(GestorMovieSession gestor) {
        this.gestor = gestor;
        this.servicioVenta = new ServicioVenta(gestor);
        this.servicioReproduccion = new ServicioReproduccion(gestor);
    }

    public void run() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = ConsolaIO.leerEnteroEnRango("Seleccione una opcion: ", 0, 3);
            switch (opcion) {
                case 1:
                    registrarFuncion();
                    break;
                case 2:
                    comprarEntrada();
                    break;
                case 3:
                    iniciarLabores();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
        System.out.println("Sistema cerrado.");
        ConsolaIO.cerrar();
    }

    private void mostrarMenu() {
        System.out.println("\n=== SISTEMA CINE ===");
        System.out.println("1. Registrar funcion");
        System.out.println("2. Comprar entrada");
        System.out.println("3. Iniciar labores");
        System.out.println("0. Salir");
    }

    private void registrarFuncion() {
        String codigo = ConsolaIO.leerTexto("Ingrese el codigo de la funcion: ");
        String pelicula = ConsolaIO.leerTexto("Ingrese el nombre de la pelicula: ");
        String horaInicio = ConsolaIO.leerTexto("Ingrese la hora de inicio (HH:mm): ");

        try {
            MovieSession ms = new MovieSession(codigo, pelicula, horaInicio);
            gestor.registrar(ms);
            System.out.println("Funcion registrada exitosamente.");
        } catch (PuestoInvalidoException e) {
            System.out.println(e.getMessage());
        } catch (MovieSessionDuplicadaException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void comprarEntrada() {
        if (!servicioVenta.hayFunciones()) {
            System.out.println("No hay funciones registradas.");
            return;
        }

        MovieSession[] funciones = servicioVenta.listarFunciones();
        System.out.println("\n=== FUNCIONES DISPONIBLES ===");
        for (int i = 0; i < funciones.length; i++) {
            System.out.println((i + 1) + ". " + funciones[i]);
        }

        int seleccion = ConsolaIO.leerEnteroEnRango("Seleccione una funcion (1-" + funciones.length + "): ", 0, funciones.length);
        if (seleccion == 0) {
            return;
        }

        MovieSession movieSession = funciones[seleccion - 1];

        while (true) {
            System.out.println("\n=== PUESTOS - " + movieSession.getCodigo() + " ===");
            System.out.print(VistaPuestos.generarCuadricula(movieSession));
            System.out.println("\n(Ingrese 0 para volver al menu)");

            int puesto = ConsolaIO.leerEntero("Ingrese el numero de puesto: ");
            if (puesto == 0) {
                return;
            }

            try {
                servicioVenta.venderEntrada(movieSession, puesto);
                System.out.println("Entrada comprada exitosamente.");
                return;
            } catch (PuestoInvalidoException e) {
                System.out.println(e.getMessage());
            } catch (PuestoOcupadoException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void iniciarLabores() {
        if (!servicioReproduccion.hayFunciones()) {
            System.out.println("No hay funciones registradas.");
            return;
        }

        System.out.println("\n=== INICIANDO LABORES ===");

        servicioReproduccion.reproducirTodas(movieSession -> {
            System.out.println("\n--- REPRODUCIENDO FUNCION ---");
            System.out.println("Pelicula: " + movieSession.getPelicula());
            System.out.println("Hora: " + movieSession.getHoraInicio());
            System.out.println("Vendidos: " + movieSession.contarVendidos() + " / " + movieSession.totalPuestos());
            System.out.println("Disponibles: " + movieSession.contarDisponibles() + " / " + movieSession.totalPuestos());
            System.out.println();
            System.out.print(VistaPuestos.generarCuadricula(movieSession));
            ConsolaIO.esperarTecla("\nPresione Enter para continuar...");
        });

        System.out.println("\nTodas las funciones fueron reproducidas.");
    }
}
