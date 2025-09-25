package gestionInventario.utilidades;

import gestionInventario.almacen.Inventario;
import gestionInventario.almacen.Producto;
import gestionInventario.almacen.Secciones;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase utilitaria para generar reportes en formato Excel.
 *
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 */
public class ExportadorExcel {

    /**
     * Genera un archivo Excel con un reporte de una lista de productos.
     *
     * @param inventario El inventario completo, para buscar la sección de cada producto.
     * @param productosFiltrados La lista de productos a incluir en el reporte.
     * @param nombreArchivo El nombre del archivo a generar (ej. "Reporte_Ventas.xlsx").
     * @throws IOException si ocurre un error durante la escritura del archivo.
     */
    public static void generarReporte(Inventario inventario, List<Producto> productosFiltrados, String nombreArchivo) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte Ventas");

            // Estilo y fuente para la cabecera
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            // Cabecera
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Sección");
            header.createCell(1).setCellValue("Producto");
            header.createCell(2).setCellValue("Ventas");
            header.forEach(cell -> cell.setCellStyle(headerStyle));

            int rowIndex = 1;
            int totalVentas = 0;

            // Llenar datos
            for (Producto p : productosFiltrados) {
                Secciones seccion = inventario.encontrarSeccionDeProducto(p);
                String nombreSeccion = (seccion != null) ? seccion.getNombre() : "No encontrada";

                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(nombreSeccion);
                row.createCell(1).setCellValue(p.getNombre());
                row.createCell(2).setCellValue(p.getVentasTotales());
                totalVentas += p.getVentasTotales();
            }

            // Fila de Total general
            Row totalRow = sheet.createRow(rowIndex);
            totalRow.createCell(1).setCellValue("TOTAL GENERAL");
            totalRow.createCell(2).setCellValue(totalVentas);
            CellStyle boldStyle = workbook.createCellStyle();
            boldStyle.setFont(font);
            totalRow.getCell(1).setCellStyle(boldStyle);
            totalRow.getCell(2).setCellStyle(boldStyle);

            // Ajustar ancho de columnas
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
                workbook.write(fileOut);
            }
        }
    }
}