package gestionInventario.utilidades;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV{
    private String filePath;
    private String delimiter;
    private String encoding;

    public LectorCSV(String filePath, String delimiter, String encoding) {
        this.filePath = filePath;
        this.delimiter = delimiter != null ? delimiter : ",";
        this.encoding = encoding != null ? encoding : StandardCharsets.UTF_8.name();
    }

    public LectorCSV(String filePath, String delimiter) {
        this(filePath, delimiter, StandardCharsets.UTF_8.name());
    }

    public LectorCSV(String filePath) {
        this(filePath, ",", StandardCharsets.UTF_8.name());
    }

    /**
     * Parsea una línea CSV considerando campos entre comillas
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