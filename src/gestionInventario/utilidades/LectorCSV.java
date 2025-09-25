package gestionInventario.utilidades;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para leer y parsear archivos CSV.
 * <p>
 * Maneja campos entrecomillados y comillas escapadas. Lanza IOException en
 * caso de errores de lectura para ser manejado por capas superiores.
 * </p>
 *
 * @author Renato Espina
 * @version 2.0 (Adaptación a JavaFX)
 */
public class LectorCSV {
    private final String filePath;
    private final String delimiter;
    private final String encoding;

    public LectorCSV(String filePath) {
        this(filePath, ",", StandardCharsets.UTF_8.name());
    }

    public LectorCSV(String filePath, String delimiter, String encoding) {
        this.filePath = filePath;
        this.delimiter = delimiter;
        this.encoding = encoding;
    }

    public List<List<String>> readAll() throws IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                records.add(parseCSVLine(line));
            }
        }
        return records;
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
}