package cine.ui;

import java.util.Scanner;

public class ConsolaIO {

    private static final Scanner scanner = new Scanner(System.in);

    private ConsolaIO() {
    }

    public static int leerEntero(String mensaje) {
        return leerEnteroConRetry(mensaje);
    }

    public static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEnteroConRetry(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("Entrada invalida. Intente de nuevo.");
        }
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public static void esperarTecla(String mensaje) {
        System.out.print(mensaje);
        scanner.nextLine();
    }

    public static void cerrar() {
        scanner.close();
    }

    private static int leerEnteroConRetry(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            }
            scanner.nextLine();
            System.out.println("Entrada invalida. Intente de nuevo.");
        }
    }
}
