package gestionInventario.almacen;

import java.util.HashMap;
import gestionInventario.excepciones.*;

/**
 * Clase que representa una sección dentro del almacén que contiene productos.
 * <p>
 * Cada sección tiene un nombre único y mantiene un inventario de productos organizados
 * por su nombre. Proporciona métodos para gestionar productos dentro de la sección,
 * incluyendo agregar, eliminar, comprar y vender productos.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Producto
 * @see ProductoNoEncontradoException
 * @see StockInsuficienteException
 */
public class Secciones {
    private String nombre;
    private HashMap<String, Producto> productos;
    
    /**
     * Constructor para crear una nueva sección vacía.
     * 
     * @param nombre El nombre de la sección
     * @throws IllegalArgumentException si el nombre es null o vacío
     */
    public Secciones(String nombre) {
        this.nombre = nombre;
        this.productos = new HashMap<>();
    }
    
    /**
     * Constructor para crear una nueva sección con un producto inicial.
     * 
     * @param nombre    El nombre de la sección
     * @param producto  El producto inicial a agregar a la sección
     * @throws IllegalArgumentException si el nombre es null o vacío, o si el producto es null
     */
    public Secciones(String nombre, Producto producto) {
        this.nombre = nombre;
        this.productos = new HashMap<>();
        productos.put(producto.getNombre(), producto);
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
     * 
     * @return Un HashMap donde la clave es el nombre del producto y el valor es el objeto Producto
     */
    public HashMap<String, Producto> getProductos() {
        return this.productos;
    }
    
    /**
     * Elimina un producto de la sección.
     * 
     * @param nombre El nombre del producto a eliminar
     * @throws ProductoNoEncontradoException si el producto no existe en la sección
     */
    public void eliminarProducto(String nombre) throws ProductoNoEncontradoException {
        if (productos.remove(nombre) == null) {
            throw new ProductoNoEncontradoException("No existe el producto " + nombre + " para eliminar.");
        } else {
            System.out.println("Producto eliminado.");
        }
    }

    /**
     * Agrega un nuevo producto a la sección.
     * <p>
     * Si el producto ya existe en la sección, se muestra un mensaje de advertencia
     * y no se realiza la operación.
     * </p>
     * 
     * @param producto El producto a agregar a la sección
     * @throws IllegalArgumentException si el producto es null
     */
    public void agregarProducto(Producto producto) {
        if (!productos.containsKey(producto.getNombre())) {
            productos.put(producto.getNombre(), producto);
        } else {
            System.out.println("EL PRODUCTO YA EXISTE!!!");
        }
    }
    
    /**
     * Realiza una compra de un producto existente en la sección.
     * <p>
     * Actualiza el stock del producto y gestiona la información del proveedor.
     * Si el producto no existe, se muestra un mensaje de error.
     * </p>
     * 
     * @param nombreProducto El nombre del producto a comprar
     * @param cantidad       La cantidad de unidades a comprar
     * @param proveedor      El proveedor de la compra
     * @throws IllegalArgumentException si la cantidad es negativa o si el proveedor es null o vacío
     */
    public void comprarProducto(String nombreProducto, int cantidad, String proveedor) {
        Producto producto = productos.get(nombreProducto);
        if (producto == null) {
            System.out.println("El producto " + nombreProducto + " no existe en la sección " + nombre);
            return;
        }
        producto.compra(proveedor, cantidad);
        System.out.println("Compra realizada: " + cantidad + " unidades de " + nombreProducto);
    }

    /**
     * Realiza una venta de un producto existente en la sección.
     * <p>
     * Verifica que el producto exista y que haya stock suficiente antes de realizar la venta.
     * </p>
     * 
     * @param nombreProducto El nombre del producto a vender
     * @param cantidad       La cantidad de unidades a vender
     * @throws ProductoNoEncontradoException si el producto no existe en la sección
     * @throws StockInsuficienteException si no hay suficiente stock para realizar la venta
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public void venderProducto(String nombreProducto, int cantidad) throws ProductoNoEncontradoException, StockInsuficienteException {
        Producto producto = productos.get(nombreProducto);
        if (producto == null) {
            throw new ProductoNoEncontradoException("El producto " + nombreProducto + " no existe en la sección " + nombre);
        }
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException("No hay suficiente stock para vender " + cantidad + " unidades de " + nombreProducto);
        }
        producto.venta(cantidad);
        System.out.println("Venta realizada: " + cantidad + " unidades de " + nombreProducto);
    }

    /**
     * Muestra la información completa de un producto específico en la sección.
     * 
     * @param nombre El nombre del producto del cual se desea obtener información
     */
    public void informacionProducto(String nombre) {
        Producto buscado = productos.get(nombre);
        if (buscado == null) {
            System.out.println("El producto no existe en esta sección");
            return;
        }
        buscado.informacion();
    }
    
    /**
     * Lista todos los productos existentes en la sección.
     * <p>
     * Muestra por consola los nombres de todos los productos contenidos en la sección.
     * Si la sección está vacía, muestra un mensaje indicando que no hay productos.
     * </p>
     */
    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos en esta sección.");
            return;
        }
        for (String key : productos.keySet()) {
            System.out.println("- " + key);
        }
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