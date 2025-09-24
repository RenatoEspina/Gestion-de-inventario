package gestionInventario;

import gestionInventario.almacen.*;
import gestionInventario.utilidades.*;

public class Main {
    public static void main(String[] args) {
        Inventario almacen = new Inventario();
        GestorPersistencia gestor = new GestorPersistencia("data/");
        almacen = gestor.cargarInventario();
        
        int opcion = 0;

        while (opcion != 6) {
            Consola.limpiarPantalla();
            String menu =  "\nSistema de Inventario\n"
                    + "1.- Crear nueva sección\n"
                    + "2.- Agregar producto\n"
                    + "3.- Eliminar producto\n"
                    + "4.- Compra/Venta de producto\n"
                    + "5.- Informacion de producto\n"
                    + "6.- Salir\n";
            opcion = Consola.leerEntero(menu + "Opción: ", true);

            switch (opcion) {
                case 1:
                    Consola.limpiarPantalla();
                    String nombreSeccion = Consola.leerString("Ingrese nombre de la nueva sección: ");
                    almacen.nuevaSeccion(nombreSeccion);
                    Consola.enterParaContinuar();
                    break;

                case 2:
                    Consola.limpiarPantalla();
                    String nombreProducto = Consola.leerString("Ingrese nombre del producto: ");
                    String proveedor = Consola.leerString("Ingrese proveedor: ");
                    int cantidad = Consola.leerEntero("Ingrese compra inicial: ");
                    Producto producto = new Producto(nombreProducto, proveedor, cantidad);
                    almacen.agregarProducto(producto);
                    Consola.enterParaContinuar();
                    break;

                case 3:
                	Consola.limpiarPantalla();
                    String nombrePro = Consola.leerString("Ingrese nombre del producto: ");
                    almacen.eliminarProducto(nombrePro);
                    Consola.enterParaContinuar();
                    break;

                case 4:
                    Consola.limpiarPantalla();
                    almacen.compraYVenta();
                    Consola.enterParaContinuar();
                    break;

                case 5:
                	Consola.limpiarPantalla();
                	almacen.informacionProducto();
                	Consola.enterParaContinuar();
                	break;
                	
                case 6:
                	gestor.guardarInventario(almacen);
                	System.out.println("Inventario guardado correctamente. Saliendo...");
                    break;

                default:
                    System.out.println("Opción no válida!");
                    Consola.enterParaContinuar();
                    break;
            }
        }
    }
}

