package gestionInventario.utilidades;

import gestionInventario.almacen.Producto;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Clase utilitaria para crear gráficos con JFreeChart a partir de los datos del inventario.
 * @author Tu Nombre (Asistente de Programación)
 * @version 1.0
 */
public class GraficoUtilidades {

    /**
     * Crea un gráfico de barras que compara las ventas de una lista de productos.
     *
     * @param productos La lista de productos a mostrar en el gráfico.
     * @param titulo El título que se mostrará en la ventana del gráfico.
     * @return un objeto JFreeChart listo para ser mostrado.
     */
    public static JFreeChart crearGraficoDeBarrasVentas(List<Producto> productos, String titulo) {
        // 1. Crear el conjunto de datos para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 2. Llenar el dataset con los datos de los productos
        // Itera sobre la lista de productos filtrados y añade sus ventas al dataset.
        for (Producto p : productos) {
            // Añadimos el valor (ventas), la serie ("Ventas") y la categoría (nombre del producto).
            dataset.addValue(p.getVentasTotales(), "Ventas", p.getNombre());
        }

        // 3. Crear el gráfico utilizando la fábrica de JFreeChart
        JFreeChart barChart = ChartFactory.createBarChart(
            titulo,                  // Título del gráfico
            "Productos",             // Etiqueta para el eje X (categorías)
            "Unidades Vendidas",     // Etiqueta para el eje Y (valores)
            dataset,                 // El conjunto de datos que creamos
            PlotOrientation.VERTICAL, // Orientación del gráfico
            true,                    // Incluir leyenda
            true,                    // Generar tooltips (información al pasar el mouse)
            false                    // No generar URLs
        );

        return barChart;
    }
}