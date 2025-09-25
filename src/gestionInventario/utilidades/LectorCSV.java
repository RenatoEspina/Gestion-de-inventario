package gestionInventario.utilidades;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para leer y parsear archivos CSV con soporte para formatos complejos.
 * <p>
 * Proporciona funcionalidades avanzadas para leer archivos CSV incluyendo manejo de:
 * </p>
 * <ul>
 *   <li>Campos entrecomillados</li>
 *   <li>Comillas escapadas dentro de campos</li>
 *   <li>Diferentes delimitadores y codificaciones</li>
 *   <li>Líneas vacías y espacios en blanco</li>
 * </ul>
 * 
 * @author Renato Espina
 * @version 1.0
 * @see BufferedReader
 * @see InputStreamReader
 */
public class LectorCSV {
    private String filePath;
    private String delimiter;
    private String encoding;

    /**
     * Constructor completo que permite especificar todos los parámetros de configuración.
     * 
     * @param filePath  Ruta del archivo CSV a leer
     * @param delimiter Delimitador de campos (si es null, usa coma por defecto)
     * @param encoding  Codificación del archivo (si es null, usa UTF-8 por defecto)
     */
    public LectorCSV(String filePath, String delimiter, String encoding) {
        this.filePath = filePath;
        this.delimiter = delimiter != null ? delimiter : ",";
        this.encoding = encoding != null ? encoding : StandardCharsets.UTF_8.name();
    }

    /**
     * Constructor que permite especificar delimitador con codificación UTF-8 por defecto.
     * 
     * @param filePath  Ruta del archivo CSV a leer
     * @param delimiter Delimitador de campos
     */
    public LectorCSV(String filePath, String delimiter) {
        this(filePath, delimiter, StandardCharsets.UTF_8.name());
    }

    /**
     * Constructor simplificado con delimitador coma y codificación UTF-8 por defecto.
     * 
     * @param filePath Ruta del archivo CSV a leer
     */
    public LectorCSV(String filePath) {
        this(filePath, ",", StandardCharsets.UTF_8.name());
    }

    /**
     * Parsea una línea CSV considerando campos entre comillas y comillas escapadas.
     * <p>
     * Implementa un parser de CSV que maneja correctamente:
     * </p>
     * <ul>
     *   <li>Campos encerrados entre comillas dobles</li>
     *   <li>Comillas dobles escapadas como "" dentro de campos entrecomillados</li>
     *   <li>Delimitadores dentro de campos entrecomillados</li>
     *   <li>Espacios en blanco alrededor de los campos</li>
     * </ul>
     * 
     * @param line La línea de texto CSV a parsear
     * @return Lista de campos parseados
     */
    private List<String> parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // Manejo de comillas dobles
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Comilla doble escapada dentro de comillas
                    currentField.append('"');
                    i++; // Saltar la siguiente comilla
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == delimiter.charAt(0) && !inQuotes) {
                // Fin del campo (solo si no estamos entre comillas)
                fields.add(currentField.toString().trim());
                currentField.setLength(0); // Limpiar para el próximo campo
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString().trim()); // Último campo
        return fields;
    }

    /**
     * Lee y parsea todo el contenido del archivo CSV.
     * <p>
     * Retorna una lista de listas donde cada lista interna representa una línea
     * del CSV y contiene los campos de esa línea. Las líneas vacías son ignoradas.
     * </p>
     * 
     * @return Lista de todas las líneas parseadas del archivo CSV
     * @throws RuntimeException si ocurre un error de lectura del archivo
     */
    public List<List<String>> readAll() {
        List<List<String>> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), encoding))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Saltar líneas vacías
                records.add(parseCSVLine(line));
            }
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
        
        return records;
    }

    /**
     * Lee solo la primera línea del archivo CSV (encabezado).
     * <p>
     * Útil para obtener los nombres de las columnas sin cargar todo el archivo.
     * </p>
     * 
     * @return Lista con los campos del encabezado, o lista vacía si el archivo está vacío
     */
    public List<String> readHeader() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), encoding))) {
            
            String firstLine = br.readLine();
            if (firstLine != null && !firstLine.trim().isEmpty()) {
                return parseCSVLine(firstLine);
            }
            
        } catch (IOException e) {
            System.err.println("Error al leer el encabezado del CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}