package gestionInventario.almacen;

import gestionInventario.utilidades.Consola;
import gestionInventario.utilidades.ExportadorExcel;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import gestionInventario.excepciones.*;

/**
 * Clase principal que representa el inventario completo del almacén.
 * <p>
 * Gestiona múltiples secciones organizadas por nombre y proporciona operaciones
 * de alto nivel para administrar el inventario completo. Incluye funcionalidades
 * para filtrar productos, generar reportes y realizar operaciones masivas.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Secciones
 * @see Producto
 * @see Consola
 * @see ExportadorExcel
 * @see ProductoNoEncontradoException
 * @see StockInsuficienteException
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
     * @return Un HashMap donde la clave es el nombre de la sección y el valor es el objeto Secciones
     */
    public HashMap<String, Secciones> getSecciones() {
        return this.secciones;
    }
    
    /**
     * Crea una nueva sección en el inventario.
     * <p>
     * Si la sección ya existe, muestra un mensaje de advertencia.
     * </p>
     * 
     * @param nombre El nombre de la nueva sección a crear
     * @throws IllegalArgumentException si el nombre es null o vacío
     */
    public void nuevaSeccion(String nombre) {
        if (!this.secciones.containsKey(nombre)) {
            Secciones seccion = new Secciones(nombre);
            this.secciones.put(nombre, seccion);
        } else {
            System.out.println("LA SECCION YA EXISTE!!!");
        }
    }

    /**
     * Elimina una sección existente del inventario.
     * 
     * @param nombre El nombre de la sección a eliminar
     */
    public void eliminarSeccion(String nombre) {
        if (this.secciones.containsKey(nombre)) {
            secciones.remove(nombre);
        } else {
            System.out.println("LA SECCION NO EXISTE!!!");
        }
    }
    
    /**
     * Elimina un producto de todo el inventario, buscando en todas las secciones.
     * 
     * @param nombre El nombre del producto a eliminar
     * @throws ProductoNoEncontradoException si el producto no se encuentra en ninguna sección
     */
    public void eliminarProducto(String nombre) throws ProductoNoEncontradoException {
        for (Secciones s : secciones.values()) {
            Producto p = s.getProductos().get(nombre);
            if (p != null) {
                if (s.getProductos().remove(nombre, p)) {
                    System.out.println("Eliminado con exito!!!");
                    return;
                }
            }
        }
        throw new ProductoNoEncontradoException("No se encontró el producto " + nombre + " en el sistema.");
    }
    
    /**
     * Agrega un producto al inventario, solicitando interactivamente la sección destino.
     * <p>
     * Muestra la lista de secciones disponibles y permite al usuario seleccionar una.
     * </p>
     * 
     * @param producto El producto a agregar al inventario
     */
    public void agregarProducto(Producto producto) {
        System.out.println("¿A qué sección desea agregar el producto?");
        for (String key : secciones.keySet()) {
            System.out.println("- " + key);
        }
        String seccionBuscada = Consola.leerString(null);
        Secciones seccion = this.secciones.get(seccionBuscada);
        if (seccion == null) {
            System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
            return;
        }
        seccion.agregarProducto(producto);
    }
    
    /**
     * Agrega un producto a una sección específica del inventario.
     * 
     * @param seccionNombre El nombre de la sección destino
     * @param producto El producto a agregar
     */
    public void agregarProducto(String seccionNombre, Producto producto) {
        Secciones seccion = this.secciones.get(seccionNombre);
        if (seccion == null) {
            System.out.println("LA SECCIÓN " + seccionNombre + " NO EXISTE EN EL SISTEMA!!");
            return;
        }
        seccion.agregarProducto(producto);
    }
    
    /**
     * Realiza una compra de producto, solicitando interactivamente la sección.
     * <p>
     * Muestra la lista de secciones y permite al usuario seleccionar donde realizar la compra.
     * </p>
     * 
     * @param nombreProducto El nombre del producto a comprar
     * @param cantidad La cantidad de unidades a comprar
     * @param proveedor El proveedor de la compra
     */
    public void comprarProducto(String nombreProducto, int cantidad, String proveedor) {
        System.out.println("¿A qué sección desea ingresar?");
        for (String key : secciones.keySet()) {
            System.out.println("- " + key);
        }
        String seccionBuscada = Consola.leerString(null);
        Secciones seccion = secciones.get(seccionBuscada);
        if (seccion == null) {
            System.out.println("LA SECCIÓN BUSCADA NO EXISTE EN EL SISTEMA!!");
            return;
        }
        seccion.comprarProducto(nombreProducto, cantidad, proveedor);
    }

    /**
     * Realiza una venta de producto, solicitando interactivamente la sección.
     * <p>
     * Verifica la existencia del producto y el stock disponible antes de realizar la venta.
     * </p>
     * 
     * @param nombreProducto El nombre del producto a vender
     * @param cantidad La cantidad de unidades a vender
     * @throws ProductoNoEncontradoException si el producto no existe en la sección seleccionada
     * @throws StockInsuficienteException si no hay suficiente stock para la venta
     */
    public void venderProducto(String nombreProducto, int cantidad) throws ProductoNoEncontradoException, StockInsuficienteException {
        System.out.println("¿A qué sección desea ingresar?");
        for (String key : secciones.keySet()) {
            System.out.println("- " + key);
        }
        String seccionBuscada = Consola.leerString(null);
        Secciones seccion = secciones.get(seccionBuscada);
        if (seccion == null) {
            throw new ProductoNoEncontradoException("La sección " + seccionBuscada + " no existe en el sistema.");
        }
        seccion.venderProducto(nombreProducto, cantidad);
    }
    
    /**
     * Filtra productos que tienen un número mínimo de ventas y genera un reporte.
     * <p>
     * Recorre todas las secciones y productos, mostrando aquellos que cumplen con el criterio.
     * Ofrece la opción de generar un reporte en Excel con los resultados.
     * </p>
     * 
     * @param ventas El número mínimo de ventas requerido para incluir un producto en el filtro
     */
    public void filtrarProductos(int ventas) {
        int sumatoriaTotal = 0;
        boolean encontroAlMenosUno = false;

        // Lista para guardar secciones que cumplen el criterio
        List<Secciones> seccionesFiltradas = new ArrayList<>();

        for (Secciones s : secciones.values()) {
            int sumatoriaSec = 0;
            boolean encontroEnSeccion = false;

            System.out.println("\n-Sección: " + s.getNombre());

            for (Producto p : s.getProductos().values()) {
                if (p.getVentasTotales() >= ventas) {
                    encontroEnSeccion = true;
                    encontroAlMenosUno = true;
                    sumatoriaSec += p.getVentasTotales();
                    System.out.println("   - " + p.getNombre() + " tiene " + p.getVentasTotales() + " ventas.");
                }
            }

            if (!encontroEnSeccion) {
                System.out.println("   No existen productos con las ventas solicitadas en esta sección.");
            } else {
                sumatoriaTotal += sumatoriaSec;
                seccionesFiltradas.add(s); // Guardamos la sección que tiene productos que cumplen el criterio
                System.out.println("   Total de ventas en la sección: " + sumatoriaSec);
            }
        }

        if (!encontroAlMenosUno) {
            System.out.println("\nNo se encontraron productos que cumplan el criterio.");
        } else {
            System.out.println("\nHubo un total de " + sumatoriaTotal + " ventas en todas las secciones.");
        }

        String opcion = Consola.leerString("¿Desea generar un archivo reporte? (si/no): ");
        if (opcion.equalsIgnoreCase("si")) {
            ExportadorExcel.generarReporte(seccionesFiltradas, ventas);
        }
    }
    
    /**
     * Muestra la información de un producto específico, solicitando interactivamente la sección.
     * <p>
     * Permite al usuario navegar por las secciones y seleccionar un producto para ver su información detallada.
     * </p>
     */
    public void informacionProducto() {
        System.out.println("¿A qué sección desea ingresar?");
        for (String key : secciones.keySet()) {
            System.out.println("- " + key);
        }
        String seccionBuscada = Consola.leerString(null);
        Secciones seccion = secciones.get(seccionBuscada);
        if (seccion == null) {
            System.out.println("LA SECCION BUSCADA NO EXISTE EN EL SISTEMA!!");
            return;
        }
        Consola.limpiarPantalla();
        System.out.println("Seccion ingresada:" + seccionBuscada);
        seccion.listarProductos();
        String nombreProducto = Consola.leerString("Ingrese nombre del producto: ");
        seccion.informacionProducto(nombreProducto);
    }
}