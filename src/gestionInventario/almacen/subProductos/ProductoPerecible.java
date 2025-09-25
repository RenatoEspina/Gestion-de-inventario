package gestionInventario.almacen.subProductos;

import gestionInventario.almacen.Producto;
import gestionInventario.excepciones.ProductoVencidoException; // Nueva excepción
import java.time.LocalDate;

/**
 * Clase que representa un producto perecible, adaptada para JavaFX.
 * <p>
 * Extiende de {@link Producto} y añade una fecha de vencimiento. La lógica de negocio
 * ahora lanza una excepción si se intenta vender un producto vencido.
 * </p>
 *
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 * @see Producto
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
     */
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * Realiza una venta del producto perecible, verificando primero que no esté vencido.
     *
     * @param cantidad La cantidad de unidades a vender
     * @throws ProductoVencidoException si el producto ha superado su fecha de vencimiento.
     */
    @Override
    public void venta(int cantidad) throws ProductoVencidoException {
        if (LocalDate.now().isAfter(fechaVencimiento)) {
            throw new ProductoVencidoException("No se puede vender '" + getNombre() + "' porque está vencido desde " + fechaVencimiento);
        }
        super.venta(cantidad);
    }
}