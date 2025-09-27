package gestionInventario.almacen;

import gestionInventario.excepciones.*;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase que representa una sección dentro del almacén que contiene productos.
 * <p>
 * Adaptada para una arquitectura de aplicación de escritorio, esta clase gestiona la
 * lógica de negocio sin interactuar directamente con la interfaz de usuario.
 * Las operaciones notifican el resultado a través de valores de retorno o excepciones.
 * </p>
 *
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 * @see Producto
 * @see ProductoNoEncontradoException
 * @see StockInsuficienteException
 */
public class Secciones {
	/** Nombre de la sección en el inventario. */
	private String nombre;

	/** Mapa de productos asociados a esta sección. */
	private HashMap<String, Producto> productos;


    /**
     * Constructor para crear una nueva sección vacía.
     *
     * @param nombre El nombre de la sección
     */
    public Secciones(String nombre) {
        this.nombre = nombre;
        this.productos = new HashMap<>();
    }

    /**
     * Constructor para crear una nueva sección en base a otra.
     *
     * @param otra La seccion a copiar
     */
    public Secciones(Secciones otra) {
        this.nombre = otra.nombre;
    }
    
    /**
     * Obtiene el nombre de la sección.
     *
     * @return El nombre de la sección
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Obtiene el mapa de productos contenidos en la sección.
     * Es útil para operaciones lógicas que requieren búsquedas por nombre.
     *
     * @return Un HashMap donde la clave es el nombre del producto y el valor es el objeto Producto
     */
    public HashMap<String, Producto> getProductos() {
        return this.productos;
    }

    /**
     * Devuelve la lista de productos en un formato observable, ideal para JavaFX.
     *
     * @return Una ObservableList de los productos en la sección.
     */
    public ObservableList<Producto> getProductosAsObservableList() {
        return FXCollections.observableArrayList(productos.values());
    }
    
    /**
     * Establece un nuevo nombre para la sección.
     * @param nombre El nuevo nombre de la sección.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
    /**
     * Clona los productos de un HashMap de productos mediante referencia
     *
     * @param productos Los productos a clonar.
     */
    public void setProductosReferencia(HashMap<String, Producto> productos) {
    	this.productos = new HashMap<>(productos);
    	return;
    }
    
    /**
     * Elimina un producto de la sección.
     *
     * @param nombre El nombre del producto a eliminar
     * @throws ProductoNoEncontradoException si el producto no existe en la sección
     */
    public void eliminarProducto(String nombre) throws ProductoNoEncontradoException {
        if (productos.remove(nombre) == null) {
            throw new ProductoNoEncontradoException("No existe el producto '" + nombre + "' en esta sección.");
        }
    }

    /**
     * Agrega un nuevo producto a la sección.
     *
     * @param producto El producto a agregar a la sección
     * @return true si el producto se agregó con éxito, false si ya existía.
     */
    public boolean agregarProducto(Producto producto) {
        if (!productos.containsKey(producto.getNombre())) {
            productos.put(producto.getNombre(), producto);
            return true;
        } else {
            return false; // El producto ya existe, no se agrega.
        }
    }
    
    /**
     * Realiza una compra de un producto existente en la sección.
     *
     * @param nombreProducto El nombre del producto a comprar
     * @param cantidad       La cantidad de unidades a comprar
     * @param proveedor      El proveedor de la compra
     * @throws ProductoNoEncontradoException si el producto no se encuentra en esta sección.
     */
    public void comprarProducto(String nombreProducto, int cantidad, String proveedor) throws ProductoNoEncontradoException {
        Producto producto = productos.get(nombreProducto);
        if (producto == null) {
            throw new ProductoNoEncontradoException("El producto '" + nombreProducto + "' no existe en la sección " + nombre);
        }
        producto.compra(proveedor, cantidad);
    }

    /**
     * Realiza una venta de un producto existente en la sección.
     *
     * @param nombreProducto El nombre del producto a vender
     * @param cantidad       La cantidad de unidades a vender
     * @throws ProductoNoEncontradoException si el producto no existe en la sección
     * @throws StockInsuficienteException si no hay suficiente stock para realizar la venta
     */
    public void venderProducto(String nombreProducto, int cantidad) throws ProductoNoEncontradoException, StockInsuficienteException {
        Producto producto = productos.get(nombreProducto);
        if (producto == null) {
            throw new ProductoNoEncontradoException("El producto '" + nombreProducto + "' no existe en la sección " + nombre);
        }
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException("No hay suficiente stock para vender " + cantidad + " unidades de " + nombreProducto + " (Stock actual: " + producto.getStock() + ").");
        }
        producto.venta(cantidad);
    }
    
    /**
     * Devuelve una representación en String de la sección.
     *
     * @return El nombre de la sección
     */
    @Override
    public String toString() {
        return nombre;
    }
}