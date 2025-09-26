package gestionInventario.utilidades;

import gestionInventario.almacen.Inventario;
import gestionInventario.almacen.Producto;
import gestionInventario.almacen.Secciones;
import gestionInventario.almacen.subProductos.ProductoPerecible;
import gestionInventario.almacen.subProductos.ProductoPremium;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase utilitaria para generar un reporte completo del inventario en formato .txt.
 *
 * @author Renato Espina (y tu nombre)
 * @version 1.0
 */
public class ExportadorTXT {

	/**
	 * Crea un nuevo exportador de datos .txt.
	 */
	public ExportadorTXT() { }
	
    /**
     * Genera un archivo de texto con el estado completo del inventario,
     * incluyendo todas las secciones y productos con sus detalles.
     *
     * @param inventario El inventario completo a exportar.
     * @param rutaArchivo La ruta completa donde se guardará el archivo .txt.
     * @throws IOException si ocurre un error durante la escritura del archivo.
     */
    public static void generarReporteCompleto(Inventario inventario, String rutaArchivo) throws IOException {
        // Usamos try-with-resources para asegurar que el writer se cierre automáticamente
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {

            // Encabezado del reporte
            writer.write("========================================\n");
            writer.write("    INFORME GENERAL DE INVENTARIO\n");
            writer.write("========================================\n");
            writer.write("Generado el: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n");

            // Recorremos cada sección del inventario
            for (Secciones seccion : inventario.getSecciones().values()) {
                writer.write("SECCIÓN: " + seccion.getNombre().toUpperCase() + "\n");
                writer.write("----------------------------------------\n");

                if (seccion.getProductos().isEmpty()) {
                    writer.write("    (No hay productos en esta sección)\n\n");
                    continue; // Pasa a la siguiente sección
                }

                // Recorremos cada producto dentro de la sección
                for (Producto p : seccion.getProductos().values()) {
                    writer.write("    -> Producto: " + p.getNombre() + "\n");
                    writer.write("       - Stock Actual: " + p.getStock() + "\n");
                    writer.write("       - Compras Totales: " + p.getComprasTotales() + "\n");
                    writer.write("       - Ventas Totales: " + p.getVentasTotales() + "\n");

                    // Verificamos si es un tipo de producto especial para añadir sus datos extra
                    if (p instanceof ProductoPerecible) {
                        ProductoPerecible pp = (ProductoPerecible) p;
                        writer.write("       - Fecha Vencimiento: " + pp.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
                    } else if (p instanceof ProductoPremium) {
                        ProductoPremium pp = (ProductoPremium) p;
                        writer.write("       - Stock Máximo: " + pp.getStockMaximo() + "\n");
                    }
                    writer.write("\n"); // Espacio entre productos
                }
                writer.write("\n"); // Espacio extra entre secciones
            }

            // Pie de página del reporte
            writer.write("========================================\n");
            writer.write("            FIN DEL REPORTE\n");
            writer.write("========================================\n");
        }
    }
}