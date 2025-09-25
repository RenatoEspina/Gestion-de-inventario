package gestionInventario.utilidades;

import gestionInventario.almacen.Secciones;
import gestionInventario.almacen.Producto;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase utilitaria para generar reportes en formato Excel de las ventas del inventario.
 * <p>
 * Utiliza la biblioteca Apache POI para crear archivos XLSX con información filtrada
 * sobre productos y sus ventas. Genera reportes estructurados con formato profesional.
 * </p>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see Secciones
 * @see Producto
 * @see XSSFWorkbook
 */
public class ExportadorExcel {

    /**
     * Genera un archivo Excel con un reporte de productos que superan un mínimo de ventas.
     * <p>
     * El reporte incluye:
     * </p>
     * <ul>
     *   <li>Nombre de la sección</li>
     *   <li>Nombre del producto</li>
     *   <li>Cantidad de ventas</li>
     *   <li>Total general de ventas</li>
     * </ul>
     * <p>
     * El archivo se guarda como "Reporte_Ventas.xlsx" en el directorio actual.
     * </p>
     * 
     * @param seccionesFiltradas Lista de secciones que contienen productos que cumplen el criterio
     * @param ventasMinimos Número mínimo de ventas requerido para incluir un producto en el reporte
     * @throws RuntimeException si ocurre un error durante la creación o escritura del archivo
     */
    public static void generarReporte(List<Secciones> seccionesFiltradas, int ventasMinimos) {
        XSSFWorkbook workbook = new XSSFWorkbook(); // Usamos directamente XSSFWorkbook
        try {
            Sheet sheet = workbook.createSheet("Reporte Ventas");

            // Estilo de cabecera
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Sección");
            header.getCell(0).setCellStyle(headerStyle);

            header.createCell(1).setCellValue("Producto");
            header.getCell(1).setCellStyle(headerStyle);

            header.createCell(2).setCellValue("Ventas");
            header.getCell(2).setCellStyle(headerStyle);

            int rowIndex = 1;
            int totalVentas = 0;

            // Llenar datos
            for (Secciones s : seccionesFiltradas) {
                for (Producto p : s.getProductos().values()) {
                    if (p.getVentasTotales() >= ventasMinimos) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(s.getNombre());
                        row.createCell(1).setCellValue(p.getNombre());
                        row.createCell(2).setCellValue(p.getVentasTotales());
                        totalVentas += p.getVentasTotales();
                    }
                }
            }

            // Total general
            Row totalRow = sheet.createRow(rowIndex);
            totalRow.createCell(0).setCellValue("TOTAL GENERAL");
            totalRow.createCell(2).setCellValue(totalVentas);

            // Ajustar columnas
            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream("Reporte_Ventas.xlsx")) {
                workbook.write(fileOut);
                System.out.println("Archivo Excel generado: Reporte_Ventas.xlsx");
            } catch (IOException e) {
                System.out.println("Error al generar el Excel: " + e.getMessage());
            }

        } finally {
            // Cerramos el workbook para liberar recursos
            try {
                workbook.close();
            } catch (IOException e) {
                // no hacemos nada, solo aseguramos cierre
            }
        }
    }
}
