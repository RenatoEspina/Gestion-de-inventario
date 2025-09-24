package gestionInventario.utilidades;

import gestionInventario.almacen.*;
import gestionInventario.almacen.subProductos.*;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;

public class GestorPersistencia {
    private String rutaBase;
    
    public GestorPersistencia(String rutaBase) {
        this.rutaBase = rutaBase;

        // Crear carpeta si no existe
        File carpeta = new File(rutaBase);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        // Crear archivos CSV si no existen
        crearArchivoSiNoExiste(rutaBase + "secciones.csv", "nombre_seccion\n");
        crearArchivoSiNoExiste(rutaBase + "productos.csv", "seccion,nombre,proveedores,compras_totales,ventas_totales,fecha_vencimiento,stock_maximo\n");
    }
    
    private void crearArchivoSiNoExiste(String rutaArchivo, String encabezado) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(encabezado);
            } catch (IOException e) {
                System.err.println("No se pudo crear el archivo: " + rutaArchivo + " -> " + e.getMessage());
            }
        }
    }
    
    // ------------------------------------------------------------
    // Guardar inventario
    // ------------------------------------------------------------
    
    public void guardarInventario(Inventario inventario) {
        guardarSecciones(inventario);
        guardarProductos(inventario);
    }
    
    private void guardarSecciones(Inventario inventario) {
        try (FileWriter writer = new FileWriter(rutaBase + "secciones.csv")) {
            writer.write("nombre_seccion\n");
            for (String nombreSeccion : inventario.getSecciones().keySet()) {
                writer.write(escapeCSV(nombreSeccion) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar secciones: " + e.getMessage());
        }
    }
    
    private void guardarProductos(Inventario inventario) {
        try (FileWriter writer = new FileWriter(rutaBase + "productos.csv")) {
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
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------
    // Cargar inventario
    // ------------------------------------------------------------
    
    public Inventario cargarInventario() {
        Inventario inventario = new Inventario();
        cargarSecciones(inventario);
        cargarProductos(inventario);
        return inventario;
    }
    
    private void cargarSecciones(Inventario inventario) {
        LectorCSV lector = new LectorCSV(rutaBase + "secciones.csv");
        List<List<String>> datos = lector.readAll();
        
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
    
    private void cargarProductos(Inventario inventario) {
        LectorCSV lector = new LectorCSV(rutaBase + "productos.csv");
        List<List<String>> datos = lector.readAll();
        
        int inicio = 0;
        if (!datos.isEmpty() && datos.get(0).size() >= 7 && 
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
                    String fechaVencimientoStr = fila.size() >= 6 ? fila.get(5).trim() : "";
                    String stockMaxStr = fila.size() >= 7 ? fila.get(6).trim() : "";

                    String[] proveedores = proveedoresStr.split(";");
                    if (proveedores.length == 0) continue;

                    int stockInicial = comprasTotales - ventasTotales;

                    Producto producto;
                    if (!fechaVencimientoStr.isEmpty()) {
                        // Producto perecible
                        LocalDate fechaVencimiento = LocalDate.parse(fechaVencimientoStr);
                        producto = new ProductoPerecible(nombre, proveedores[0], stockInicial, fechaVencimiento);
                    } else if (!stockMaxStr.isEmpty()) {
                        // Producto premium
                        int stockMax = Integer.parseInt(stockMaxStr);
                        producto = new ProductoPremium(nombre, proveedores[0], stockInicial, stockMax);
                    } else {
                        // Producto normal
                        producto = new Producto(nombre, proveedores[0], stockInicial);
                    }

                    for (int j = 1; j < proveedores.length; j++) {
                        producto.agregarProveedor(proveedores[j]);
                    }

                    producto.ajustarComprasVentas(comprasTotales, ventasTotales);
                    inventario.agregarProducto(seccion, producto);

                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear números en línea " + (i+1) + ": " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error al procesar línea " + (i+1) + ": " + e.getMessage());
                }
            }
        }
    }
    
    // ------------------------------------------------------------
    // Métodos auxiliares CSV
    // ------------------------------------------------------------
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String unescapeCSV(String value) {
        if (value == null) return "";
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            value = value.replace("\"\"", "\"");
        }
        return value;
    }
}

