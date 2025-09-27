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
	/** un mapa donde se guardan secciones. */
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
     * Clona las secciones de un HashMap de secciones mediante referencia
     *
     * @param secciones Las secciones a clonar.
     */
    public void setSeccionesReferencia(HashMap<String, Secciones> secciones) {
    	this.secciones = new HashMap<>(secciones);
    	return;
    }
    
    /**
     * Clona las secciones de un HashMap de secciones sin dependencia del HashMap clonado
     *
     * @param secciones Las secciones a clonar.
     */
    public void setSeccionesClonacion(HashMap<String, Secciones> secciones) {
    	for (HashMap.Entry<String, Secciones> entry : secciones.entrySet()) {
    	    this.secciones.put(entry.getKey(), new Secciones(entry.getValue()));
    	}
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
    
    /**
     * Renombra una sección existente en el inventario.
     *
     * @param nombreActual El nombre actual de la sección a renombrar.
     * @param nombreNuevo El nuevo nombre para la sección.
     * @return true si el renombrado fue exitoso, false si el nuevo nombre ya existe o la sección actual no se encuentra.
     */
    public boolean renombrarSeccion(String nombreActual, String nombreNuevo) {
        // Validar que el nuevo nombre no esté vacío y no exista ya
        if (nombreNuevo == null || nombreNuevo.trim().isEmpty() || secciones.containsKey(nombreNuevo)) {
            return false;
        }

        // Buscar y obtener la sección con el nombre actual
        Secciones seccion = secciones.get(nombreActual);
        if (seccion != null) {
            // Eliminar la entrada antigua del mapa
            secciones.remove(nombreActual);
            
            // Actualizar el nombre dentro del objeto sección
            seccion.setNombre(nombreNuevo);
            
            // Volver a insertar la sección en el mapa con la nueva clave (el nuevo nombre)
            secciones.put(nombreNuevo, seccion);
            
            return true; // Éxito
        }

        return false; // La sección original no fue encontrada
    }
    
 // Archivo: gestionInventario/almacen/Inventario.java

    /**
     * Devuelve una lista observable con todos los productos de todas las secciones.
     * Es ideal para vistas consolidadas en JavaFX.
     *
     * @return Una ObservableList que contiene todos los productos del inventario.
     */
    public ObservableList<Producto> getAllProductosAsObservableList() {
        ObservableList<Producto> todosLosProductos = FXCollections.observableArrayList();
        for (Secciones s : secciones.values()) {
            todosLosProductos.addAll(s.getProductos().values());
        }
        return todosLosProductos;
    }
    
    /**
     * Busca y devuelve la sección a la que pertenece un producto por su nombre.
     *
     * @param nombreProducto El nombre del producto a buscar.
     * @return La sección que contiene el producto, o null si no se encuentra.
     */
    public Secciones encontrarSeccionDeProducto(String nombreProducto) {
        if (nombreProducto == null) return null;
        for (Secciones s : secciones.values()) {
            if (s.getProductos().containsKey(nombreProducto)) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * Filtra la lista de todos los productos para encontrar aquellos que coincidan con un proveedor.
     * La búsqueda no es sensible a mayúsculas/minúsculas y busca coincidencias parciales.
     *
     * @param proveedor El nombre o parte del nombre del proveedor a buscar.
     * @return Una lista de productos que tienen al menos un proveedor que coincide con la búsqueda.
     */
    public List<Producto> filtrarProductosPorProveedor(String proveedor) {
        List<Producto> productosFiltrados = new ArrayList<>();
        // Preparamos el término de búsqueda para que no sea sensible a mayúsculas o espacios.
        String proveedorLowerCase = proveedor.trim().toLowerCase();
        if (proveedorLowerCase.isEmpty()) {
            return productosFiltrados; // Devolvemos lista vacía si la búsqueda es vacía.
        }

        // Usamos el método que ya teníamos para obtener todos los productos.
        for (Producto p : getAllProductosAsObservableList()) {
            // Recorremos la lista de proveedores de cada producto.
            for (String prov : p.getProveedores()) {
                if (prov.toLowerCase().contains(proveedorLowerCase)) {
                    productosFiltrados.add(p);
                    break; // Una vez encontrado, pasamos al siguiente producto.
                }
            }
        }
        return productosFiltrados;
    }
}