package gestionInventario.utilidades;
import java.util.Scanner;

public class Consola {
	public static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void enterParaContinuar(Scanner scanner) {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}
