package gestionInventario.almacen;

import gestionInventario.excepciones.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Clase principal que representa el inventario completo del almacén.
 * <p>
 * Gestiona múltiples secciones y proporciona una API de alto nivel para manipular
 * el inventario. Esta versión está adaptada para una arquitectura de GUI,
 * separando la lógica de negocio de la interacción con el usuario.
 * </p>
 *
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 * @see Secciones
 * @see Producto
 */
public class Inventario {
    private HashMap<String, Secciones> secciones;

    /**
     * Constructor que inicializa un inventario vacío.
     */
    public Inventario() {
        this.secciones = new HashMap<>();
    }

    /**
     * Obtiene el mapa de secciones del inventario.
     *
     * @return Un HashMap donde la clave es el nombre de la sección y el valor es el objeto Secciones.
     */
    public HashMap<String, Secciones> getSecciones() {
        return this.secciones;
    }

    /**
     * Devuelve la lista de secciones en un formato observable, ideal para JavaFX.
     *
     * @return Una ObservableList de las secciones en el inventario.
     */
    public ObservableList<Secciones> getSeccionesAsObservableList() {
        return FXCollections.observableArrayList(secciones.values());
    }
    
    /**
     * Crea una nueva sección en el inventario.
     *
     * @param nombre El nombre de la nueva sección a crear.
     * @return true si la sección se creó, false si ya existía.
     */
    public boolean nuevaSeccion(String nombre) {
        if (!this.secciones.containsKey(nombre)) {
            secciones.put(nombre, new Secciones(nombre));
            return true;
        }
        return false;
    }

    /**
     * Elimina una sección existente del inventario.
     *
     * @param nombre El nombre de la sección a eliminar.
     * @return true si la sección se eliminó, false si no existía.
     */
    public boolean eliminarSeccion(String nombre) {
        return this.secciones.remove(nombre) != null;
    }
    
    /**
     * Elimina un producto de todo el inventario, buscando en todas las secciones.
     *
     * @param nombre El nombre del producto a eliminar.
     * @throws ProductoNoEncontradoException si el producto no se encuentra en ninguna sección.
     */
    public void eliminarProducto(String nombre) throws ProductoNoEncontradoException {
        for (Secciones s : secciones.values()) {
            if (s.getProductos().containsKey(nombre)) {
                s.eliminarProducto(nombre);
                return; // Termina en cuanto lo encuentra y elimina
            }
        }
        throw new ProductoNoEncontradoException("No se encontró el producto '" + nombre + "' en ninguna sección.");
    }
    
    /**
     * Agrega un producto a una sección específica del inventario.
     *
     * @param seccionNombre El nombre de la sección destino.
     * @param producto      El producto a agregar.
     * @return true si el producto se agregó, false si ya existía en esa sección.
     * @throws SeccionNoEncontradaException si la sección no existe.
     */
    public boolean agregarProducto(String seccionNombre, Producto producto) throws SeccionNoEncontradaException {
        Secciones seccion = this.secciones.get(seccionNombre);
        if (seccion == null) {
            throw new SeccionNoEncontradaException("La sección '" + seccionNombre + "' no existe.");
        }
        return seccion.agregarProducto(producto);
    }
    
    /**
     * Filtra productos que tienen un número mínimo de ventas.
     * <p>
     * Este método devuelve una lista de los productos que cumplen con el criterio,
     * para que la interfaz de usuario pueda mostrarlos. No interactúa con la consola
     * ni genera el reporte directamente.
     * </p>
     *
     * @param ventasMinimas El número mínimo de ventas requerido.
     * @return Una lista de productos que cumplen con el criterio de ventas.
     */
    public List<Producto> filtrarProductosPorVentas(int ventasMinimas) {
        List<Producto> productosFiltrados = new ArrayList<>();
        for (Secciones s : secciones.values()) {
            for (Producto p : s.getProductos().values()) {
                if (p.getVentasTotales() >= ventasMinimas) {
                    productosFiltrados.add(p);
                }
            }
        }
        return productosFiltrados;
    }

    /**
     * Busca y devuelve la sección a la que pertenece un producto.
     *
     * @param producto El producto a buscar.
     * @return La sección que contiene el producto, or null si no se encuentra.
     */
    public Secciones encontrarSeccionDeProducto(Producto producto) {
        if (producto == null) return null;
        for (Secciones s : secciones.values()) {
            if (s.getProductos().containsKey(producto.getNombre())) {
                return s;
            }
        }
        return null;
    }
}