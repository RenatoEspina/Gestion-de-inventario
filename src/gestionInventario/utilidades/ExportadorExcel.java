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
 * Clase utilitaria para generar reportes en formato Excel (.xlsx) del inventario.
 * Proporciona métodos para crear reportes filtrados por ventas y por proveedor.
 * 
 * @author Renato Espina
 * @version 2.3
 */
public class ExportadorExcel {

	/**
	 * Crea un nuevo exportador de datos a Excel.
	 */
	public ExportadorExcel() { }
	
    /**
     * Genera un reporte de Excel con productos filtrados por ventas mínimas.
     * El reporte incluye información detallada de cada producto y su sección correspondiente.
     * 
     * @param inventario El inventario del cual obtener los productos
     * @param productosFiltrados Lista de productos filtrados por ventas
     * @param nombreArchivo Ruta y nombre del archivo Excel a generar
     * @throws IOException Si ocurre un error durante la escritura del archivo
     */
    public static void generarReporteVentas(Inventario inventario, List<Producto> productosFiltrados, String nombreArchivo) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte Ventas");
            CellStyle headerStyle = createHeaderStyle(workbook);

            Row header = sheet.createRow(0);
            String[] titulos = {"Sección", "Producto", "Stock", "Compras Totales", "Ventas Totales"};
            for (int i = 0; i < titulos.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(titulos[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (Producto producto : productosFiltrados) {
                Secciones seccion = inventario.encontrarSeccionDeProducto(producto.getNombre());
                String nombreSeccion = (seccion != null) ? seccion.getNombre() : "N/A";

                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(nombreSeccion);
                row.createCell(1).setCellValue(producto.getNombre());
                row.createCell(2).setCellValue(producto.getStock());
                row.createCell(3).setCellValue(producto.getComprasTotales());
                row.createCell(4).setCellValue(producto.getVentasTotales());
            }

            for (int i = 0; i < titulos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
                workbook.write(fileOut);
            }
        }
    }

    /**
     * Genera un reporte de Excel con productos filtrados por proveedor.
     * El reporte incluye información básica de cada producto.
     * 
     * @param productosFiltrados Lista de productos filtrados por proveedor
     * @param nombreArchivo Ruta y nombre del archivo Excel a generar
     * @throws IOException Si ocurre un error durante la escritura del archivo
     */
    public static void generarReporteProveedor(List<Producto> productosFiltrados, String nombreArchivo) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte Proveedor");
            CellStyle headerStyle = createHeaderStyle(workbook);

            Row header = sheet.createRow(0);
            String[] titulos = {"Producto", "Stock", "Compras Totales", "Ventas Totales"};
            for (int i = 0; i < titulos.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(titulos[i]);
                cell.setCellStyle(headerStyle);
            }
            
            int rowIndex = 1;
            for (Producto producto : productosFiltrados) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(producto.getNombre());
                row.createCell(1).setCellValue(producto.getStock());
                row.createCell(2).setCellValue(producto.getComprasTotales());
                row.createCell(3).setCellValue(producto.getVentasTotales());
            }

            for (int i = 0; i < titulos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
                workbook.write(fileOut);
            }
        }
    }

    /**
     * Crea y configura el estilo para las celdas de encabezado en el archivo Excel.
     * 
     * @param workbook El libro de trabajo de Excel
     * @return CellStyle configurado para encabezados con texto en negrita
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }
}