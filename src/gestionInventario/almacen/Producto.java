package gestionInventario.almacen;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase que representa un producto básico en el sistema de gestión de inventario,
 * adaptada para su uso con JavaFX.
 * <p>
 * Un producto contiene información sobre su nombre, proveedores, stock actual,
 * compras totales y ventas totales, utilizando propiedades de JavaFX para permitir
 * el enlace de datos (data binding) con la interfaz de usuario.
 * </p>
 * * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 * @see gestionInventario.almacen.subProductos.ProductoPerecible
 * @see gestionInventario.almacen.subProductos.ProductoPremium
 */
public class Producto {
	
	/** Propiedad observable para el nombre del producto, permite el enlace con la UI. */
	private final SimpleStringProperty nombre;

	/** Lista observable de proveedores del producto, para vincular a vistas como tablas o listas. */
	private final ObservableList<String> proveedores;

	/** Propiedad observable para el stock actual del producto. */
	private final SimpleIntegerProperty stock;

	/** Propiedad observable para el total de unidades compradas del producto. */
	private final SimpleIntegerProperty compras;

	/** Propiedad observable para el total de unidades vendidas del producto. */
	private final SimpleIntegerProperty ventas;
    
    /**
     * Constructor para crear un nuevo producto.
     * @param nombre     El nombre del producto
     * @param proveedor  El proveedor inicial del producto
     * @param compra     La cantidad inicial comprada del producto
     * @throws IllegalArgumentException si el nombre es null o vacío, o si la compra es negativa
     */
    public Producto(String nombre, String proveedor, int compra) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser vacío.");
        }
        if (compra < 0) {
            throw new IllegalArgumentException("La compra inicial no puede ser negativa.");
        }
        
        this.nombre = new SimpleStringProperty(nombre);
        this.compras = new SimpleIntegerProperty(compra);
        this.stock = new SimpleIntegerProperty(compra);
        this.ventas = new SimpleIntegerProperty(0);
        this.proveedores = FXCollections.observableArrayList();
        this.proveedores.add(proveedor);
    }
    
    // --- Getters de valores ---
    
    /**
     * Obtiene el stock actual del producto.
     * @return El número de unidades disponibles en stock
     */
    public int getStock() {
        return this.stock.get();
    }
    
    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto
     */
    public String getNombre() {
        return this.nombre.get();
    }
    
    /**
     * Obtiene la lista observable de proveedores del producto.
     * @return Una ObservableList con los nombres de todos los proveedores
     */
    public ObservableList<String> getProveedores() {
        return this.proveedores;
    }

    /**
     * Obtiene el total de compras realizadas para este producto.
     * @return El número total de unidades compradas
     */
    public int getComprasTotales() {
        return this.compras.get();
    }

    /**
     * Obtiene el total de ventas realizadas para este producto.
     * @return El número total de unidades vendidas
     */
    public int getVentasTotales() {
        return this.ventas.get();
    }

    // --- Getters de propiedades (para JavaFX) ---

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public SimpleIntegerProperty stockProperty() {
        return stock;
    }

    public SimpleIntegerProperty comprasProperty() {
        return compras;
    }

    public SimpleIntegerProperty ventasProperty() {
        return ventas;
    }

    // --- Lógica de negocio ---

    /**
     * Agrega un nuevo proveedor a la lista de proveedores del producto.
     * Si el proveedor ya existe, no se realiza ninguna acción.
     * @param proveedor El nombre del proveedor a agregar
     */
    public void agregarProveedor(String proveedor) {
        if (!this.proveedores.contains(proveedor)) {
            this.proveedores.add(proveedor);
        }
    }

    /**
     * Ajusta los valores de compras y ventas totales, y recalcula el stock.
     * @param comprasTotales El nuevo valor total de compras
     * @param ventasTotales  El nuevo valor total de ventas
     */
    public void ajustarComprasVentas(int comprasTotales, int ventasTotales) {
        this.compras.set(comprasTotales);
        this.ventas.set(ventasTotales);
        this.stock.set(comprasTotales - ventasTotales);
    }
    
    /**
     * Registra una compra del producto, actualizando stock y lista de proveedores.
     * @param proveedor El proveedor de la compra
     * @param cantidad    La cantidad de unidades compradas
     */
    public void compra(String proveedor, int cantidad) {
        if (cantidad < 0) return;
        agregarProveedor(proveedor);
        this.compras.set(getComprasTotales() + cantidad);
        this.stock.set(getStock() + cantidad);
    }
    
    /**
     * Registra una venta del producto, actualizando stock y ventas totales.
     * @param cantidad La cantidad de unidades vendidas
     * @throws IllegalArgumentException si la cantidad de venta es mayor al stock disponible
     */
    public void venta(int cantidad) {
        if (cantidad < 0 || cantidad > getStock()) {
            throw new IllegalArgumentException("La cantidad de venta es inválida o supera el stock.");
        }
        this.ventas.set(getVentasTotales() + cantidad);
        this.stock.set(getStock() - cantidad);
    }
    
    /**
     * Devuelve una representación en String del producto.
     * @return El nombre del producto
     */
    @Override
    public String toString() {
        return getNombre();
    }
}