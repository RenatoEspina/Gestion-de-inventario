package gestionInventario.utilidades;

import gestionInventario.almacen.*;
import gestionInventario.almacen.subProductos.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase responsable de la persistencia de datos del sistema de inventario.
 * Guarda y carga el estado completo del inventario desde archivos CSV.
 * 
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 */
public class GestorPersistencia {
	/** ruta donde se guardan las secciones. */
    private final String rutaSecciones;
    /** ruta donde se guardan los productos. */
    private final String rutaProductos;

    /**
     * Constructor que inicializa el gestor de persistencia.
     *
     * @param rutaBase La ruta del directorio donde se almacenarán los archivos
     * @throws IOException si no se pueden crear los directorios o archivos iniciales
     */
    public GestorPersistencia(String rutaBase) throws IOException {
        this.rutaSecciones = rutaBase + "secciones.csv";
        this.rutaProductos = rutaBase + "productos.csv";

        File carpeta = new File(rutaBase);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        crearArchivoSiNoExiste(rutaSecciones, "nombre_seccion\n");
        crearArchivoSiNoExiste(rutaProductos, "seccion,nombre,proveedores,compras_totales,ventas_totales,fecha_vencimiento,stock_maximo\n");
    }

    /**
     * Crea un archivo con el encabezado especificado si no existe.
     *
     * @param rutaArchivo Ruta del archivo a crear
     * @param encabezado Encabezado a escribir en el archivo
     * @throws IOException Si ocurre un error al crear o escribir en el archivo
     */
    private void crearArchivoSiNoExiste(String rutaArchivo, String encabezado) throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(encabezado);
            }
        }
    }

    /**
     * Guarda el estado completo del inventario en archivos CSV.
     *
     * @param inventario El inventario a guardar
     * @throws IOException Si ocurre un error durante la escritura de los archivos
     */
    public void guardarInventario(Inventario inventario) throws IOException {
        guardarSecciones(inventario);
        guardarProductos(inventario);
    }

    /**
     * Guarda las secciones del inventario en el archivo CSV correspondiente.
     *
     * @param inventario El inventario con las secciones a guardar
     * @throws IOException Si ocurre un error durante la escritura del archivo
     */
    private void guardarSecciones(Inventario inventario) throws IOException {
        try (FileWriter writer = new FileWriter(rutaSecciones)) {
            writer.write("nombre_seccion\n");
            for (String nombreSeccion : inventario.getSecciones().keySet()) {
                writer.write(escapeCSV(nombreSeccion) + "\n");
            }
        }
    }

    /**
     * Guarda los productos de todas las secciones en el archivo CSV correspondiente.
     *
     * @param inventario El inventario con los productos a guardar
     * @throws IOException Si ocurre un error durante la escritura del archivo
     */
    private void guardarProductos(Inventario inventario) throws IOException {
        try (FileWriter writer = new FileWriter(rutaProductos)) {
            writer.write("seccion,nombre,proveedores,compras_totales,ventas_totales,fecha_vencimiento,stock_maximo\n");
            for (Secciones seccion : inventario.getSecciones().values()) {
                for (Producto producto : seccion.getProductos().values()) {
                    String fechaVencimiento = "";
                    String stockMaximo = "";
                    
                    if (producto instanceof ProductoPerecible) {
                        fechaVencimiento = ((ProductoPerecible) producto).getFechaVencimiento().toString();
                    }
                    if (producto instanceof ProductoPremium) {
                        stockMaximo = String.valueOf(((ProductoPremium) producto).getStockMaximo());
                    }
                    
                    String linea = String.format("%s,%s,%s,%d,%d,%s,%s\n",
                        escapeCSV(seccion.getNombre()),
                        escapeCSV(producto.getNombre()),
                        escapeCSV(String.join(";", producto.getProveedores())),
                        producto.getComprasTotales(),
                        producto.getVentasTotales(),
                        fechaVencimiento,
                        stockMaximo);
                    writer.write(linea);
                }
            }
        }
    }

    /**
     * Carga el inventario completo desde los archivos CSV.
     *
     * @return Inventario cargado con todas las secciones y productos
     * @throws IOException Si ocurre un error durante la lectura de los archivos
     */
    public Inventario cargarInventario() throws IOException {
        Inventario inventario = new Inventario();
        cargarSecciones(inventario);
        cargarProductos(inventario);
        return inventario;
    }

    /**
     * Carga las secciones desde el archivo CSV correspondiente.
     *
     * @param inventario El inventario donde se cargarán las secciones
     * @throws IOException Si ocurre un error durante la lectura del archivo
     */
    private void cargarSecciones(Inventario inventario) throws IOException {
        LectorCSV lector = new LectorCSV(rutaSecciones);
        List<List<String>> datos = lector.readAll();
        
        int inicio = (datos.isEmpty() || datos.get(0).isEmpty() || !"nombre_seccion".equals(datos.get(0).get(0))) ? 0 : 1;
        
        for (int i = inicio; i < datos.size(); i++) {
            List<String> fila = datos.get(i);
            if (!fila.isEmpty() && !fila.get(0).trim().isEmpty()) {
                inventario.nuevaSeccion(unescapeCSV(fila.get(0)));
            }
        }
    }

    /**
     * Carga los productos desde el archivo CSV correspondiente.
     *
     * @param inventario El inventario donde se cargarán los productos
     * @throws IOException Si ocurre un error durante la lectura del archivo o procesamiento de datos
     */
    private void cargarProductos(Inventario inventario) throws IOException {
        LectorCSV lector = new LectorCSV(rutaProductos);
        List<List<String>> datos = lector.readAll();

        int inicio = (datos.isEmpty() || !"seccion".equals(datos.get(0).get(0))) ? 0 : 1;

        for (int i = inicio; i < datos.size(); i++) {
            List<String> fila = datos.get(i);
            if (fila.size() >= 5) {
                try {
                    String seccion = unescapeCSV(fila.get(0));
                    String nombre = unescapeCSV(fila.get(1));
                    String[] proveedores = unescapeCSV(fila.get(2)).split(";");
                    int comprasTotales = Integer.parseInt(fila.get(3));
                    int ventasTotales = Integer.parseInt(fila.get(4));
                    String fechaVencimientoStr = fila.size() > 5 ? fila.get(5).trim() : "";
                    String stockMaxStr = fila.size() > 6 ? fila.get(6).trim() : "";

                    if (proveedores.length == 0 || proveedores[0].trim().isEmpty()) continue;
                    
                    Producto producto;
                    if (!fechaVencimientoStr.isEmpty()) {
                        producto = new ProductoPerecible(nombre, proveedores[0], 0, LocalDate.parse(fechaVencimientoStr));
                    } else if (!stockMaxStr.isEmpty()) {
                        producto = new ProductoPremium(nombre, proveedores[0], 0, Integer.parseInt(stockMaxStr));
                    } else {
                        producto = new Producto(nombre, proveedores[0], 0);
                    }

                    for (int j = 1; j < proveedores.length; j++) {
                        producto.agregarProveedor(proveedores[j]);
                    }

                    producto.ajustarComprasVentas(comprasTotales, ventasTotales);
                    inventario.agregarProducto(seccion, producto);

                } catch (Exception e) {
                    throw new IOException("Error al procesar la línea " + (i + 1) + " del archivo de productos: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Escapa una cadena para formato CSV, añadiendo comillas si contiene caracteres especiales.
     *
     * @param value Cadena a escapar
     * @return Cadena escapada para CSV
     */
    private String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    /**
     * Remueve el escape de una cadena en formato CSV.
     *
     * @param value Cadena escapada en CSV
     * @return Cadena original sin escape
     */
    private String unescapeCSV(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            return value.replace("\"\"", "\"");
        }
        return value;
    }
}