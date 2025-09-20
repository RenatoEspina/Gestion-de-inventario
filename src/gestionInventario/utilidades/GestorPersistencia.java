package gestionInventario.utilidades;

import gestionInventario.almacen.Inventario;
import gestionInventario.almacen.Secciones;
import gestionInventario.almacen.Producto;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class GestorPersistencia {
    private String rutaBase;
    
    public GestorPersistencia(String rutaBase) {
        this.rutaBase = rutaBase;
    }
    
    /**
     * Guarda toda la información del inventario en archivos CSV
     */
    public void guardarInventario(Inventario inventario) {
        guardarSecciones(inventario);
        guardarProductos(inventario);
    }
    
    /**
     * Guarda las secciones en un archivo CSV
     */
    private void guardarSecciones(Inventario inventario) {
        try (FileWriter writer = new FileWriter(rutaBase + "secciones.csv")) {
            // Escribir encabezado
            writer.write("nombre_seccion\n");
            
            // Escribir cada sección
            for (String nombreSeccion : inventario.getSecciones().keySet()) {
                writer.write(escapeCSV(nombreSeccion) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar secciones: " + e.getMessage());
        }
    }
    
    /**
     * Guarda los productos en un archivo CSV
     */
    private void guardarProductos(Inventario inventario) {
        try (FileWriter writer = new FileWriter(rutaBase + "productos.csv")) {
            // Escribir encabezado
            writer.write("seccion,nombre,proveedores,compras_totales,ventas_totales\n");
            
            // Escribir cada producto de cada sección
            for (Secciones seccion : inventario.getSecciones().values()) {
                for (Producto producto : seccion.getProductos().values()) {
                    String linea = String.format("%s,%s,%s,%d,%d\n",
                            escapeCSV(seccion.getNombre()),
                            escapeCSV(producto.getNombre()),
                            escapeCSV(String.join(";", producto.getProveedores())),
                            producto.getComprasTotales(),
                            producto.getVentasTotales());
                    writer.write(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
        }
    }
    
    /**
     * Carga el inventario desde archivos CSV
     */
    public Inventario cargarInventario(String nombreEmpresa) {
        Inventario inventario = new Inventario(nombreEmpresa);
        
        // Primero cargar las secciones
        cargarSecciones(inventario);
        
        // Luego cargar los productos
        cargarProductos(inventario);
        
        return inventario;
    }
    
    /**
     * Carga las secciones desde el archivo CSV
     */
    private void cargarSecciones(Inventario inventario) {
        LectorCSV lector = new LectorCSV(rutaBase + "secciones.csv");
        List<List<String>> datos = lector.readAll();
        
        // Saltar el encabezado si existe
        int inicio = 0;
        if (!datos.isEmpty() && !datos.get(0).isEmpty() && 
            "nombre_seccion".equals(datos.get(0).get(0))) {
            inicio = 1;
        }
        
        for (int i = inicio; i < datos.size(); i++) {
            List<String> fila = datos.get(i);
            if (!fila.isEmpty()) {
                String nombreSeccion = unescapeCSV(fila.get(0));
                inventario.nuevaSeccion(nombreSeccion);
            }
        }
    }
    
    /**
     * Carga los productos desde el archivo CSV
     */
    private void cargarProductos(Inventario inventario) {
        LectorCSV lector = new LectorCSV(rutaBase + "productos.csv");
        List<List<String>> datos = lector.readAll();
        
        // Saltar el encabezado si existe
        int inicio = 0;
        if (!datos.isEmpty() && datos.get(0).size() >= 5 && 
            "seccion".equals(datos.get(0).get(0))) {
            inicio = 1;
        }
        
        for (int i = inicio; i < datos.size(); i++) {
            List<String> fila = datos.get(i);
            if (fila.size() >= 5) {
                try {
                    String seccion = unescapeCSV(fila.get(0));
                    String nombre = unescapeCSV(fila.get(1));
                    String proveedoresStr = unescapeCSV(fila.get(2));
                    int comprasTotales = Integer.parseInt(fila.get(3));
                    int ventasTotales = Integer.parseInt(fila.get(4));
                    
                    // Obtener el primer proveedor
                    String[] proveedores = proveedoresStr.split(";");
                    if (proveedores.length == 0) continue;
                    
                    // Calcular stock inicial (compras - ventas)
                    int stockInicial = comprasTotales - ventasTotales;
                    
                    // Crear el producto
                    Producto producto = new Producto(nombre, proveedores[0], stockInicial);
                    
                    // Añadir proveedores adicionales si existen
                    for (int j = 1; j < proveedores.length; j++) {
                        producto.agregarProveedor(proveedores[j]);
                    }
                    
                    // Ajustar compras y ventas totales
                    producto.ajustarComprasVentas(comprasTotales, ventasTotales);
                    
                    // Agregar producto a la sección
                    inventario.agregarProducto(seccion, producto);
                    
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear números en línea " + (i+1) + ": " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error al procesar línea " + (i+1) + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Escapa valores para CSV (envuelve en comillas si contiene comas o comillas)
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    /**
     * Desescapa valores de CSV
     */
    private String unescapeCSV(String value) {
        if (value == null) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }
}