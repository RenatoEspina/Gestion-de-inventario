package gestionInventario.utilidades;

import gestionInventario.almacen.Secciones;
import gestionInventario.almacen.Producto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorCSV {

    public static void generarReporte(List<Secciones> seccionesFiltradas, int ventasMinimos) {
        String archivoCSV = "Reporte_Ventas.csv";
        int totalVentas = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoCSV))) {
            // Escribir cabecera
            writer.write("Sección,Producto,Ventas");
            writer.newLine();

            // Escribir datos
            for (Secciones s : seccionesFiltradas) {
                for (Producto p : s.getProductos().values()) {
                    if (p.getVentasTotales() >= ventasMinimos) {
                        writer.write(String.format("%s,%s,%d",
                                s.getNombre(),
                                p.getNombre(),
                                p.getVentasTotales()));
                        writer.newLine();
                        totalVentas += p.getVentasTotales();
                    }
                }
            }

            // Total general
            writer.write(String.format("TOTAL GENERAL,,%d", totalVentas));
            writer.newLine();

            System.out.println("Archivo CSV generado: " + archivoCSV);

        } catch (IOException e) {
            System.out.println("Error al generar el CSV: " + e.getMessage());
        }
    }
}
