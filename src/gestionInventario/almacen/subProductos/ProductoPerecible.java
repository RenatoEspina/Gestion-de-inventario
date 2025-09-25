package gestionInventario.almacen.subProductos;

import gestionInventario.almacen.Producto;
import java.time.LocalDate;

/**
 * Clase que representa un producto perecible en el sistema de gestión de inventario.
 * <p>
 * Extiende la funcionalidad de la clase {@link Producto} agregando una fecha de vencimiento
 * que permite controlar la caducidad del producto. Impide la venta de productos que hayan
 * excedido su fecha de vencimiento.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Producto
 * @see ProductoPremium
 */
public class ProductoPerecible extends Producto {
    private LocalDate fechaVencimiento;

    /**
     * Constructor para crear un nuevo producto perecible.
     * 
     * @param nombre           El nombre del producto perecible
     * @param proveedor        El proveedor inicial del producto
     * @param compra           La cantidad inicial comprada del producto
     * @param fechaVencimiento La fecha de vencimiento del producto
     * @throws IllegalArgumentException si la fecha de vencimiento es null o anterior a la fecha actual
     */
    public ProductoPerecible(String nombre, String proveedor, int compra, LocalDate fechaVencimiento) {
        super(nombre, proveedor, compra);
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Obtiene la fecha de vencimiento del producto perecible.
     * 
     * @return La fecha de vencimiento del producto
     */
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * Establece una nueva fecha de vencimiento para el producto.
     * 
     * @param fechaVencimiento La nueva fecha de vencimiento a establecer
     * @throws IllegalArgumentException si la fecha de vencimiento es null
     */
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Realiza una venta del producto perecible, verificando primero que no esté vencido.
     * <p>
     * Si el producto ha excedido su fecha de vencimiento, se muestra un mensaje de advertencia
     * y no se realiza la venta. En caso contrario, se delega la operación a la clase padre.
     * </p>
     * 
     * @param cantidad La cantidad de unidades a vender
     * @throws IllegalArgumentException si la cantidad es mayor al stock disponible
     * @throws IllegalStateException si el producto está vencido
     */
    @Override
    public void venta(int cantidad) {
        if (LocalDate.now().isAfter(fechaVencimiento)) {
            System.out.println("No se puede vender " + getNombre() + " porque está vencido desde " + fechaVencimiento);
        } else {
            super.venta(cantidad);
        }
    }

    /**
     * Muestra por consola la información completa del producto perecible.
     * <p>
     * Incluye toda la información de la clase padre más la fecha de vencimiento específica
     * de los productos perecibles.
     * </p>
     */
    @Override
    public void informacion() {
        super.informacion();
        System.out.println("Fecha de vencimiento: " + fechaVencimiento);
    }
}