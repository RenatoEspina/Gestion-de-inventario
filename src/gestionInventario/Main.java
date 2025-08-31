package gestionInventario;

import gestionInventario.almacen.*;
import gestionInventario.utilidades.Consola;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese nombre de la empresa: ");
        String nombre = sc.nextLine();
        Inventario almacen = new Inventario(nombre);
        Consola.limpiarPantalla();

        String opcionP;
        System.out.print("Desea precargar datos para testeo? (si/no): ");
        opcionP= sc.nextLine();
        if(opcionP.equals("si")) {
        	almacen.nuevaSeccion("Electronica");
            almacen.nuevaSeccion("Alimentos");
            almacen.nuevaSeccion("Ropa");
            Producto p1 = new Producto("Televisor", "LG", 10);
            Producto p2 = new Producto("Celular", "Samsung", 15);
            Producto p3 = new Producto("Leche", "Colun", 30);
            Producto p4 = new Producto("Pan", "Bimbo", 25);
            Producto p5 = new Producto("Polera", "Adidas", 20);
            almacen.agregarProducto("Electronica",p1);
            almacen.agregarProducto("Electronica",p2);
            almacen.agregarProducto("Alimentos",p3);
            almacen.agregarProducto("Alimentos",p4);
            almacen.agregarProducto("Ropa",p5);

            Consola.enterParaContinuar(sc);
            Consola.limpiarPantalla();

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

