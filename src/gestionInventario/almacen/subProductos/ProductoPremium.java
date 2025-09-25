package gestionInventario.almacen.subProductos;

import gestionInventario.almacen.Producto;

/**
 * Clase que representa un producto premium en el sistema de gestión de inventario.
 * <p>
 * Extiende la funcionalidad de la clase {@link Producto} agregando un límite máximo de stock
 * que no puede ser excedido. Esto permite controlar el inventario de productos de alto valor
 * o espacio limitado.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Producto
 * @see ProductoPerecible
 */
public class ProductoPremium extends Producto {
    private int stockMaximo;

    /**
     * Constructor para crear un nuevo producto premium.
     * 
     * @param nombre      El nombre del producto premium
     * @param proveedor   El proveedor inicial del producto
     * @param compra      La cantidad inicial comprada del producto
     * @param stockMaximo El límite máximo de stock permitido para este producto
     * @throws IllegalArgumentException si el stock máximo es menor que la compra inicial
     *                                  o si es un valor negativo
     */
    public ProductoPremium(String nombre, String proveedor, int compra, int stockMaximo) {
        super(nombre, proveedor, compra);
        this.stockMaximo = stockMaximo;
    }

    /**
     * Realiza una compra del producto premium, verificando que no exceda el stock máximo permitido.
     * <p>
     * Si la compra haría que el stock total supere el límite máximo, se muestra un mensaje de advertencia
     * y no se realiza la compra. En caso contrario, se delega la operación a la clase padre.
     * </p>
     * 
     * @param proveedor El proveedor de la compra
     * @param cantidad  La cantidad de unidades a comprar
     * @throws IllegalArgumentException si la cantidad es negativa o si excede el stock máximo permitido
     */
    @Override
    public void compra(String proveedor, int cantidad) {
        if (getStock() + cantidad > stockMaximo) {
            System.out.println("No se puede comprar " + cantidad + " unidades. Límite máximo de stock: " + stockMaximo);
        } else {
            super.compra(proveedor, cantidad);
        }
    }

    /**
     * Obtiene el límite máximo de stock permitido para este producto premium.
     * 
     * @return El número máximo de unidades que pueden estar en stock
     */
    public int getStockMaximo() {
        return stockMaximo;
    }

    /**
     * Establece un nuevo límite máximo de stock para el producto premium.
     * <p>
     * Si el nuevo límite es menor que el stock actual, se muestra una advertencia pero se permite
     * el cambio. Las futuras compras estarán limitadas por este nuevo valor.
     * </p>
     * 
     * @param stockMaximo El nuevo límite máximo de stock a establecer
     * @throws IllegalArgumentException si el stock máximo es un valor negativo
     */
    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    /**
     * Muestra por consola la información completa del producto premium.
     * <p>
     * Incluye toda la información de la clase padre más el límite máximo de stock específico
     * de los productos premium.
     * </p>
     */
    @Override
    public void informacion() {
        super.informacion();
        System.out.println("Stock máximo permitido: " + stockMaximo);
    }
}