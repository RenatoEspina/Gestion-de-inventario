package gestionInventario.almacen.subProductos;

import gestionInventario.almacen.Producto;
import gestionInventario.excepciones.StockMaximoExcedidoException; // Nueva excepción
/**
 * Clase que representa un producto premium, adaptada para JavaFX.
 * <p>
 * Extiende de {@link Producto} y añade un límite de stock. La lógica de compra
 * ahora lanza una excepción si se intenta exceder dicho límite.
 * </p>
 *
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 * @see Producto
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
     */
    public ProductoPremium(String nombre, String proveedor, int compra, int stockMaximo) {
        super(nombre, proveedor, compra);
        if (compra > stockMaximo) {
            throw new IllegalArgumentException("La compra inicial (" + compra + ") no puede superar el stock máximo (" + stockMaximo + ").");
        }
        this.stockMaximo = stockMaximo;
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
     *
     * @param stockMaximo El nuevo límite máximo de stock a establecer
     */
    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }
    
    /**
     * Realiza una compra del producto premium, verificando que no exceda el stock máximo.
     *
     * @param proveedor El proveedor de la compra
     * @param cantidad  La cantidad de unidades a comprar
     * @throws StockMaximoExcedidoException si la compra supera el límite de stock.
     */
    @Override
    public void compra(String proveedor, int cantidad) throws StockMaximoExcedidoException { 
        if (getStock() + cantidad > stockMaximo) {
            int espacioDisponible = stockMaximo - getStock();
            throw new StockMaximoExcedidoException("No se puede comprar " + cantidad + " unidades. Se supera el límite de stock. Espacio disponible: " + espacioDisponible + ".");
        }
        super.compra(proveedor, cantidad);
    }
}