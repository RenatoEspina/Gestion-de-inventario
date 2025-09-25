package gestionInventario.almacen;

import java.util.ArrayList;

/**
 * Clase que representa un producto básico en el sistema de gestión de inventario.
 * <p>
 * Un producto contiene información sobre su nombre, proveedores, stock actual,
 * compras totales y ventas totales. Proporciona métodos para gestionar las
 * operaciones de compra, venta y ajuste de inventario.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see ProductoPerecible
 * @see ProductoPremium
 */
public class Producto {
    private String nombre;
    private ArrayList<String> proveedores;
    private int stock;
    private int compras;
    private int ventas;
    
    /**
     * Constructor para crear un nuevo producto.
     * 
     * @param nombre     El nombre del producto
     * @param proveedor  El proveedor inicial del producto
     * @param compra     La cantidad inicial comprada del producto
     * @throws IllegalArgumentException si el nombre es null o vacío, o si la compra es negativa
     */
    public Producto(String nombre, String proveedor, int compra) {
        this.nombre = nombre;
        this.compras = compra;
        this.stock = compra;
        this.ventas = 0;
        this.proveedores = new ArrayList<>();
        this.proveedores.add(proveedor);
    }
    
    /**
     * Obtiene el stock actual del producto.
     * 
     * @return El número de unidades disponibles en stock
     */
    public int getStock() {
        return this.stock;
    }
    
    /**
     * Obtiene el nombre del producto.
     * 
     * @return El nombre del producto
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Obtiene la lista de proveedores del producto.
     * 
     * @return Una ArrayList con los nombres de todos los proveedores
     */
    public ArrayList<String> getProveedores() {
        return this.proveedores;
    }

    /**
     * Obtiene el total de compras realizadas para este producto.
     * 
     * @return El número total de unidades compradas
     */
    public int getComprasTotales() {
        return this.compras;
    }

    /**
     * Obtiene el total de ventas realizadas para este producto.
     * 
     * @return El número total de unidades vendidas
     */
    public int getVentasTotales() {
        return this.ventas;
    }

    /**
     * Agrega un nuevo proveedor a la lista de proveedores del producto.
     * Si el proveedor ya existe, no se realiza ninguna acción.
     * 
     * @param proveedor El nombre del proveedor a agregar
     */
    public void agregarProveedor(String proveedor) {
        if (!this.proveedores.contains(proveedor)) {
            this.proveedores.add(proveedor);
        }
    }

    /**
     * Ajusta los valores de compras y ventas totales, y recalcula el stock.
     * <p>
     * Este método es útil para correcciones o ajustes manuales del inventario.
     * </p>
     * 
     * @param comprasTotales El nuevo valor total de compras
     * @param ventasTotales  El nuevo valor total de ventas
     * @throws IllegalArgumentException si las ventas totales son mayores que las compras totales
     */
    public void ajustarComprasVentas(int comprasTotales, int ventasTotales) {
        this.compras = comprasTotales;
        this.ventas = ventasTotales;
        this.stock = comprasTotales - ventasTotales;
    }
    
    /**
     * Registra una compra del producto, actualizando stock y lista de proveedores.
     * 
     * @param proveedor El proveedor de la compra
     * @param compra    La cantidad de unidades compradas
     * @throws IllegalArgumentException si la cantidad de compra es negativa
     */
    public void compra(String proveedor, int compra) {
        if (!this.proveedores.contains(proveedor)) {
            this.proveedores.add(proveedor);
        }
        this.compras += compra;
        this.stock += compra;
    }
    
    /**
     * Registra una venta del producto, actualizando stock y ventas totales.
     * 
     * @param venta La cantidad de unidades vendidas
     * @throws IllegalArgumentException si la cantidad de venta es mayor al stock disponible
     */
    public void venta(int venta) {
        this.ventas += venta;
        this.stock -= venta;
    }
    
    /**
     * Muestra por consola la información completa del producto.
     * <p>
     * La información incluye nombre, proveedores, stock, compras y ventas.
     * </p>
     */
    public void informacion() {
        System.out.println("Informacion de " + nombre);
        System.out.println("Proveedores " + proveedores);
        System.out.println("Stock: " + stock);
        System.out.println("Compras: " + compras);
        System.out.println("Ventas: " + ventas);
    }
    
    /**
     * Devuelve una representación en String del producto.
     * 
     * @return El nombre del producto
     */
    @Override
    public String toString() {
        return nombre;
    }
}