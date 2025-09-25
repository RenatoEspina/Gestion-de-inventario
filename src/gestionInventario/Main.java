package gestionInventario;

import gestionInventario.almacen.*;
import gestionInventario.almacen.subProductos.*;
import gestionInventario.utilidades.*;
import java.time.LocalDate;
import gestionInventario.excepciones.*;

/**
 * Clase principal que inicia la aplicación de gestión de inventario.
 * <p>
 * Esta clase contiene el método main y gestiona los menús principales de la aplicación.
 * Proporciona una interfaz de consola para interactuar con el sistema de inventario,
 * permitiendo gestionar secciones y productos de diferentes tipos.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Inventario
 * @see GestorPersistencia
 * @see Consola
 * @see Producto
 * @see ProductoPremium
 * @see ProductoPerecible
 */
public class Main {
    
    /**
     * Método principal que inicia la aplicación de gestión de inventario.
     * <p>
     * Carga el inventario desde persistencia, muestra el menú principal y gestiona
     * el flujo de la aplicación hasta que el usuario decide salir. Al finalizar,
     * guarda automáticamente el estado del inventario.
     * </p>
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        Inventario almacen = new Inventario();
        GestorPersistencia gestor = new GestorPersistencia("data/");
        almacen = gestor.cargarInventario();
        
        int opcion = 0;

        while (opcion != 3) {
            String menuPrincipal =  "\nSistema de Inventario\n"
                    + "1.- Secciones\n"
                    + "2.- Productos\n"
                    + "3.- Salir\n";
            opcion = Consola.leerEntero(menuPrincipal + "Opción: ", true);

            switch (opcion) {
                case 1:
                    Consola.limpiarPantalla();
                    menuSecciones(almacen);
                    break;

                case 2:
                    Consola.limpiarPantalla();
                    menuProductos(almacen);
                    break;

                case 3:
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

    /**
     * Menú para la gestión de secciones del inventario.
     * <p>
     * Permite al usuario agregar nuevas secciones o eliminar secciones existentes.
     * </p>
     * 
     * @param almacen El inventario sobre el cual se realizarán las operaciones
     */
    private static void menuSecciones(Inventario almacen) {
        int opcion = 0;
        while (opcion != 3) {
            Consola.limpiarPantalla();
            String menu = "\nMenú Secciones\n"
                        + "1.- Agregar Sección\n"
                        + "2.- Eliminar Sección\n"
                        + "3.- Volver\n";
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
                    String seccionEliminar = Consola.leerString("Ingrese nombre de la sección a eliminar: ");
                    almacen.eliminarSeccion(seccionEliminar);
                    Consola.enterParaContinuar();
                    break;

                case 3:
                    Consola.limpiarPantalla();
                    break;

                default:
                    System.out.println("Opción no válida!");
                    Consola.enterParaContinuar();
                    break;
            }
        }
    }

    /**
     * Menú para la gestión de productos del inventario.
     * <p>
     * Proporciona funcionalidades completas para gestionar productos: agregar, eliminar,
     * comprar, vender y generar reportes. Soporta diferentes tipos de productos
     * (normal, premium, perecible) con características específicas.
     * </p>
     * 
     * @param almacen El inventario sobre el cual se realizarán las operaciones
     */
    private static void menuProductos(Inventario almacen) {
        int opcion = 0;
        while (opcion != 6) {
            Consola.limpiarPantalla();
            String menu = "\nMenú Productos\n"
                        + "1.- Agregar Producto\n"
                        + "2.- Eliminar Producto\n"
                        + "3.- Comprar Producto\n"
                        + "4.- Vender Producto\n"
                        + "5.- Generar Reporte de Ventas\n"
                        + "6.- Volver\n";
            opcion = Consola.leerEntero(menu + "Opción: ", true);

            switch (opcion) {
            
                case 1:
                    Consola.limpiarPantalla();
                    String nombreProducto = Consola.leerString("Ingrese nombre del producto: ");
                    String proveedor = Consola.leerString("Ingrese proveedor: ");
                    int cantidad = Consola.leerEntero("Ingrese compra inicial: ");
                    String tipo = Consola.leerString("Tipo de producto (normal/premium/perecible): ");

                    switch(tipo.toLowerCase()) {
                        case "premium":
                            int stockMax = Consola.leerEntero("Ingrese stock máximo permitido: ");
                            ProductoPremium premium = new ProductoPremium(nombreProducto, proveedor, cantidad, stockMax);
                            almacen.agregarProducto(premium);
                            break;
                        case "perecible":
                            LocalDate fechaVenc = Consola.leerFecha("Ingrese fecha de vencimiento (YYYY-MM-DD): ");
                            ProductoPerecible perecible = new ProductoPerecible(nombreProducto, proveedor, cantidad, fechaVenc);
                            almacen.agregarProducto(perecible);
                            break;
                        default:
                            Producto normal = new Producto(nombreProducto, proveedor, cantidad);
                            almacen.agregarProducto(normal);
                            break;
                    }

                    System.out.println("Producto agregado correctamente.");
                    Consola.enterParaContinuar();
                    break;

                case 2:
                    Consola.limpiarPantalla();
                    String nombreEliminar = Consola.leerString("Ingrese nombre del producto a eliminar: ");
                    try {
                        almacen.eliminarProducto(nombreEliminar);
                        System.out.println("Producto eliminado correctamente.");
                    } 
                    catch (ProductoNoEncontradoException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    Consola.enterParaContinuar();
                    break;

                case 3:
                    Consola.limpiarPantalla();
                    String prodCompra = Consola.leerString("Ingrese nombre del producto a comprar: ");
                    String prov = Consola.leerString("Ingrese proveedor: ");
                    int cantidadCompra = Consola.leerEntero("Ingrese cantidad a comprar: ");
                    almacen.comprarProducto(prodCompra, cantidadCompra, prov);
                    Consola.enterParaContinuar();
                    break;

                case 4:
                    Consola.limpiarPantalla();
                    String prodVenta = Consola.leerString("Ingrese nombre del producto a vender: ");
                    int cantidadVenta = Consola.leerEntero("Ingrese cantidad a vender: ");
                    try {
                        almacen.venderProducto(prodVenta, cantidadVenta);
                    } 
                    catch (ProductoNoEncontradoException | StockInsuficienteException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    Consola.enterParaContinuar();
                    break;

                case 5:
                    Consola.limpiarPantalla();
                    int ventaFiltrada = Consola.leerEntero("Ingrese la cantidad minima de ventas buscadas: ");
                    almacen.filtrarProductos(ventaFiltrada);
                    Consola.enterParaContinuar();
                    break;

                case 6:
                    Consola.limpiarPantalla();
                    break;

                default:
                    System.out.println("Opción no válida!");
                    Consola.enterParaContinuar();
                    break;
            }
        }
    }
}