package gestionInventario.utilidades;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Consola {
	private static Scanner sc= new Scanner(System.in);
	
	public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void enterParaContinuar() {
        System.out.println("\nPresione ENTER para continuar...");
        sc.nextLine();
    }
    
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                if (mensaje != null) System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero válido.");
            }
        }
    }
    
    public static int leerEntero(String mensaje, boolean limpiarPantallaAntes) {
        while (true) {
            if (limpiarPantallaAntes) limpiarPantalla();
            try {
                if (mensaje != null) System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número entero válido.");
            }
        }
    }


    public static String leerString(String mensaje) {
        while (true) {
            if (mensaje != null) System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Error: No puedes dejar el campo vacío.");
        }
    }
    
    public static LocalDate leerFecha(String mensaje) {
        while (true) {
            if (mensaje != null) System.out.print(mensaje);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input); // espera formato YYYY-MM-DD
            } catch (DateTimeParseException e) {
                System.out.println("Error: Debes ingresar una fecha válida en formato YYYY-MM-DD.");
            }
        }
    }
}

