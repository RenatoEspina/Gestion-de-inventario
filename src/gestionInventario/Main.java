package gestionInventario;

import gestionInventario.almacen.*;
import gestionInventario.utilidades.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese nombre de la empresa: ");
        String nombre = sc.nextLine();
        Inventario almacen = new Inventario(nombre);
        GestorPersistencia gestor = new GestorPersistencia("data/");
        Consola.limpiarPantalla();

        System.out.print("¿Desea cargar datos existentes? (si/no): ");
        String cargar = sc.nextLine();
        if (cargar.equals("si")) {
            almacen = gestor.cargarInventario(nombre);
        }
        
        int opcion = 0;

        while (opcion != 5) {
            Consola.limpiarPantalla();
            System.out.println("\nSistema de Inventario - Empresa: " + nombre);
            System.out.println("1.- Crear nueva sección");
            System.out.println("2.- Agregar producto");
            System.out.println("3.- Compra/Venta de producto");
            System.out.println("4.- Informacion de producto");
            System.out.println("5.- Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    Consola.limpiarPantalla();
                    System.out.print("Ingrese nombre de la nueva sección: ");
                    String nombreSeccion = sc.nextLine();
                    almacen.nuevaSeccion(nombreSeccion);
                    Consola.enterParaContinuar(sc);
                    break;

                case 2:
                    Consola.limpiarPantalla();
                    System.out.print("Ingrese nombre del producto: ");
                    String nombreProducto = sc.nextLine();
                    System.out.print("Ingrese proveedor: ");
                    String proveedor = sc.nextLine();
                    System.out.print("Ingrese compra inicial: ");
                    int cantidad = sc.nextInt();
                    sc.nextLine();
                    Producto producto = new Producto(nombreProducto, proveedor, cantidad);
                    almacen.agregarProducto(producto);
                    Consola.enterParaContinuar(sc);
                    break;

                case 3:
                    Consola.limpiarPantalla();
                    almacen.compraYVenta();
                    Consola.enterParaContinuar(sc);
                    break;

                case 4:
                	Consola.limpiarPantalla();
                	almacen.informacionProducto();
                	Consola.enterParaContinuar(sc);
                	break;
                	
                case 5:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida!");
                    Consola.enterParaContinuar(sc);
                    break;
            }
        }

        sc.close();
    }
}

